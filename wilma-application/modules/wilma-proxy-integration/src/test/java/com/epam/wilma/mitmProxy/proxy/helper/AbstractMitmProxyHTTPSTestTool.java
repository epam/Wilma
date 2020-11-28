package com.epam.wilma.mitmProxy.proxy.helper;

import com.epam.mitm.proxy.ProxyServer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * Base for tests that test the proxy. This base class encapsulates all of the
 * testing infrastructure.
 */
public abstract class AbstractMitmProxyHTTPSTestTool {

    /**
     * The server used by the tests.
     */
    public static final int PROXY_TIMEOUT = 60000; //1 minute
    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractMitmProxyHTTPSTestTool.class);
    protected static final String NO_NEED_STUB_RESPONSE = "/getServer";
    protected static final String NEED_STUB_RESPONSE = "/getStub";
    protected static final String SERVER_BACKEND = "server-backend";
    protected static final String STUB_BACKEND = "stub-backend";
    public ProxyServer proxyServer;
    protected int webServerPort = -1;
    protected int stubServerPort = -1;
    protected int httpsWebServerPort = -1;
    protected int proxyPort = -1;
    protected HttpHost httpsWebHost;
    protected AtomicInteger requestCount;
    protected AtomicInteger responseCount;
    /**
     * The web server that provides the back-end.
     */
    private Server webServer;

    @Before
    public void runSetup() throws Exception {
        initializeCounters();
        startServers();
        startProxy();
        //and finally
        setUp();
    }

    protected abstract void setUp() throws Exception;

    private void initializeCounters() {
        requestCount = new AtomicInteger(0);
        responseCount = new AtomicInteger(0);
    }

    private void startProxy() throws Exception {
        proxyServer = new ProxyServer(0);
        proxyServer.start(PROXY_TIMEOUT);
        proxyPort = proxyServer.getPort();
    }

    private void startServers() {
        webServer = TestUtils.startWebServerWithResponse(true, SERVER_BACKEND.getBytes());
        // find out what ports the HTTP and HTTPS connectors were bound to
        httpsWebServerPort = TestUtils.findLocalHttpsPort(webServer);
        if (httpsWebServerPort < 0) {
            throw new RuntimeException("HTTPS connector should already be open and listening, but port was " + webServerPort);
        }

        webServerPort = TestUtils.findLocalHttpPort(webServer);
        if (webServerPort < 0) {
            throw new RuntimeException("HTTP connector should already be open and listening, but port was " + webServerPort);
        }

        httpsWebHost = new HttpHost("127.0.0.1", httpsWebServerPort, "https");

    }

    @After
    public void runTearDown() throws Exception {
        try {
            tearDown();
        } finally {
            try {
                if (this.proxyServer != null) {
                    this.proxyServer.stop();
                }
            } finally {
                if (this.webServer != null) {
                    webServer.stop();
                }
            }
        }

    }

    protected void tearDown() throws Exception {
    }

    protected ResponseInfo httpGetWithApacheClient(HttpHost host, String resourceUrl, boolean isProxied, boolean callHeadFirst)
            throws Exception {
        DefaultHttpClient httpClient = TestUtils.buildHttpClient();
        try {
            if (isProxied) {
                HttpHost proxy = new HttpHost("127.0.0.1", proxyServer.getPort());
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            }

            Integer contentLength = null;
            if (callHeadFirst) {
                HttpHead request = new HttpHead(resourceUrl);
                request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
                HttpResponse response = httpClient.execute(host, request);
                contentLength = new Integer(response.getFirstHeader("Content-Length").getValue());
            }

            HttpGet request = new HttpGet(resourceUrl);
            request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);

            HttpResponse response = httpClient.execute(host, request);
            HttpEntity resEntity = response.getEntity();

            if (contentLength != null) {
                assertEquals(
                        "Content-Length from GET should match that from HEAD",
                        contentLength,
                        new Integer(response.getFirstHeader("Content-Length").getValue()));
            }
            return new ResponseInfo(response.getStatusLine().getStatusCode(), EntityUtils.toString(resEntity));
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

}
