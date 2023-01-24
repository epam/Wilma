package com.epam.wilma.mitmproxy.proxy;
/*==========================================================================
Copyright since 2013, EPAM Systems

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

import com.epam.wilma.mitmproxy.proxy.helper.AbstractProxyTool;
import com.epam.wilma.mitmproxy.proxy.helper.ContentEncoding;
import com.epam.wilma.mitmproxy.proxy.helper.DefaultRequestInterceptor;
import com.epam.wilma.mitmproxy.proxy.helper.DefaultResponseInterceptor;
import com.epam.wilma.mitmproxy.proxy.helper.ResponseInfo;
import org.junit.jupiter.api.Test;
import website.magyar.mitm.proxy.ProxyServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests just a single basic proxy running as a man in the middle.
 *
 * @author Tamas_Kohegyi
 */
public abstract class ProxyServerTestWithEncodingBase extends AbstractProxyTool {

    protected ContentEncoding contentEncoding;

    protected abstract void setUpContentEncoding();

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
        setUpContentEncoding();
    }

    @Test
    public void testSimpleGetRequest() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(webHost, NO_NEED_STUB_RESPONSE, true, false, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    //@Ignore
    @Test
    public void testSimpleGetRequestOverHTTPS() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(httpsWebHost, NO_NEED_STUB_RESPONSE, true, false, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimplePostRequest() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(webHost, NO_NEED_STUB_RESPONSE, true, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    //@Ignore
    @Test
    public void testSimplePostRequestOverHTTPS() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(httpsWebHost, NO_NEED_STUB_RESPONSE, true, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(SERVER_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimpleGetRequestToStub() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(webHost, NEED_STUB_RESPONSE, true, false, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimpleGetRequestOverHTTPSToStub() throws Exception {
        ResponseInfo proxiedResponse = httpGetWithApacheClient(httpsWebHost, NEED_STUB_RESPONSE, true, false, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimplePostRequestToStub() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(webHost, NEED_STUB_RESPONSE, true, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimplePostRequestOverHTTPSToStub() throws Exception {
        ResponseInfo proxiedResponse = httpPostWithApacheClient(httpsWebHost, NEED_STUB_RESPONSE, true, contentEncoding);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(STUB_BACKEND, proxiedResponse.getBody());
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

}
