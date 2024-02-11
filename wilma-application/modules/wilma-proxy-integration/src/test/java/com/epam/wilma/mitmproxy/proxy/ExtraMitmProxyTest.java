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

import javax.net.ssl.SSLContext;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests just a single basic proxy running as a man in the middle.
 *
 * @author Tamas_Kohegyi
 */
public class ExtraMitmProxyTest extends AbstractProxyTool {

    private static final String EXTERNAL_CALL = "/ok";

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

    @Test
    public void testSimpleGetRequestOverHTTPS() throws Exception {
        HttpHost externalHost = new HttpHost("127.0.0.1", 8443, "https");
        try {
            httpGetWithApacheClient(externalHost, EXTERNAL_CALL, false, false, ContentEncoding.ANY);
        } catch (Exception e) {
            externalHost = null;
        }
        assumeTrue(externalHost != null);
        ResponseInfo proxiedResponse = httpGetWithApacheClient(externalHost, EXTERNAL_CALL, true, false, ContentEncoding.ANY);
        assertEquals(200, proxiedResponse.getStatusCode());
        assertTrue(proxiedResponse.getBody().contains("Wilma Test Server"));
        assertEquals(1, responseCount.get());
        assertEquals(1, requestCount.get());
    }

}
