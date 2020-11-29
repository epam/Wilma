package com.epam.wilma.mitmProxy.proxy;

import com.epam.mitm.proxy.ProxyServer;
import com.epam.wilma.mitmProxy.proxy.helper.AbstractProxyTool;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultRequestInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultResponseInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.ResponseInfo;
import org.apache.http.HttpHost;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.SSLContext;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests just a single basic proxy running as a man in the middle.
 */
public class ExtraMitmProxyTest extends AbstractProxyTool {

    private HttpHost externalHost = null;
    private final static String EXTERNAL_CALL = "/ok";

    @Override
    protected void setUp() throws NoSuchAlgorithmException {
        String stubUrl = "http://127.0.0.1:" + stubServerPort + "/stub";
        LOGGER.info("STUB URL used: {}", stubUrl);
        DefaultRequestInterceptor defaultRequestInterceptor = new DefaultRequestInterceptor(requestCount, NEED_STUB_RESPONSE, stubUrl);
        DefaultResponseInterceptor defaultResponseInterceptor = new DefaultResponseInterceptor(responseCount);
        proxyServer.addRequestInterceptor(defaultRequestInterceptor);
        proxyServer.addResponseInterceptor(defaultResponseInterceptor);
        proxyServer.setCaptureBinaryContent(false);
        proxyServer.setCaptureContent(false);
        ProxyServer.setResponseVolatile(true);
        //check if external test server is available
        LOGGER.info("*** Available HTTPS Protocols for Wilma: {}", String.join(" ", SSLContext.getDefault().getSupportedSSLParameters().getProtocols()));
    }

    @Ignore
    @Test
    public void testSimpleGetRequestOverHTTPS() throws Exception {
        externalHost = new HttpHost("127.0.0.1", 8443, "https");
        try {
            httpGetWithApacheClient(externalHost, EXTERNAL_CALL, false, false);
        } catch (Exception e) {
            externalHost = null;
        }
        org.junit.Assume.assumeTrue(externalHost != null);
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, EXTERNAL_CALL, true, false);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(proxiedResponse.getBody().contains("Wilma Test Server"));
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

}
