package net.lightbody.bmp.proxy.http;

import cz.mallat.uasparser.CachingOnlineUpdateUASparser;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarCookie;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarNameVersion;
import net.lightbody.bmp.core.har.HarPostData;
import net.lightbody.bmp.core.har.HarPostDataParam;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.core.har.HarTimings;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.util.Base64;
import net.lightbody.bmp.proxy.util.CappedByteArrayOutputStream;
import net.lightbody.bmp.proxy.util.ClonedOutputStream;
import net.lightbody.bmp.proxy.util.IOUtils;
import net.lightbody.bmp.proxy.util.Log;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.java_bandwidthlimiter.StreamManager;
import org.xbill.DNS.Cache;
import org.xbill.DNS.DClass;
import sun.nio.ch.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class BrowserMobHttpClient {
    private static final Log LOG = new Log();
    private static final int MAX_BUFFER_SIZE = 1024 * 1024; // MOB-216 don't buffer more than 1 MB
    public static UASparser PARSER = null;

    static {
        try {
            PARSER = new CachingOnlineUpdateUASparser();
        } catch (IOException e) {
            LOG.severe("Unable to create User-Agent parser, falling back but proxy is in damaged state and should be restarted", e);
            try {
                PARSER = new UASparser();
            } catch (Exception e1) {
                // ignore
            }
        }
    }

    public static void setUserAgentParser(final UASparser parser) {
        PARSER = parser;
    }

    private static final int BUFFER = 4096;

    private Har har;
    private String harPageRef;

    private boolean captureHeaders;
    private boolean captureContent;
    // if captureContent is set, default policy is to capture binary contents too
    private boolean captureBinaryContent = true;

    private SimulatedSocketFactory socketFactory;
    private TrustingSSLSocketFactory sslSocketFactory;
    private ThreadSafeClientConnManager httpClientConnMgr; // use PoolingHttpClientConnectionManager
    private DefaultHttpClient httpClient;
    private List<BlacklistEntry> blacklistEntries = null;
    private WhitelistEntry whitelistEntry = null;
    private final List<RewriteRule> rewriteRules = new CopyOnWriteArrayList<RewriteRule>();
    private final List<RequestInterceptor> requestInterceptors = new CopyOnWriteArrayList<RequestInterceptor>();
    private final List<ResponseInterceptor> responseInterceptors = new CopyOnWriteArrayList<ResponseInterceptor>();
    private final HashMap<String, String> additionalHeaders = new LinkedHashMap<String, String>();
    private int requestTimeout;
    private final AtomicBoolean allowNewRequests = new AtomicBoolean(true);
    private BrowserMobHostNameResolver hostNameResolver;
    private boolean decompress = true;
    // not using CopyOnWriteArray because we're WRITE heavy and it is for READ heavy operations
    // instead doing it the old fashioned way with a synchronized block
    private final Set<ActiveRequest> activeRequests = new HashSet<ActiveRequest>();
    private WildcardMatchingCredentialsProvider credsProvider;
    private boolean shutdown = false;
    private AuthType authType;

    private boolean followRedirects = true;
    private static final int MAX_REDIRECT = 10;
    private AtomicInteger requestCounter;

    public BrowserMobHttpClient(final StreamManager streamManager, final AtomicInteger requestCounter, final int requestTimeOut) {
        this.requestCounter = requestCounter;
        this.requestTimeout = requestTimeOut;
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        hostNameResolver = new BrowserMobHostNameResolver(new Cache(DClass.ANY));

        socketFactory = new SimulatedSocketFactory(hostNameResolver, streamManager, requestTimeout);
        try {
            sslSocketFactory = new TrustingSSLSocketFactory(hostNameResolver, streamManager, requestTimeout);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }

        sslSocketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());

        schemeRegistry.register(new Scheme("http", 80, socketFactory));
        schemeRegistry.register(new Scheme("https", 443, sslSocketFactory));

        httpClientConnMgr = new ThreadSafeClientConnManager(schemeRegistry) {
            @Override
            public ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
                final ClientConnectionRequest wrapped = super.requestConnection(route, state);
                return new ClientConnectionRequest() {
                    @Override
                    public ManagedClientConnection getConnection(final long timeout, final TimeUnit tunit) throws InterruptedException,
                        ConnectionPoolTimeoutException {
                        Date start = new Date();
                        try {
                            return wrapped.getConnection(timeout, tunit);
                        } finally {
                            RequestInfo.get().blocked(start, new Date());
                        }
                    }

                    @Override
                    public void abortRequest() {
                        wrapped.abortRequest();
                    }
                };
            }
        };

        // MOB-338: 30 total connections and 6 connections per host matches the behavior in Firefox 3
        httpClientConnMgr.setMaxTotal(30);
        httpClientConnMgr.setDefaultMaxPerRoute(6);

        httpClient = new DefaultHttpClient(httpClientConnMgr) {
            @Override
            protected HttpRequestExecutor createRequestExecutor() {
                return new HttpRequestExecutor() {
                    @Override
                    protected HttpResponse doSendRequest(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
                        throws IOException, HttpException {
                        Date start = new Date();
                        HttpResponse response = super.doSendRequest(request, conn, context);
                        RequestInfo.get().send(start, new Date());
                        return response;
                    }

                    @Override
                    protected HttpResponse doReceiveResponse(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
                        throws HttpException, IOException {
                        Date start = new Date();
                        HttpResponse response = super.doReceiveResponse(request, conn, context);
                        RequestInfo.get().wait(start, new Date());
                        return response;
                    }
                };
            }
        };
        credsProvider = new WildcardMatchingCredentialsProvider();
        httpClient.setCredentialsProvider(credsProvider);
        httpClient.addRequestInterceptor(new PreemptiveAuth(), 0);
        httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, Boolean.TRUE);
        setRetryCount(0);

        // we always set this to false so it can be handled manually:
        httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);

        HttpClientInterrupter.watch(this);
        setConnectionTimeout(requestTimeOut);
        setSocketOperationTimeout(requestTimeOut);
        setRequestTimeout(-1);
    }

    public void setRetryCount(final int count) {
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(count, false));
    }

    public void remapHost(final String source, final String target) {
        hostNameResolver.remap(source, target);
    }

    @Deprecated
    public void addRequestInterceptor(final HttpRequestInterceptor i) {
        httpClient.addRequestInterceptor(i);
    }

    public void addRequestInterceptor(final RequestInterceptor interceptor) {
        requestInterceptors.add(interceptor);
    }

    @Deprecated
    public void addResponseInterceptor(final HttpResponseInterceptor i) {
        httpClient.addResponseInterceptor(i);
    }

    public void addResponseInterceptor(final ResponseInterceptor interceptor) {
        responseInterceptors.add(interceptor);
    }

    public void createCookie(final String name, final String value, final String domain) {
        createCookie(name, value, domain, null);
    }

    public void createCookie(final String name, final String value, final String domain, final String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        if (path != null) {
            cookie.setPath(path);
        }
        httpClient.getCookieStore().addCookie(cookie);
    }

    public void clearCookies() {
        httpClient.getCookieStore().clear();
    }

    public Cookie getCookie(final String name) {
        return getCookie(name, null, null);
    }

    public Cookie getCookie(final String name, final String domain) {
        return getCookie(name, domain, null);
    }

    public Cookie getCookie(final String name, final String domain, final String path) {
        for (Cookie cookie : httpClient.getCookieStore().getCookies()) {
            if (cookie.getName().equals(name)) {
                if (domain != null && !domain.equals(cookie.getDomain())) {
                    continue;
                }
                if (path != null && !path.equals(cookie.getPath())) {
                    continue;
                }

                return cookie;
            }
        }

        return null;
    }

    public BrowserMobHttpRequest newPost(final String url, final net.lightbody.bmp.proxy.jetty.http.HttpRequest proxyRequest) {
        try {
            URI uri = makeUri(url);
            return new BrowserMobHttpRequest(new HttpPost(uri), this, -1, captureContent, proxyRequest);
        } catch (URISyntaxException e) {
            throw reportBadURI(url, "POST");
        }
    }

    public BrowserMobHttpRequest newGet(final String url, final net.lightbody.bmp.proxy.jetty.http.HttpRequest proxyRequest) {
        try {
            URI uri = makeUri(url);
            return new BrowserMobHttpRequest(new HttpGet(uri), this, -1, captureContent, proxyRequest);
        } catch (URISyntaxException e) {
            throw reportBadURI(url, "GET");
        }
    }

    public BrowserMobHttpRequest newPut(final String url, final net.lightbody.bmp.proxy.jetty.http.HttpRequest proxyRequest) {
        try {
            URI uri = makeUri(url);
            return new BrowserMobHttpRequest(new HttpPut(uri), this, -1, captureContent, proxyRequest);
        } catch (Exception e) {
            throw reportBadURI(url, "PUT");
        }
    }

    public BrowserMobHttpRequest newDelete(final String url, final net.lightbody.bmp.proxy.jetty.http.HttpRequest proxyRequest) {
        try {
            URI uri = makeUri(url);
            return new BrowserMobHttpRequest(new HttpDelete(uri), this, -1, captureContent, proxyRequest);
        } catch (URISyntaxException e) {
            throw reportBadURI(url, "DELETE");
        }
    }

    public BrowserMobHttpRequest newOptions(final String url, final net.lightbody.bmp.proxy.jetty.http.HttpRequest proxyRequest) {
        try {
            URI uri = makeUri(url);
            return new BrowserMobHttpRequest(new HttpOptions(uri), this, -1, captureContent, proxyRequest);
        } catch (URISyntaxException e) {
            throw reportBadURI(url, "OPTIONS");
        }
    }

    public BrowserMobHttpRequest newHead(final String url, final net.lightbody.bmp.proxy.jetty.http.HttpRequest proxyRequest) {
        try {
            URI uri = makeUri(url);
            return new BrowserMobHttpRequest(new HttpHead(uri), this, -1, captureContent, proxyRequest);
        } catch (URISyntaxException e) {
            throw reportBadURI(url, "HEAD");
        }
    }

    private URI makeUri(String url) throws URISyntaxException {
        // MOB-120: check for | character and change to correctly escaped %7C
        url = url.replace(" ", "%20");
        url = url.replace(">", "%3C");
        url = url.replace("<", "%3E");
        url = url.replace("#", "%23");
        url = url.replace("{", "%7B");
        url = url.replace("}", "%7D");
        url = url.replace("|", "%7C");
        url = url.replace("\\", "%5C");
        url = url.replace("^", "%5E");
        url = url.replace("~", "%7E");
        url = url.replace("[", "%5B");
        url = url.replace("]", "%5D");
        url = url.replace("`", "%60");
        url = url.replace("\"", "%22");

        URI uri = new URI(url);

        // are we using the default ports for http/https? if so, let's rewrite the URI to make sure the :80 or :443
        // is NOT included in the string form the URI. The reason we do this is that in HttpClient 4.0 the Host header
        // would include a value such as "yahoo.com:80" rather than "yahoo.com". Not sure why this happens but we don't
        // want it to, and rewriting the URI solves it
        if ((uri.getPort() == 80 && "http".equals(uri.getScheme())) || (uri.getPort() == 443 && "https".equals(uri.getScheme()))) {
            // we rewrite the URL with a StringBuilder (vs passing in the components of the URI) because if we were
            // to pass in these components using the URI's 7-arg constructor query parameters get double escaped (bad!)
            StringBuilder sb = new StringBuilder(uri.getScheme()).append("://");
            if (uri.getRawUserInfo() != null) {
                sb.append(uri.getRawUserInfo()).append("@");
            }
            sb.append(uri.getHost());
            if (uri.getRawPath() != null) {
                sb.append(uri.getRawPath());
            }
            if (uri.getRawQuery() != null) {
                sb.append("?").append(uri.getRawQuery());
            }
            if (uri.getRawFragment() != null) {
                sb.append("#").append(uri.getRawFragment());
            }

            uri = new URI(sb.toString());
        }
        return uri;
    }

    private RuntimeException reportBadURI(final String url, final String method) {
        if (har != null && harPageRef != null) {
            HarEntry entry = new HarEntry(harPageRef, BrowserMobHttpRequest.TIME_STAMP_BASED_ID_GENERATOR.nextIdentifier());
            entry.setTime(0);
            entry.setRequest(new HarRequest(method, url, "HTTP/1.1"));
            entry.setResponse(new HarResponse(-998, "Bad URI", "HTTP/1.1"));
            entry.setTimings(new HarTimings());
            har.getLog().addEntry(entry);
        }

        throw new BadURIException("Bad URI requested: " + url);
    }

    public void checkTimeout() {
        synchronized (activeRequests) {
            for (ActiveRequest activeRequest : activeRequests) {
                activeRequest.checkTimeout();
            }
        }
    }

    //MAIN METHOD TO HANDLE A REQUEST AND PREPARE A RESULT
    public BrowserMobHttpResponse execute(final BrowserMobHttpRequest req) {
        if (!allowNewRequests.get()) {
            throw new RuntimeException("No more requests allowed");
        }

        try {
            //            long start = System.currentTimeMillis();
            boolean isResponseVolatile = ProxyServer.getResponseVolatile(); //this is the base of response volatility
            req.setResponseVolatile(isResponseVolatile);

            requestCounter.incrementAndGet();
            for (RequestInterceptor interceptor : requestInterceptors) {
                interceptor.process(req);
            }

            //            long request = System.currentTimeMillis();
            //            System.out.println("REQUEST-INTERCEPTORS: " + (request - start));

            // Response volatility might be overwritten in request interceptors, but not later, so from now it is fixed:
            isResponseVolatile = req.getResponseVolatile();

            BrowserMobHttpResponse response = execute(req, 1, isResponseVolatile);

            //            long respStart = System.currentTimeMillis();
            //            System.out.println("REQUEST-EXECUTION-RESPONSE: " + (respStart - request));

            for (ResponseInterceptor interceptor : responseInterceptors) {
                interceptor.process(response);
            }

            //            long responseEnd = System.currentTimeMillis();
            //            System.out.println("RESPONSE-INTERCEPTORS: " + (responseEnd - respStart));
            if (isResponseVolatile) {
                response.doAnswer();
            }
            return response;
        } finally {
            requestCounter.decrementAndGet();
        }
    }

    //
    //If we were making cake, this would be the filling :)
    //Sending the prepared - maybe altered - request to the server, and getting back the result
    //
    private BrowserMobHttpResponse execute(final BrowserMobHttpRequest req, int depth, boolean isResponseVolatile) {
        if (depth >= MAX_REDIRECT) {
            throw new IllegalStateException("Max number of redirects (" + MAX_REDIRECT + ") reached");
        }

        RequestCallback callback = req.getRequestCallback();

        HttpRequestBase method = req.getMethod();
        String verificationText = req.getVerificationText();
        String url = method.getURI().toString();

        // save the browser and version if it's not yet been set
        if (har != null && har.getLog().getBrowser() == null) {
            Header[] uaHeaders = method.getHeaders("User-Agent");
            if (uaHeaders != null && uaHeaders.length > 0) {
                String userAgent = uaHeaders[0].getValue();
                try {
                    // note: this doesn't work for 'Fandango/4.5.1 CFNetwork/548.1.4 Darwin/11.0.0'
                    UserAgentInfo uai = PARSER.parse(userAgent);
                    String name = uai.getUaName();
                    int lastSpace = name.lastIndexOf(' ');
                    String browser = name.substring(0, lastSpace);
                    String version = name.substring(lastSpace + 1);
                    har.getLog().setBrowser(new HarNameVersion(browser, version));
                } catch (IOException e) {
                    // ignore it, it's fine
                } catch (Exception e) {
                    LOG.warn("Failed to parse user agent string", e);
                }
            }
        }

        // process any rewrite requests
        boolean rewrote = false;
        String newUrl = url;
        for (RewriteRule rule : rewriteRules) {
            Matcher matcher = rule.match.matcher(newUrl);
            newUrl = matcher.replaceAll(rule.replace);
            rewrote = true;
        }

        if (rewrote) {
            try {
                method.setURI(new URI(newUrl));
                url = newUrl;
            } catch (URISyntaxException e) {
                LOG.warn("Could not rewrite url to %s", newUrl);
            }
        }

        // handle whitelist and blacklist entries
        int mockResponseCode = -1;
        if (whitelistEntry != null) {
            boolean found = false;
            for (Pattern pattern : whitelistEntry.patterns) {
                if (pattern.matcher(url).matches()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                mockResponseCode = whitelistEntry.responseCode;
            }
        }

        if (blacklistEntries != null) {
            for (BlacklistEntry blacklistEntry : blacklistEntries) {
                if (blacklistEntry.pattern.matcher(url).matches()) {
                    mockResponseCode = blacklistEntry.responseCode;
                    break;
                }
            }
        }

        if (!additionalHeaders.isEmpty()) {
            // Set the additional headers
            for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                method.removeHeaders(key);
                method.addHeader(key, value);
            }
        }

        String charSet = "UTF-8";
        String responseBody = null;

        InputStream is = null;
        int statusCode = -998;
        long bytes = 0;
        boolean gzipping = false;
        boolean deflating = false;
        boolean contentMatched = true;
        OutputStream os = req.getOutputStream();
        if (os == null) {
            os = new CappedByteArrayOutputStream(MAX_BUFFER_SIZE);
        }
        if (verificationText != null) {
            contentMatched = false;
        }
        Date start = new Date();

        // link the object up now, before we make the request, so that if we get cut off (ie: favicon.ico request and browser shuts down)
        // we still have the attempt associated, even if we never got a response
        HarEntry entry = new HarEntry(harPageRef, req.getWilmaMessageId());

        // clear out any connection-related information so that it's not stale from previous use of this thread.
        RequestInfo.clear(url, entry);

        entry.setRequest(new HarRequest(method.getMethod(), url, method.getProtocolVersion().getProtocol()));
        entry.setResponse(new HarResponse(-999, "NO RESPONSE", method.getProtocolVersion().getProtocol()));
        if (har != null && harPageRef != null) {
            har.getLog().addEntry(entry);
        }

        String query = method.getURI().getRawQuery();
        if (query != null) {
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(query, params, "UTF-8");
            for (String k : params.keySet()) {
                for (Object v : params.getValues(k)) {
                    entry.getRequest().getQueryString().add(new HarNameValuePair(k, (String) v));
                }
            }
        }

        String errorMessage = null;
        HttpResponse response = null;

        BasicHttpContext ctx = new BasicHttpContext();

        ActiveRequest activeRequest = new ActiveRequest(method, ctx, entry.getStartedDateTime());
        synchronized (activeRequests) {
            activeRequests.add(activeRequest);
        }

        // for dealing with automatic authentication
        if (authType == AuthType.NTLM) {
            // todo: not supported yet
            //ctx.setAttribute("preemptive-auth", new NTLMScheme(new JCIFSEngine()));
        } else if (authType == AuthType.BASIC) {
            ctx.setAttribute("preemptive-auth", new BasicScheme());
        }

        StatusLine statusLine = null;
        ByteArrayOutputStream bos = null;

        try {
            // set the User-Agent if it's not already set
            if (method.getHeaders("User-Agent").length == 0) {
                method.addHeader("User-Agent", "BrowserMob VU/1.0");
            }

            // was the request mocked out?
            if (mockResponseCode != -1) {
                statusCode = mockResponseCode;

                // TODO: HACKY!!
                callback.handleHeaders(new Header[]{new Header() {
                    @Override
                    public String getName() {
                        return "Content-Type";
                    }

                    @Override
                    public String getValue() {
                        return "text/plain";
                    }

                    @Override
                    public HeaderElement[] getElements() throws ParseException {
                        return new HeaderElement[0];
                    }
                }});
            } else {
                response = httpClient.execute(method, ctx);

                statusLine = response.getStatusLine();
                statusCode = statusLine.getStatusCode();

                if (callback != null) {
                    callback.handleStatusLine(statusLine);
                    callback.handleHeaders(response.getAllHeaders());
                }

                if (response.getEntity() != null) {
                    is = response.getEntity().getContent();
                }

                // check for null (resp 204 can cause HttpClient to return null, which is what Google does with http://clients1.google.com/generate_204)
                if (is != null) {
                    Header contentEncodingHeader = response.getFirstHeader("Content-Encoding");
                    if (contentEncodingHeader != null) {
                        if ("gzip".equalsIgnoreCase(contentEncodingHeader.getValue())) {
                            gzipping = true;
                            }
                        else if ("deflate".equalsIgnoreCase(contentEncodingHeader.getValue())) {
                            deflating = true;
                            }
                    }

                    // deal with GZIP content!
                    if(decompress && response.getEntity().getContentLength() != 0) { //getContentLength<0 if unknown
                        if (gzipping) {
                            is = new GZIPInputStream(is);
                            }
                        else if (deflating) {  //RAW deflate only
                            // WARN : if system is using zlib<=1.1.4 the stream must be append with a dummy byte
                            // that is not required for zlib>1.1.4 (not mentioned on current Inflater javadoc)
                            is = new InflaterInputStream(is, new Inflater(true));
                            }
                    }

                    if (isResponseVolatile) {
                        //response content is volatile
                        bytes = is.available();
                        bos = new ByteArrayOutputStream();
                        org.apache.commons.io.IOUtils.copy(is, bos);
                    } else {
                        //response content is not volatile
                        if (captureContent) {
                            // todo - something here?
                            os = new ClonedOutputStream(os);

                        }
                        bytes = copyWithStatsDynamic(is, os); //if copied to os, then response gone back
                    }
                }
            }
        } catch (Exception e) {
            errorMessage = e.toString();

            if (callback != null) {
                callback.reportError(e);
            }

            // only log it if we're not shutdown (otherwise, errors that happen during a shutdown can likely be ignored)
            if (!shutdown) {
                LOG.info(String.format("%s when requesting %s", errorMessage, url));
            }
        } finally {
            // the request is done, get it out of here
            synchronized (activeRequests) {
                activeRequests.remove(activeRequest);
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // this is OK to ignore
                }
            }
        }

        // record the response as ended
        RequestInfo.get().finish();

        // set the start time and other timings
        entry.setStartedDateTime(RequestInfo.get().getStart());
        entry.setTimings(RequestInfo.get().getTimings());
        entry.setServerIPAddress(RequestInfo.get().getResolvedAddress());
        entry.setTime(RequestInfo.get().getTotalTime());

        // todo: where you store this in HAR?
        // obj.setErrorMessage(errorMessage);
        entry.getResponse().setBodySize(bytes);
        entry.getResponse().getContent().setSize(bytes);
        entry.getResponse().setStatus(statusCode);
        if (statusLine != null) {
            entry.getResponse().setStatusText(statusLine.getReasonPhrase());
        }

        boolean urlEncoded = false;
        if (captureHeaders || captureContent) {
            for (Header header : method.getAllHeaders()) {
                if (header.getValue() != null && header.getValue().startsWith(URLEncodedUtils.CONTENT_TYPE)) {
                    urlEncoded = true;
                }

                entry.getRequest().getHeaders().add(new HarNameValuePair(header.getName(), header.getValue()));
            }

            if (response != null) {
                for (Header header : response.getAllHeaders()) {
                    entry.getResponse().getHeaders().add(new HarNameValuePair(header.getName(), header.getValue()));
                }
            }
        }

        if (captureContent) {
            // can we understand the POST data at all?
            if (method instanceof HttpEntityEnclosingRequestBase && req.getCopy() != null) {
                HttpEntityEnclosingRequestBase enclosingReq = (HttpEntityEnclosingRequestBase) method;
                HttpEntity entity = enclosingReq.getEntity();

                HarPostData data = new HarPostData();
                data.setMimeType(req.getMethod().getFirstHeader("Content-Type").getValue());
                entry.getRequest().setPostData(data);

                if (urlEncoded || URLEncodedUtils.isEncoded(entity)) {
                    try {
                        final String content = new String(req.getCopy().toByteArray(), "UTF-8");
                        if (content != null && content.length() > 0) {
                            List<NameValuePair> result = new ArrayList<NameValuePair>();
                            URLEncodedUtils.parse(result, new Scanner(content), null);

                            ArrayList<HarPostDataParam> params = new ArrayList<HarPostDataParam>();
                            data.setParams(params);

                            for (NameValuePair pair : result) {
                                params.add(new HarPostDataParam(pair.getName(), pair.getValue()));
                            }
                        }
                    } catch (Exception e) {
                        LOG.info("Unexpected problem when parsing input copy", e);
                    }
                } else {
                    // not URL encoded, so let's grab the body of the POST and capture that
                    data.setText(new String(req.getCopy().toByteArray()));
                }
            }
        }

        //capture request cookies
        javax.servlet.http.Cookie[] cookies = req.getProxyRequest().getCookies();
        for (javax.servlet.http.Cookie cookie : cookies) {
            HarCookie hc = new HarCookie();
            hc.setName(cookie.getName());
            hc.setValue(cookie.getValue());
            entry.getRequest().getCookies().add(hc);
        }

        String contentType = null;

        if (response != null) {
            try {
                Header contentTypeHdr = response.getFirstHeader("Content-Type");
                if (contentTypeHdr != null) {
                    contentType = contentTypeHdr.getValue();
                    entry.getResponse().getContent().setMimeType(contentType);

                    ByteArrayOutputStream copy = null;
                    boolean enableWorkWithCopy = false;
                    if (!isResponseVolatile && captureContent && os != null && os instanceof ClonedOutputStream) {
                        copy = ((ClonedOutputStream) os).getOutput();
                        enableWorkWithCopy = true;
                    }
                    if (isResponseVolatile && captureContent && bos != null) {
                        enableWorkWithCopy = true;
                        copy = bos;
                    }
                    if (captureContent && enableWorkWithCopy) {
                        if (entry.getResponse().getBodySize() != 0 && (gzipping || deflating)) {
                            // ok, we need to decompress it before we can put it in the har file
                            try {
                                InputStream temp = null;
                                if (gzipping) {
                                    temp = new GZIPInputStream(new ByteArrayInputStream(copy.toByteArray()));
                                }
                                else if (deflating) {
                                    //RAW deflate only
                                    // WARN : if system is using zlib<=1.1.4 the stream must be append with a dummy byte
                                    // that is not required for zlib>1.1.4 (not mentioned on current Inflater javadoc)
                                    temp = new InflaterInputStream(new ByteArrayInputStream(copy.toByteArray()), new Inflater(true));
                                }
                                copy = new ByteArrayOutputStream();
                                IOUtils.copy(temp, copy);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if (contentType != null && (contentType.startsWith("text/") || contentType.startsWith("application/x-javascript"))
                                || contentType.startsWith("application/javascript") || contentType.startsWith("application/json")
                                || contentType.startsWith("application/xml") || contentType.startsWith("application/xhtml+xml")
                                || contentType.startsWith("application/soap+xml")) {
                            entry.getResponse().getContent().setText(new String(copy.toByteArray()));
                        } else if (captureBinaryContent) {
                            entry.getResponse().getContent().setText(Base64.byteArrayToBase64(copy.toByteArray()));
                        }
                    }

                    NameValuePair nvp = contentTypeHdr.getElements()[0].getParameterByName("charset");

                    if (nvp != null) {
                        charSet = nvp.getValue();
                    }
                }

                if (os instanceof ByteArrayOutputStream) {
                    responseBody = ((ByteArrayOutputStream) os).toString(charSet);

                    if (verificationText != null) {
                        contentMatched = responseBody.contains(verificationText);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if (contentType != null) {
            entry.getResponse().getContent().setMimeType(contentType);
        }

        // checking to see if the client is being redirected
        boolean isRedirect = false;

        String location = null;
        if (response != null && statusCode >= 300 && statusCode < 400 && statusCode != 304) {
            isRedirect = true;

            // pulling the header for the redirect
            Header locationHeader = response.getLastHeader("location");
            if (locationHeader != null) {
                location = locationHeader.getValue();
            } else if (followRedirects) {
                throw new RuntimeException("Invalid redirect - missing location header");
            }
        }

        //
        // Response validation - they only work if we're not following redirects
        //

        int expectedStatusCode = req.getExpectedStatusCode();

        // if we didn't mock out the actual response code and the expected code isn't what we saw, we have a problem
        if (mockResponseCode == -1 && expectedStatusCode > -1) {
            if (followRedirects) {
                throw new RuntimeException("Response validation cannot be used while following redirects");
            }
            if (expectedStatusCode != statusCode) {
                if (isRedirect) {
                    throw new RuntimeException("Expected status code of " + expectedStatusCode + " but saw " + statusCode + " redirecting to: "
                            + location);
                } else {
                    throw new RuntimeException("Expected status code of " + expectedStatusCode + " but saw " + statusCode);
                }
            }
        }

        // Location header check:
        if (isRedirect && (req.getExpectedLocation() != null)) {
            if (followRedirects) {
                throw new RuntimeException("Response validation cannot be used while following redirects");
            }

            if (location.compareTo(req.getExpectedLocation()) != 0) {
                throw new RuntimeException("Expected a redirect to  " + req.getExpectedLocation() + " but saw " + location);
            }
        }

        // end of validation logic

        // basic tail recursion for redirect handling
        if (isRedirect && followRedirects) {
            // updating location:
            try {
                URI redirectUri = new URI(location);
                URI newUri = method.getURI().resolve(redirectUri);
                method.setURI(newUri);

                return execute(req, ++depth, isResponseVolatile);
            } catch (URISyntaxException e) {
                LOG.warn("Could not parse URL", e);
            }
        }

        return new BrowserMobHttpResponse(statusCode, entry, method, req.getProxyRequest().getURI(), response, contentMatched, verificationText, errorMessage,
                entry.getResponse().getContent().getText(), contentType, charSet, bos, os, isResponseVolatile);
    }

    private long copyWithStatsDynamic(InputStream is, OutputStream os) throws IOException {
        long bytesCopied = 0;
        byte[] buffer = new byte[BUFFER];
        int length;

        try {
            // read the first byte
            int firstByte = is.read();

            if (firstByte == -1) {
                return 0;
            }

            os.write(firstByte);
            bytesCopied++;

            do {
                length = is.read(buffer, 0, BUFFER);
                if (length != -1) {
                    bytesCopied += length;
                    os.write(buffer, 0, length);
                    os.flush();
                }
            } while (length != -1);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // ok to ignore
            }

            try {
                os.close();
            } catch (IOException e) {
                // ok to ignore
            }
        }

        return bytesCopied;
    }

    public void shutdown() {
        shutdown = true;
        abortActiveRequests();
        rewriteRules.clear();
        credsProvider.clear();
        httpClientConnMgr.shutdown();
        HttpClientInterrupter.release(this);
    }

    public void abortActiveRequests() {
        allowNewRequests.set(true);

        synchronized (activeRequests) {
            for (ActiveRequest activeRequest : activeRequests) {
                activeRequest.abort();
            }
            activeRequests.clear();
        }
    }

    public void setHar(final Har har) {
        this.har = har;
    }

    public void setHarPageRef(final String harPageRef) {
        this.harPageRef = harPageRef;
    }

    public void setRequestTimeout(final int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setSocketOperationTimeout(final int readTimeout) {
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout);
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
    }

    public void setFollowRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;

    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void autoBasicAuthorization(final String domain, final String username, final String password) {
        authType = AuthType.BASIC;
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(domain, -1), new UsernamePasswordCredentials(username, password));
    }

    public void autoNTLMAuthorization(final String domain, final String username, final String password) {
        authType = AuthType.NTLM;
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(domain, -1), new NTCredentials(username, password, "workstation", domain));
    }

    public void rewriteUrl(final String match, final String replace) {
        rewriteRules.add(new RewriteRule(match, replace));
    }

    // this method is provided for backwards compatibility before we renamed it to
    // blacklistRequests (note the plural)
    public void blacklistRequest(final String pattern, final int responseCode) {
        blacklistRequests(pattern, responseCode);
    }

    public void blacklistRequests(final String pattern, final int responseCode) {
        if (blacklistEntries == null) {
            blacklistEntries = new CopyOnWriteArrayList<BlacklistEntry>();
        }

        blacklistEntries.add(new BlacklistEntry(pattern, responseCode));
    }

    public void whitelistRequests(final String[] patterns, final int responseCode) {
        whitelistEntry = new WhitelistEntry(patterns, responseCode);
    }

    public void addHeader(final String name, final String value) {
        additionalHeaders.put(name, value);
    }

    public void prepareForBrowser() {
        // Clear cookies, let the browser handle them
        httpClient.setCookieStore(new BlankCookieStore());
        httpClient.getCookieSpecs().register("easy", new CookieSpecFactory() {
            @Override
            public CookieSpec newInstance(final HttpParams params) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
                        // easy!
                    }
                };
            }
        });
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
        decompress = false;
        setFollowRedirects(false);
    }

    public String remappedHost(final String host) {
        return hostNameResolver.remapping(host);
    }

    public List<String> originalHosts(final String host) {
        return hostNameResolver.original(host);
    }

    public Har getHar() {
        return har;
    }

    public void setCaptureHeaders(final boolean captureHeaders) {
        this.captureHeaders = captureHeaders;
    }

    public void setCaptureContent(final boolean captureContent) {
        this.captureContent = captureContent;
    }

    public void setCaptureBinaryContent(final boolean captureBinaryContent) {
        this.captureBinaryContent = captureBinaryContent;
    }

    public void setHttpProxy(final String httpProxy) {
        String host = httpProxy.split(":")[0];
        Integer port = Integer.parseInt(httpProxy.split(":")[1]);
        HttpHost proxy = new HttpHost(host, port);
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    static class PreemptiveAuth implements HttpRequestInterceptor {
        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {

            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme available yet, try to initialize it preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                    if (creds != null) {
                        authState.setAuthScheme(authScheme);
                        authState.setCredentials(creds);
                    }
                }
            }
        }
    }

    class ActiveRequest {
        HttpRequestBase request;
        BasicHttpContext ctx;
        Date start;

        ActiveRequest(final HttpRequestBase request, final BasicHttpContext ctx, final Date start) {
            this.request = request;
            this.ctx = ctx;
            this.start = start;
        }

        void checkTimeout() {
            if (requestTimeout != -1) {
                if (request != null && start != null && new Date(System.currentTimeMillis() - requestTimeout).after(start)) {
                    LOG.info("Aborting request to %s after it failed to complete in %d ms", request.getURI().toString(), requestTimeout);

                    abort();
                }
            }
        }

        public void abort() {
            request.abort();

            // try to close the connection? is this necessary? unclear based on preliminary debugging of HttpClient, but
            // it doesn't seem to hurt to try
            HttpConnection conn = (HttpConnection) ctx.getAttribute("http.connection");
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    // this is fine, we're shutting it down anyway
                }
            }
        }
    }

    private class WhitelistEntry {
        private final List<Pattern> patterns = new CopyOnWriteArrayList<Pattern>();
        private final int responseCode;

        private WhitelistEntry(final String[] patterns, final int responseCode) {
            for (String pattern : patterns) {
                this.patterns.add(Pattern.compile(pattern));
            }
            this.responseCode = responseCode;
        }
    }

    private class BlacklistEntry {
        private final Pattern pattern;
        private final int responseCode;

        private BlacklistEntry(final String pattern, final int responseCode) {
            this.pattern = Pattern.compile(pattern);
            this.responseCode = responseCode;
        }
    }

    private class RewriteRule {
        private final Pattern match;
        private final String replace;

        private RewriteRule(final String match, final String replace) {
            this.match = Pattern.compile(match);
            this.replace = replace;
        }
    }

    private enum AuthType {
        NONE,
        BASIC,
        NTLM
    }

    public void clearDNSCache() {
        hostNameResolver.clearCache();
    }

    public void setDNSCacheTimeout(final int timeout) {
        hostNameResolver.setCacheTimeout(timeout);
    }

}
