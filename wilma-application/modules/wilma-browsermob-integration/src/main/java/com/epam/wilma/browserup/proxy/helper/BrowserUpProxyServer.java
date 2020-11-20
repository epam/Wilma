package com.epam.wilma.browserup.proxy.helper;
/*==========================================================================
Copyright since 2020, EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.assertion.HarEntryAssertion;
import com.browserup.bup.assertion.ResponseTimeLessThanOrEqualAssertion;
import com.browserup.bup.assertion.error.HarEntryAssertionError;
import com.browserup.bup.assertion.field.content.ContentContainsStringAssertion;
import com.browserup.bup.assertion.field.content.ContentDoesNotContainStringAssertion;
import com.browserup.bup.assertion.field.content.ContentMatchesAssertion;
import com.browserup.bup.assertion.field.content.ContentSizeLessThanOrEqualAssertion;
import com.browserup.bup.assertion.field.header.FilteredHeadersContainStringAssertion;
import com.browserup.bup.assertion.field.header.FilteredHeadersDoNotContainStringAssertion;
import com.browserup.bup.assertion.field.header.FilteredHeadersMatchAssertion;
import com.browserup.bup.assertion.field.header.HeadersContainStringAssertion;
import com.browserup.bup.assertion.field.header.HeadersDoNotContainStringAssertion;
import com.browserup.bup.assertion.field.header.HeadersMatchAssertion;
import com.browserup.bup.assertion.field.status.StatusBelongsToClassAssertion;
import com.browserup.bup.assertion.field.status.StatusEqualsAssertion;
import com.browserup.bup.assertion.model.AssertionEntryResult;
import com.browserup.bup.assertion.model.AssertionResult;
import com.browserup.bup.assertion.supplier.CurrentStepHarEntriesSupplier;
import com.browserup.bup.assertion.supplier.HarEntriesSupplier;
import com.browserup.bup.assertion.supplier.MostRecentHarEntrySupplier;
import com.browserup.bup.assertion.supplier.MostRecentUrlFilteredHarEntrySupplier;
import com.browserup.bup.assertion.supplier.UrlFilteredHarEntriesSupplier;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.filters.AddHeadersFilter;
import com.browserup.bup.filters.AutoBasicAuthFilter;
import com.browserup.bup.filters.BlacklistFilter;
import com.browserup.bup.filters.HttpsHostCaptureFilter;
import com.browserup.bup.filters.HttpsOriginalHostCaptureFilter;
import com.browserup.bup.filters.LatencyFilter;
import com.browserup.bup.filters.RegisterRequestFilter;
import com.browserup.bup.filters.RequestFilter;
import com.browserup.bup.filters.RequestFilterAdapter;
import com.browserup.bup.filters.ResolvedHostnameCacheFilter;
import com.browserup.bup.filters.ResponseFilter;
import com.browserup.bup.filters.ResponseFilterAdapter;
import com.browserup.bup.filters.RewriteUrlFilter;
import com.browserup.bup.filters.UnregisterRequestFilter;
import com.browserup.bup.filters.WhitelistFilter;
import com.browserup.bup.mitm.KeyStoreFileCertificateSource;
import com.browserup.bup.mitm.TrustSource;
import com.browserup.bup.mitm.keys.ECKeyGenerator;
import com.browserup.bup.mitm.keys.RSAKeyGenerator;
import com.browserup.bup.mitm.manager.ImpersonatingMitmManager;
import com.browserup.bup.proxy.ActivityMonitor;
import com.browserup.bup.proxy.BlacklistEntry;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.bup.proxy.RewriteRule;
import com.browserup.bup.proxy.Whitelist;
import com.browserup.bup.proxy.auth.AuthType;
import com.browserup.bup.proxy.dns.AdvancedHostResolver;
import com.browserup.bup.proxy.dns.DelegatingHostResolver;
import com.browserup.bup.util.BrowserUpHttpUtil;
import com.browserup.bup.util.HttpStatusClass;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.epam.wilma.browsermob.domain.exception.ProxyCannotBeStartedException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapMaker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.ChainedProxyAdapter;
import org.littleshoot.proxy.ChainedProxyManager;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.extras.SelfSignedSslEngineSource;
import org.littleshoot.proxy.impl.ClientDetails;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.littleshoot.proxy.impl.ThreadPoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toCollection;

public class BrowserUpProxyServer implements BrowserUpProxy {
    /**
     * The default pseudonym to use when adding the Via header to proxied requests.
     */
    public static final String VIA_HEADER_ALIAS = "BrowserUpProxyWilma";
    private static final Logger log = LoggerFactory.getLogger(BrowserUpProxyServer.class);
    /* Default MITM resources */
    private static final String RSA_KEYSTORE_RESOURCE = "/sslSupport/ca-keystore-rsa.p12";
    private static final String EC_KEYSTORE_RESOURCE = "/sslSupport/ca-keystore-ec.p12";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEYSTORE_PRIVATE_KEY_ALIAS = "key";
    private static final String KEYSTORE_PASSWORD = "password";
    /**
     * True only after the proxy has been successfully started.
     */
    private final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * True only after the proxy has been successfully started, then successfully stopped or aborted.
     */
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    /**
     * The list of filterFactories that will generate the filters that implement BrowserUp-proxy behavior.
     */
    private final List<HttpFiltersSource> filterFactories = new CopyOnWriteArrayList<>();
    /**
     * List of accepted URL patterns. Unlisted URL patterns will be rejected with the response code contained in the Whitelist.
     */
    private final AtomicReference<Whitelist> whitelist = new AtomicReference<>(Whitelist.WHITELIST_DISABLED);
    /**
     * Set to true when LittleProxy has been bootstrapped with the default chained proxy. This allows modifying the chained proxy
     * after the proxy has been started.
     */
    private final AtomicBoolean bootstrappedWithDefaultChainedProxy = new AtomicBoolean(false);
    /**
     * Resolver to use when resolving hostnames to IP addresses. This is a bridge between {@link org.littleshoot.proxy.HostResolver} and
     * {@link com.browserup.bup.proxy.dns.AdvancedHostResolver}. It allows the resolvers to be changed on-the-fly without re-bootstrapping the
     * littleproxy server. The default resolver (native JDK resolver) can be changed using {@link #setHostNameResolver(com.browserup.bup.proxy.dns.AdvancedHostResolver)} and
     * supplying one of the pre-defined resolvers in {@link ClientUtil}, such as {@link ClientUtil#createDnsJavaWithNativeFallbackResolver()}
     * or {@link ClientUtil#createDnsJavaResolver()}. You can also build your own resolver, or use {@link com.browserup.bup.proxy.dns.ChainedHostResolver}
     * to chain together multiple DNS resolvers.
     */
    private final DelegatingHostResolver delegatingResolver = new DelegatingHostResolver(ClientUtil.createNativeCacheManipulatingResolver());
    private final ActivityMonitor activityMonitor = new ActivityMonitor();
    /**
     * A mapping of hostnames to base64-encoded Basic auth credentials that will be added to the Authorization header for
     * matching requests.
     */
    private final ConcurrentMap<String, String> basicAuthCredentials = new MapMaker()
            .concurrencyLevel(1)
            .makeMap();
    /**
     * When true, MITM will be disabled. The proxy will no longer intercept HTTPS requests, but they will still be proxied.
     */
    private volatile boolean mitmDisabled = false;
    /**
     * The MITM manager that will be used for HTTPS requests.
     */
    private volatile MitmManager mitmManager;
    /**
     * List of rejected URL patterns
     */
    private volatile Collection<BlacklistEntry> blacklistEntries = new CopyOnWriteArrayList<>();
    /**
     * List of URLs to rewrite
     */
    private volatile CopyOnWriteArrayList<RewriteRule> rewriteRules = new CopyOnWriteArrayList<>();
    /**
     * The LittleProxy instance that performs all proxy operations.
     */
    private volatile HttpProxyServer proxyServer;
    /**
     * Maximum bandwidth to consume when reading responses from servers.
     */
    private volatile long readBandwidthLimitBps;
    /**
     * Maximum bandwidth to consume when writing requests to servers.
     */
    private volatile long writeBandwidthLimitBps;
    /**
     * Additional headers that will be sent with every request. The map is declared as a ConcurrentMap to indicate that writes may be performed
     * by other threads concurrently (e.g. due to an incoming REST call), but the concurrencyLevel is set to 1 because modifications to the
     * additionalHeaders are rare, and in most cases happen only once, at start-up.
     */
    private volatile ConcurrentMap<String, String> additionalHeaders = new MapMaker().concurrencyLevel(1).makeMap();
    /**
     * The amount of time to wait while connecting to a server.
     */
    private volatile int connectTimeoutMs;
    /**
     * The amount of time a connection to a server can remain idle while receiving data from the server.
     */
    private volatile int idleConnectionTimeoutSec;
    /**
     * The amount of time to wait before forwarding the response to the client.
     */
    private volatile int latencyMs;
    /**
     * The address of an upstream chained proxy to route traffic through.
     */
    private volatile InetSocketAddress upstreamProxyAddress;
    /**
     * Whether to connect to that upstream chained proxy using https, rather than http
     */
    private volatile boolean upstreamProxyHTTPS;
    /**
     * The hosts and addresses that should not be routed through the upstream proxy
     */
    private List<String> upstreamProxyNonProxyHosts;
    /**
     * The chained proxy manager that manages upstream proxies.
     */
    private volatile ChainedProxyManager chainedProxyManager;
    /**
     * The address of the network interface from which the proxy will initiate connections.
     */
    private volatile InetAddress serverBindAddress;
    /**
     * The TrustSource that will be used to validate servers' certificates. If null, will not validate server certificates.
     */
    private volatile TrustSource trustSource = TrustSource.defaultTrustSource();
    /**
     * When true, use Elliptic Curve keys and certificates when impersonating upstream servers.
     */
    private volatile boolean useEcc = false;
    /**
     * The acceptor and worker thread configuration for the Netty thread pools.
     */
    private volatile ThreadPoolConfiguration threadPoolConfiguration;
    /**
     * Base64-encoded credentials to use to authenticate with the upstream proxy.
     */
    private volatile String chainedProxyCredentials;

    public BrowserUpProxyServer() {
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }

    @Override
    public void start(int port) {
        throw new ProxyCannotBeStartedException("BrowserUp Wilma proxy shall not be started this way.");
    }

    @Override
    public void start(int port, InetAddress bindAddress) {
        throw new ProxyCannotBeStartedException("BrowserUp Wilma proxy shall not be started this way.");
    }

    @Override
    public void start(int port, InetAddress clientBindAddress, InetAddress serverBindAddress) {
        throw new ProxyCannotBeStartedException("BrowserUp Wilma proxy shall not be started this way.");
    }

    public void start(int port, int timeout) {
        //set timeouts
        setIdleConnectionTimeout(timeout, TimeUnit.MILLISECONDS);
        setConnectTimeout(timeout, TimeUnit.MILLISECONDS);

        boolean notStarted = started.compareAndSet(false, true);
        if (!notStarted) {
            throw new IllegalStateException("Proxy server is already started. Not restarting.");
        }


        InetSocketAddress clientBindSocket;
        clientBindSocket = new InetSocketAddress(port);

        this.serverBindAddress = null;

        // initialize all the default BrowserUp filter factories that provide core BUP functionality
        addBrowserUpFilters();

        HttpProxyServerBootstrap bootstrap = DefaultHttpProxyServer.bootstrap()
                .withFiltersSource(new HttpFiltersSource() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext channelHandlerContext) {
                        return new BrowserUpWilmaHttpFilterChain(BrowserUpProxyServer.this, originalRequest, channelHandlerContext);
                    }

                    @Override
                    public int getMaximumRequestBufferSizeInBytes() {
                        return getMaximumRequestBufferSize();
                    }

                    @Override
                    public int getMaximumResponseBufferSizeInBytes() {
                        return getMaximumResponseBufferSize();
                    }
                })
                .withServerResolver(delegatingResolver)
                .withAddress(clientBindSocket)
                .withConnectTimeout(connectTimeoutMs)
                .withIdleConnectionTimeout(idleConnectionTimeoutSec)
                .withProxyAlias(VIA_HEADER_ALIAS);

        if (serverBindAddress != null) {
            bootstrap.withNetworkInterface(new InetSocketAddress(serverBindAddress, 0));
        }

        if (!mitmDisabled) {
            if (mitmManager == null) {
                mitmManager = ImpersonatingMitmManager.builder()
                        .rootCertificateSource(new KeyStoreFileCertificateSource(
                                KEYSTORE_TYPE,
                                useEcc ? EC_KEYSTORE_RESOURCE : RSA_KEYSTORE_RESOURCE,
                                KEYSTORE_PRIVATE_KEY_ALIAS,
                                KEYSTORE_PASSWORD))
                        .serverKeyGenerator(useEcc ? new ECKeyGenerator() : new RSAKeyGenerator())
                        .trustSource(trustSource)
                        .build();
            }

            bootstrap.withManInTheMiddle(mitmManager);
        }

        if (readBandwidthLimitBps > 0 || writeBandwidthLimitBps > 0) {
            bootstrap.withThrottling(readBandwidthLimitBps, writeBandwidthLimitBps);
        }

        if (chainedProxyManager != null) {
            bootstrap.withChainProxyManager(chainedProxyManager);
        } else if (upstreamProxyAddress != null) {
            // indicate that the proxy was bootstrapped with the default chained proxy manager, which allows changing the
            // chained proxy after the proxy is started.
            bootstrappedWithDefaultChainedProxy.set(true);

            bootstrap.withChainProxyManager(new ChainedProxyManager() {
                @Override
                public void lookupChainedProxies(HttpRequest httpRequest, Queue<ChainedProxy> chainedProxies, ClientDetails clientDetails) {
                    final InetSocketAddress upstreamProxy = upstreamProxyAddress;

                    if (upstreamProxy != null) {

                        final boolean useEncryption = upstreamProxyHTTPS;
                        final List<String> nonProxyHosts = upstreamProxyNonProxyHosts;

                        // skip upstream proxy configuration because the host is defined as proxy exception / non proxy hosts
                        // therefore we need to cast it to an URL
                        URL url = null;
                        try {
                            url = new URL(httpRequest.uri());
                        } catch (MalformedURLException e) {
                            log.error("The requested URL is not valid.", e);
                        }

                        final URL finalUrl = url; // need for lambda expression.
                        // note that we have to transform the wildcard like *.example.com to a valid regex
                        if (nonProxyHosts != null && finalUrl != null && nonProxyHosts.stream().anyMatch(nph -> finalUrl.getHost().matches(nph.trim().replace("*", ".*?")))) {
                            chainedProxies.add(ChainedProxyAdapter.FALLBACK_TO_DIRECT_CONNECTION);
                        } else {
                            chainedProxies.add(new ChainedProxyAdapter() {
                                @Override
                                public InetSocketAddress getChainedProxyAddress() {
                                    return upstreamProxy;
                                }

                                @Override
                                public void filterRequest(HttpObject httpObject) {
                                    String chainedProxyAuth = chainedProxyCredentials;
                                    if (chainedProxyAuth != null) {
                                        if (httpObject instanceof HttpRequest) {
                                            if (ProxyUtils.isCONNECT(httpObject) || !((HttpRequest) httpObject).uri().startsWith("/")) {
                                                HttpHeaders.addHeader((HttpRequest) httpObject, HttpHeaderNames.PROXY_AUTHORIZATION, "Basic " + chainedProxyAuth);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public boolean requiresEncryption() {
                                    return useEncryption;
                                }

                                @Override
                                public SSLEngine newSslEngine() {
                                    if (useEncryption) {
                                        return new SelfSignedSslEngineSource(
                                                true, false).newSslEngine();
                                    } else {
                                        return null;
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        if (threadPoolConfiguration != null) {
            bootstrap.withThreadPoolConfiguration(threadPoolConfiguration);
        }

        proxyServer = bootstrap.start();
    }

    @Override
    public void start() {
        throw new ProxyCannotBeStartedException("BrowserUp proxy shall not be started this way.");
    }

    @Override
    public void stop() {
        stop(true);
    }

    @Override
    public void abort() {
        stop(false);
    }

    protected void stop(boolean graceful) {
        if (isStarted()) {
            if (stopped.compareAndSet(false, true)) {
                if (proxyServer != null) {
                    if (graceful) {
                        proxyServer.stop();
                    } else {
                        proxyServer.abort();
                    }
                } else {
                    log.warn("Attempted to stop proxy server, but proxy was never successfully started.");
                }
            } else {
                throw new IllegalStateException("Proxy server is already stopped. Cannot re-stop.");
            }
        } else {
            throw new IllegalStateException("Proxy server has not been started");
        }
    }

    @Override
    public InetAddress getClientBindAddress() {
        if (started.get()) {
            return proxyServer.getListenAddress().getAddress();
        } else {
            return null;
        }
    }

    @Override
    public int getPort() {
        if (started.get()) {
            return proxyServer.getListenAddress().getPort();
        } else {
            return 0;
        }
    }

    @Override
    public InetAddress getServerBindAddress() {
        return serverBindAddress;
    }

    @Override
    public Har getHar() {
        return null;
    }

    @Override
    public Har getHar(boolean cleanHar) {
        return null;
    }

    @Override
    public Har newHar() {
        return newHar(null);
    }

    @Override
    public Har newHar(String initialPageRef) {
        return newHar(initialPageRef, null);
    }

    @Override
    public Har newHar(String initialPageRef, String initialPageTitle) {
        return null;
    }

    @Override
    public EnumSet<CaptureType> getHarCaptureTypes() {
        return null;
    }

    @Override
    public void setHarCaptureTypes(Set<CaptureType> harCaptureSettings) {
    }

    @Override
    public void setHarCaptureTypes(CaptureType... captureTypes) {
    }

    @Override
    public void enableHarCaptureTypes(Set<CaptureType> captureTypes) {
    }

    @Override
    public void enableHarCaptureTypes(CaptureType... captureTypes) {
    }

    @Override
    public void disableHarCaptureTypes(Set<CaptureType> captureTypes) {
    }

    @Override
    public void disableHarCaptureTypes(CaptureType... captureTypes) {
    }

    @Override
    public Har newPage() {
        return null;
    }

    @Override
    public Har newPage(String pageRef) {
        return null;
    }

    @Override
    public Har newPage(String pageRef, String pageTitle) {
        return null;
    }

    @Override
    public Har endHar() {
        return null;
    }

    @Override
    public long getReadBandwidthLimit() {
        return readBandwidthLimitBps;
    }

    @Override
    public void setReadBandwidthLimit(long bytesPerSecond) {
        this.readBandwidthLimitBps = bytesPerSecond;

        if (isStarted()) {
            proxyServer.setThrottle(this.readBandwidthLimitBps, this.writeBandwidthLimitBps);
        }
    }

    @Override
    public long getWriteBandwidthLimit() {
        return writeBandwidthLimitBps;
    }

    @Override
    public void setWriteBandwidthLimit(long bytesPerSecond) {
        this.writeBandwidthLimitBps = bytesPerSecond;

        if (isStarted()) {
            proxyServer.setThrottle(this.readBandwidthLimitBps, this.writeBandwidthLimitBps);
        }
    }

    public void endPage() {
    }

    @Override
    public void addHeaders(Map<String, String> headers) {
        ConcurrentMap<String, String> newHeaders = new MapMaker().concurrencyLevel(1).makeMap();
        newHeaders.putAll(headers);

        this.additionalHeaders = newHeaders;
    }

    @Override
    public void setLatency(long latency, TimeUnit timeUnit) {
        this.latencyMs = (int) TimeUnit.MILLISECONDS.convert(latency, timeUnit);
    }

    @Override
    public void autoAuthorization(String domain, String username, String password, AuthType authType) {
        switch (authType) {
        case BASIC:
            // base64 encode the "username:password" string
            String base64EncodedCredentials = BrowserUpHttpUtil.base64EncodeBasicCredentials(username, password);

            basicAuthCredentials.put(domain, base64EncodedCredentials);
            break;

        default:
            throw new UnsupportedOperationException("AuthType " + authType + " is not supported for HTTP Authorization");
        }
    }

    @Override
    public void stopAutoAuthorization(String domain) {
        basicAuthCredentials.remove(domain);
    }

    @Override
    public void chainedProxyAuthorization(String username, String password, AuthType authType) {
        switch (authType) {
        case BASIC:
            chainedProxyCredentials = BrowserUpHttpUtil.base64EncodeBasicCredentials(username, password);
            break;

        default:
            throw new UnsupportedOperationException("AuthType " + authType + " is not supported for Proxy Authorization");
        }
    }

    @Override
    public void setConnectTimeout(int connectTimeout, TimeUnit timeUnit) {
        this.connectTimeoutMs = (int) TimeUnit.MILLISECONDS.convert(connectTimeout, timeUnit);

        if (isStarted()) {
            proxyServer.setConnectTimeout((int) TimeUnit.MILLISECONDS.convert(connectTimeout, timeUnit));
        }
    }

    /**
     * The LittleProxy implementation only allows idle connection timeouts to be specified in seconds. idleConnectionTimeouts greater than
     * 0 but less than 1 second will be set to 1 second; otherwise, values will be truncated (i.e. 1500ms will become 1s).
     */
    @Override
    public void setIdleConnectionTimeout(int idleConnectionTimeout, TimeUnit timeUnit) {
        long timeout = TimeUnit.SECONDS.convert(idleConnectionTimeout, timeUnit);
        if (timeout == 0 && idleConnectionTimeout > 0) {
            this.idleConnectionTimeoutSec = 1;
        } else {
            this.idleConnectionTimeoutSec = (int) timeout;
        }

        if (isStarted()) {
            proxyServer.setIdleConnectionTimeout(idleConnectionTimeoutSec);
        }
    }

    @Override
    public void setRequestTimeout(int requestTimeout, TimeUnit timeUnit) {
        //TODO: implement Request Timeouts using LittleProxy. currently this only sets an idle connection timeout, if the idle connection
        // timeout is higher than the specified requestTimeout.
        if (idleConnectionTimeoutSec == 0 || idleConnectionTimeoutSec > TimeUnit.SECONDS.convert(requestTimeout, timeUnit)) {
            setIdleConnectionTimeout(requestTimeout, timeUnit);
        }
    }

    @Override
    public void rewriteUrl(String pattern, String replace) {
        rewriteRules.add(new RewriteRule(pattern, replace));
    }

    @Override
    public void rewriteUrls(Map<String, String> rewriteRules) {
        this.rewriteRules = rewriteRules.entrySet().stream()
                .map(rewriteRule -> new RewriteRule(rewriteRule.getKey(), rewriteRule.getValue()))
                .collect(toCollection(CopyOnWriteArrayList::new));
    }

    @Override
    public void clearRewriteRules() {
        rewriteRules.clear();
    }

    @Override
    public void blacklistRequests(String pattern, int responseCode) {
        blacklistEntries.add(new BlacklistEntry(pattern, responseCode));
    }

    @Override
    public void blacklistRequests(String pattern, int responseCode, String method) {
        blacklistEntries.add(new BlacklistEntry(pattern, responseCode, method));
    }

    @Override
    public Collection<BlacklistEntry> getBlacklist() {
        return Collections.unmodifiableCollection(blacklistEntries);
    }

    @Override
    public void setBlacklist(Collection<BlacklistEntry> blacklist) {
        this.blacklistEntries = new CopyOnWriteArrayList<>(blacklist);
    }

    @Override
    public boolean isWhitelistEnabled() {
        return whitelist.get().isEnabled();
    }

    @Override
    public Collection<String> getWhitelistUrls() {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        whitelist.get().getPatterns().stream()
                .map(Pattern::pattern)
                .forEach(builder::add);

        return builder.build();
    }

    @Override
    public int getWhitelistStatusCode() {
        return whitelist.get().getStatusCode();
    }

    @Override
    public void clearBlacklist() {
        blacklistEntries.clear();
    }

    @Override
    public void whitelistRequests(Collection<String> urlPatterns, int statusCode) {
        this.whitelist.set(new Whitelist(urlPatterns, statusCode));
    }

    @Override
    public void addWhitelistPattern(String urlPattern) {
        // to make sure this method is threadsafe, we need to guarantee that the "snapshot" of the whitelist taken at the beginning
        // of the method has not been replaced by the time we have constructed a new whitelist at the end of the method
        boolean whitelistUpdated = false;
        while (!whitelistUpdated) {
            Whitelist currentWhitelist = this.whitelist.get();
            if (!currentWhitelist.isEnabled()) {
                throw new IllegalStateException("Whitelist is disabled. Cannot add patterns to a disabled whitelist.");
            }

            // retrieve the response code and list of patterns from the current whitelist, the construct a new list of patterns that contains
            // all of the old whitelist's patterns + this new pattern
            int statusCode = currentWhitelist.getStatusCode();
            List<String> newPatterns = currentWhitelist.getPatterns().stream()
                    .map(Pattern::pattern)
                    .collect(toCollection(() -> new ArrayList<>(currentWhitelist.getPatterns().size() + 1)));
            newPatterns.add(urlPattern);

            // create a new (immutable) Whitelist object with the new pattern list and status code
            Whitelist newWhitelist = new Whitelist(newPatterns, statusCode);

            // replace the current whitelist with the new whitelist only if the current whitelist has not changed since we started
            whitelistUpdated = this.whitelist.compareAndSet(currentWhitelist, newWhitelist);
        }
    }

    /**
     * Whitelist the specified request patterns, returning the specified responseCode for non-whitelisted
     * requests.
     *
     * @param patterns     regular expression strings matching URL patterns to whitelist. if empty or null,
     *                     the whitelist will be enabled but will not match any URLs.
     * @param responseCode the HTTP response code to return for non-whitelisted requests
     */
    public void whitelistRequests(String[] patterns, int responseCode) {
        if (patterns == null || patterns.length == 0) {
            this.enableEmptyWhitelist(responseCode);
        } else {
            this.whitelistRequests(Arrays.asList(patterns), responseCode);
        }
    }

    @Override
    public void enableEmptyWhitelist(int statusCode) {
        whitelist.set(new Whitelist(statusCode));
    }

    @Override
    public void disableWhitelist() {
        whitelist.set(Whitelist.WHITELIST_DISABLED);
    }

    @Override
    public void addHeader(String name, String value) {
        additionalHeaders.put(name, value);
    }

    @Override
    public void removeHeader(String name) {
        additionalHeaders.remove(name);
    }

    @Override
    public void removeAllHeaders() {
        additionalHeaders.clear();
    }

    @Override
    public Map<String, String> getAllHeaders() {
        return ImmutableMap.copyOf(additionalHeaders);
    }

    @Override
    public AdvancedHostResolver getHostNameResolver() {
        return delegatingResolver.getResolver();
    }

    @Override
    public void setHostNameResolver(AdvancedHostResolver resolver) {
        delegatingResolver.setResolver(resolver);
    }

    @Override
    public boolean waitForQuiescence(long quietPeriod, long timeout, TimeUnit timeUnit) {
        return activityMonitor.waitForQuiescence(quietPeriod, timeout, timeUnit);
    }

    @Override
    public void setChainedProxyHTTPS(boolean chainedProxyHTTPS) {
        upstreamProxyHTTPS = chainedProxyHTTPS;
    }

    @Override
    public InetSocketAddress getChainedProxy() {
        return upstreamProxyAddress;
    }

    /**
     * Instructs this proxy to route traffic through an upstream proxy.
     *
     * <b>Note:</b> Using {@link #setChainedProxyManager(ChainedProxyManager)} will supersede any value set by this method. A chained
     * proxy must be set before the proxy is started, though it can be changed after the proxy is started.
     *
     * @param chainedProxyAddress address of the upstream proxy
     */
    @Override
    public void setChainedProxy(InetSocketAddress chainedProxyAddress) {
        if (isStarted() && !bootstrappedWithDefaultChainedProxy.get()) {
            throw new IllegalStateException("Cannot set a chained proxy after the proxy is started if the proxy was started without a chained proxy.");
        }

        upstreamProxyAddress = chainedProxyAddress;
    }

    /**
     * Allows access to the LittleProxy {@link ChainedProxyManager} for fine-grained control of the chained proxies. To enable a single
     * chained proxy, {@link BrowserUpProxy#setChainedProxy(InetSocketAddress)} is generally more convenient.
     *
     * <b>Note:</b> The chained proxy manager must be enabled before calling {@link #start()}.
     *
     * @param chainedProxyManager chained proxy manager to enable
     */
    public void setChainedProxyManager(ChainedProxyManager chainedProxyManager) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot configure chained proxy manager after proxy has started.");
        }

        this.chainedProxyManager = chainedProxyManager;
    }

    @Override
    public void setChainedProxyNonProxyHosts(List<String> upstreamNonProxyHosts) {
        this.upstreamProxyNonProxyHosts = upstreamNonProxyHosts;
    }

    /**
     * Configures the Netty thread pool used by the LittleProxy back-end. See {@link ThreadPoolConfiguration} for details.
     *
     * @param threadPoolConfiguration thread pool configuration to use
     */
    public void setThreadPoolConfiguration(ThreadPoolConfiguration threadPoolConfiguration) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot configure thread pool after proxy has started.");
        }

        this.threadPoolConfiguration = threadPoolConfiguration;
    }

    @Override
    public void addFirstHttpFilterFactory(HttpFiltersSource filterFactory) {
        filterFactories.add(0, filterFactory);
    }

    @Override
    public void addLastHttpFilterFactory(HttpFiltersSource filterFactory) {
        filterFactories.add(filterFactory);
    }

    /**
     * <b>Note:</b> The current implementation of this method forces a maximum response size of 2 MiB. To adjust the maximum response size, or
     * to disable aggregation (which disallows access to the {@link com.browserup.bup.util.HttpMessageContents}), you may add the filter source
     * directly: <code>addFirstHttpFilterFactory(new ResponseFilterAdapter.FilterSource(filter, bufferSizeInBytes));</code>
     */
    @Override
    public void addResponseFilter(ResponseFilter filter) {
        addLastHttpFilterFactory(new ResponseFilterAdapter.FilterSource(filter));
    }

    /**
     * <b>Note:</b> The current implementation of this method forces a maximum request size of 2 MiB. To adjust the maximum request size, or
     * to disable aggregation (which disallows access to the {@link com.browserup.bup.util.HttpMessageContents}), you may add the filter source
     * directly: <code>addFirstHttpFilterFactory(new RequestFilterAdapter.FilterSource(filter, bufferSizeInBytes));</code>
     */
    @Override
    public void addRequestFilter(RequestFilter filter) {
        addFirstHttpFilterFactory(new RequestFilterAdapter.FilterSource(filter));
    }

    @Override
    public Map<String, String> getRewriteRules() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        rewriteRules.forEach(rewriteRule -> builder.put(rewriteRule.getPattern().pattern(), rewriteRule.getReplace()));

        return builder.build();
    }

    @Override
    public void removeRewriteRule(String urlPattern) {
        // normally removing elements from the list we are iterating over would not be possible, but since this is a CopyOnWriteArrayList
        // the iterator it returns is a "snapshot" of the list that will not be affected by removal (and that does not support removal, either)
        rewriteRules.stream()
                .filter(rewriteRule -> rewriteRule.getPattern().pattern().equals(urlPattern))
                .forEach(rewriteRule -> rewriteRules.remove(rewriteRule));
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public void addHttpFilterFactory(HttpFiltersSource filterFactory) {
        filterFactories.add(filterFactory);
    }

    public List<HttpFiltersSource> getFilterFactories() {
        return filterFactories;
    }

    @Override
    public void setMitmManager(MitmManager mitmManager) {
        this.mitmManager = mitmManager;
    }

    @Override
    public void setTrustAllServers(boolean trustAllServers) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot disable upstream server verification after the proxy has been started");
        }

        if (trustAllServers) {
            trustSource = null;
        } else {
            if (trustSource == null) {
                trustSource = TrustSource.defaultTrustSource();
            }
        }
    }

    @Override
    public void setTrustSource(TrustSource trustSource) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot change TrustSource after proxy has been started");
        }

        this.trustSource = trustSource;
    }

    @Override
    public Optional<HarEntry> findMostRecentEntry(Pattern url) {
        List<HarEntry> entries = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url).get();
        return Optional.ofNullable(entries.isEmpty() ? null : entries.get(0));
    }

    @Override
    public Collection<HarEntry> findEntries(Pattern url) {
        return new UrlFilteredHarEntriesSupplier(getHar(), url).get();
    }

    @Override
    public AssertionResult assertMostRecentResponseTimeLessThanOrEqual(Pattern url, long time) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new ResponseTimeLessThanOrEqualAssertion(time);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertResponseTimeLessThanOrEqual(Pattern url, long time) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new ResponseTimeLessThanOrEqualAssertion(time);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseContentContains(Pattern url, String text) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentContainsStringAssertion(text);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseContentDoesNotContain(Pattern url, String text) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentDoesNotContainStringAssertion(text);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseContentMatches(Pattern url, Pattern contentPattern) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentMatchesAssertion(contentPattern);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlContentLengthLessThanOrEquals(Pattern url, Long maxSize) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentSizeLessThanOrEqualAssertion(maxSize);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlContentMatches(Pattern url, Pattern contentPattern) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentMatchesAssertion(contentPattern);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlContentContains(Pattern url, String text) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentContainsStringAssertion(text);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlContentDoesNotContain(Pattern url, String text) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentDoesNotContainStringAssertion(text);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlResponseHeaderContains(Pattern url, String value) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new HeadersContainStringAssertion(value);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlResponseHeaderContains(Pattern url, String name, String value) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = StringUtils.isEmpty(name) ?
                new HeadersContainStringAssertion(value) :
                new FilteredHeadersContainStringAssertion(name, value);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlResponseHeaderDoesNotContain(Pattern url, String name, String value) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = StringUtils.isEmpty(name) ?
                new HeadersDoNotContainStringAssertion(value) :
                new FilteredHeadersDoNotContainStringAssertion(name, value);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlResponseHeaderDoesNotContain(Pattern url, String value) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new HeadersDoNotContainStringAssertion(value);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertAnyUrlResponseHeaderMatches(Pattern url, Pattern namePattern, Pattern valuePattern) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = namePattern == null ?
                new HeadersMatchAssertion(valuePattern) :
                new FilteredHeadersMatchAssertion(namePattern, valuePattern);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertResponseStatusCode(Integer status) {
        HarEntriesSupplier supplier = new CurrentStepHarEntriesSupplier(getHar());
        HarEntryAssertion assertion = new StatusEqualsAssertion(status);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertResponseStatusCode(HttpStatusClass clazz) {
        HarEntriesSupplier supplier = new CurrentStepHarEntriesSupplier(getHar());
        HarEntryAssertion assertion = new StatusBelongsToClassAssertion(clazz);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertResponseStatusCode(Pattern url, Integer status) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new StatusEqualsAssertion(status);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertResponseStatusCode(Pattern url, HttpStatusClass clazz) {
        HarEntriesSupplier supplier = new UrlFilteredHarEntriesSupplier(getHar(), url);
        HarEntryAssertion assertion = new StatusBelongsToClassAssertion(clazz);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseStatusCode(Integer status) {
        HarEntriesSupplier supplier = new MostRecentHarEntrySupplier(getHar());
        HarEntryAssertion assertion = new StatusEqualsAssertion(status);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseStatusCode(HttpStatusClass clazz) {
        HarEntriesSupplier supplier = new MostRecentHarEntrySupplier(getHar());
        HarEntryAssertion assertion = new StatusBelongsToClassAssertion(clazz);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseStatusCode(Pattern url, Integer status) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new StatusEqualsAssertion(status);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseStatusCode(Pattern url, HttpStatusClass clazz) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new StatusBelongsToClassAssertion(clazz);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseContentLengthLessThanOrEqual(Pattern url, Long max) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = new ContentSizeLessThanOrEqualAssertion(max);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseHeaderContains(Pattern url, String name, String value) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = StringUtils.isEmpty(name) ?
                new HeadersContainStringAssertion(value) :
                new FilteredHeadersContainStringAssertion(name, value);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseHeaderDoesNotContain(Pattern url, String name, String value) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = StringUtils.isEmpty(name) ?
                new HeadersDoNotContainStringAssertion(value) :
                new FilteredHeadersDoNotContainStringAssertion(name, value);

        return checkAssertion(supplier, assertion);
    }

    @Override
    public AssertionResult assertMostRecentResponseHeaderMatches(Pattern url, Pattern name, Pattern value) {
        HarEntriesSupplier supplier = new MostRecentUrlFilteredHarEntrySupplier(getHar(), url);
        HarEntryAssertion assertion = name == null ?
                new HeadersMatchAssertion(value) :
                new FilteredHeadersMatchAssertion(name, value);

        return checkAssertion(supplier, assertion);
    }

    private AssertionResult checkAssertion(HarEntriesSupplier harEntriesSupplier, HarEntryAssertion assertion) {
        AssertionResult.Builder result = new AssertionResult.Builder();

        List<HarEntry> entries = harEntriesSupplier.get();

        AtomicInteger failedCount = new AtomicInteger();

        entries.forEach(entry -> {
            AssertionEntryResult.Builder requestResult = new AssertionEntryResult.Builder();
            requestResult.setUrl(entry.getRequest().getUrl());

            Optional<HarEntryAssertionError> error = assertion.assertion(entry);
            requestResult.setFailed(error.isPresent());

            if (error.isPresent()) {
                requestResult.setMessage(error.get().getMessage());
                failedCount.getAndIncrement();
            }

            result.addRequest(requestResult.create());
        });

        String resultMessage = String.format("%d passed, %d total", entries.size() - failedCount.get(), entries.size());

        return result
                .setFilter(harEntriesSupplier.getFilterInfo())
                .setFailed(failedCount.get() > 0)
                .setMessage(resultMessage)
                .setPassed(failedCount.get() == 0)
                .create();
    }

    public boolean isMitmDisabled() {
        return this.mitmDisabled;
    }

    @Override
    public void setMitmDisabled(boolean mitmDisabled) throws IllegalStateException {
        if (isStarted()) {
            throw new IllegalStateException("Cannot disable MITM after the proxy has been started");
        }

        this.mitmDisabled = mitmDisabled;
    }

    public void setUseEcc(boolean useEcc) {
        this.useEcc = useEcc;
    }

    /**
     * Adds the basic BrowserUp-proxy filters, except for the relatively-expensive HAR capture filter.
     */
    protected void addBrowserUpFilters() {
        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new ResolvedHostnameCacheFilter(originalRequest, ctx);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new RegisterRequestFilter(originalRequest, ctx, activityMonitor);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new HttpsOriginalHostCaptureFilter(originalRequest, ctx);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new BlacklistFilter(originalRequest, ctx, getBlacklist());
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                Whitelist currentWhitelist = whitelist.get();
                return new WhitelistFilter(originalRequest, ctx, isWhitelistEnabled(), currentWhitelist.getStatusCode(), currentWhitelist.getPatterns());
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new AutoBasicAuthFilter(originalRequest, ctx, basicAuthCredentials);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new RewriteUrlFilter(originalRequest, ctx, rewriteRules);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new HttpsHostCaptureFilter(originalRequest, ctx);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest) {
                return new AddHeadersFilter(originalRequest, additionalHeaders);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest) {
                return new LatencyFilter(originalRequest, latencyMs);
            }
        });

        addHttpFilterFactory(new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                return new UnregisterRequestFilter(originalRequest, ctx, activityMonitor);
            }
        });
    }

    private int getMaximumRequestBufferSize() {
        int maxBufferSize = 0;
        for (HttpFiltersSource source : filterFactories) {
            int requestBufferSize = source.getMaximumRequestBufferSizeInBytes();
            if (requestBufferSize > maxBufferSize) {
                maxBufferSize = requestBufferSize;
            }
        }

        return maxBufferSize;
    }

    private int getMaximumResponseBufferSize() {
        int maxBufferSize = 0;
        for (HttpFiltersSource source : filterFactories) {
            int requestBufferSize = source.getMaximumResponseBufferSizeInBytes();
            if (requestBufferSize > maxBufferSize) {
                maxBufferSize = requestBufferSize;
            }
        }

        return maxBufferSize;
    }

}