package com.epam.wilma.mitmProxy.proxy;

import com.epam.wilma.mitmProxy.proxy.helper.AbstractProxyTool;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultRequestInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.DefaultResponseInterceptor;
import com.epam.wilma.mitmProxy.proxy.helper.ResponseInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.rockhill.mitm.proxy.ProxyServer;

import static org.junit.Assert.assertEquals;

/**
 * Tests just a single basic proxy running as a man in the middle.
 */
public class ProxyServerMinimalHTTPSTest extends AbstractProxyTool {

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

    @Ignore
    @Test
    public void testSimpleGetRequestOverHTTPS() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(httpsWebHost, NO_NEED_STUB_RESPONSE, true, false);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

}
