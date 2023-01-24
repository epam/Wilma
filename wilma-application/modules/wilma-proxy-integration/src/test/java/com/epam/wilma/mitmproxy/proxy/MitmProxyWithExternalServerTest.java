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
import org.apache.http.HttpHost;
import org.junit.jupiter.api.Test;
import website.magyar.mitm.proxy.ProxyServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests just a single basic proxy running as a man in the middle.
 *
 * @author Tamas_Kohegyi
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
        assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, true, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(proxiedResponse.getBody().contains("Wilma Test Server"));
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testSimpleLocalGetRequestOverHTTPSThroughProxyEnforceBrotli() throws Exception {
        String CALL = "/ok";
        HttpHost externalHost = new HttpHost("127.0.0.1", 8443, "https");
        try {
            httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.BROTLI);
        } catch (Exception e) {
            externalHost = null;
        }
        assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, true, false, ContentEncoding.BROTLI);
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
        assumeTrue(externalHost != null);
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
        assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, true, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(responseCount.get() > 0);
        assertTrue(requestCount.get() > 0);
    }

    @Test
    public void testSimpleRemoteGetRequestOverHTTPSThroughProxyEnforceBrotli() throws Exception {
        //check if external test server is available
        String CALL = "/search?q=mitmJavaProxy";
        HttpHost externalHost = new HttpHost("www.google.com", 443, "https");
        try {
            httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.BROTLI);
        } catch (Exception e) {
            externalHost = null;
        }
        assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, true, false, ContentEncoding.BROTLI);
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
        assumeTrue(externalHost != null);
        //do test if available
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, CALL, false, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertEquals(0, responseCount.get());
        assertEquals(0, requestCount.get());
    }

}
