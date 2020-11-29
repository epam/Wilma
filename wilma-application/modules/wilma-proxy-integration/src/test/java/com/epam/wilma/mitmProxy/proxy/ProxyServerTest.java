package com.epam.wilma.mitmProxy.proxy;

import com.epam.mitm.proxy.ProxyServer;
import com.epam.wilma.mitmProxy.proxy.helper.AbstractProxyTool;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultRequestInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultResponseInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.ResponseInfo;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests just a single basic proxy running as a man in the middle.
 */
public class ProxyServerTest extends AbstractProxyTool {

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
    public void testSimpleGetRequest() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(webHost, NO_NEED_STUB_RESPONSE, true, false);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Ignore
    @Test
    public void testSimpleGetRequestOverHTTPS() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(httpsWebHost, NO_NEED_STUB_RESPONSE, true, false);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimplePostRequest() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(webHost, NO_NEED_STUB_RESPONSE, true);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Ignore
    @Test
    public void testSimplePostRequestOverHTTPS() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(httpsWebHost, NO_NEED_STUB_RESPONSE, true);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimpleGetRequestToStub() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(webHost, NEED_STUB_RESPONSE, true, false);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimpleGetRequestOverHTTPSToStub() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(httpsWebHost, NEED_STUB_RESPONSE, true, false);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimplePostRequestToStub() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(webHost, NEED_STUB_RESPONSE, true);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimplePostRequestOverHTTPSToStub() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(httpsWebHost, NEED_STUB_RESPONSE, true);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

}
