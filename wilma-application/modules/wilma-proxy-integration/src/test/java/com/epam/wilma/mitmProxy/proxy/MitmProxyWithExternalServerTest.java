package com.epam.wilma.mitmProxy.proxy;

import com.epam.wilma.mitmProxy.proxy.helper.AbstractProxyTool;
import com.epam.wilma.mitmProxy.proxy.helper.ContentEncoding;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultRequestInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultResponseInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.ResponseInfo;
import org.apache.http.HttpHost;
import org.junit.Test;
import website.magyar.mitm.proxy.ProxyServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests just a single basic proxy running as a man in the middle.
 */
public class MitmProxyWithExternalServerTest extends AbstractProxyTool {

    @Override
    protected void setUp() {
        String stubUrl = "http://127.0.0.1:" + stubServerPort + "/stub";
        LOGGER.info("STUB URL used: {}", stubUrl);
        DefaultRequestInterceptor defaultRequestInterceptor = new DefaultRequestInterceptor(requestCount, NEED_STUB_RESPONSE, stubUrl);
        DefaultResponseInterceptor defaultResponseInterceptor = new DefaultResponseInterceptor(responseCount);
        proxyServer.addRequestInterceptor(defaultRequestInterceptor);
        proxyServer.addResponseInterceptor(defaultResponseInterceptor);
        proxyServer.setCaptureBinaryContent(false);
        proxyServer.setCaptureContent(false);
        ProxyServer.setResponseVolatile(true);
    }

    @Test
    public void testSimpleLocalGetRequestOverHTTPSThroughProxy() throws Exception {
        String CALL = "/ok";
        HttpHost externalHost = new HttpHost("127.0.0.1", 8443, "https");
        try {
            httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        } catch (Exception e) {
            externalHost = null;
        }
        org.junit.Assume.assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, true, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(proxiedResponse.getBody().contains("Wilma Test Server"));
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimpleLocalGetRequestOverHTTPSWithoutProxy() throws Exception {
        String CALL = "/ok";
        HttpHost externalHost = new HttpHost("127.0.0.1", 8443, "https");
        try {
            httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        } catch (Exception e) {
            externalHost = null;
        }
        org.junit.Assume.assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(proxiedResponse.getBody().contains("Wilma Test Server"));
        assertEquals(0, responseCount.get());
        assertEquals(0, requestCount.get());
    }

    @Test
    public void testSimpleRemoteGetRequestOverHTTPSThroughProxy() throws Exception {
        //check if external test server is available
        String CALL = "/search?q=mitmJavaProxy";
        HttpHost externalHost = new HttpHost("www.google.com", 443, "https");
        try {
            httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        } catch (Exception e) {
            externalHost = null;
        }
        org.junit.Assume.assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, true, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(responseCount.get() > 0);
        assertTrue(requestCount.get() > 0);
    }

    @Test
    public void testSimpleRemoteGetRequestOverHTTPSWithoutProxy() throws Exception {
        //check if external test server is available
        String CALL = "/search?q=mitmJavaProxy";
        HttpHost externalHost = new HttpHost("www.google.com", 443, "https");
        try {
            httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        } catch (Exception e) {
            externalHost = null;
        }
        org.junit.Assume.assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(0, responseCount.get());
        assertEquals(0, requestCount.get());
    }

}
