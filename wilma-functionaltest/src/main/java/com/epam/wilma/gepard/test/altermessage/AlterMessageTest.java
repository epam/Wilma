package com.epam.wilma.gepard.test.altermessage;

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

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Functional tests for checking custom postfix settings at logged messages.
 */
@TestClass(id = "Proxy Alter Message", name = "Basic Test")
public class AlterMessageTest extends WilmaTestCase {
    private static final String STUB_CONFIG = "resources/interceptor/AlterProxyMessage/stubAlterMessage.json";
    private static final String TEST_SERVER_REQUEST = "NEED_ANSWER BLAH BLAH";
    private static final String TEST_SERVER_ALTERED_REQUEST = "NEED_ANSWER BAAH BAAH"; //L -> A
    private static final String TEST_SERVER_RESPONSE = "NEED_ANSWER AAAH AAAH";  //B -> A
    private static final String INTERCEPTOR_RESOURCE_BASE = "resources/interceptor/AlterProxyMessage/";
    private static final String INTERCEPTOR_CLASS = "MessageAlterInterceptor.class";

    private RequestParameters createRequest(final String response) throws Exception {
        setOriginalRequestMessageEmpty();
        setExpectedResponseMessage(response);
        return createRequestParameters();
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrlBase() + "example";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain;charset=UTF-8";
        String acceptHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        String contentEncoding = "";
        String acceptEncoding = ""; //cannot be gzip - this interceptor is not handling such method
        InputStream stream = new ByteArrayInputStream(TEST_SERVER_REQUEST.getBytes(StandardCharsets.UTF_8));
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).acceptHeader(acceptHeader).acceptEncoding(acceptEncoding).contentEncoding(contentEncoding)
                .customHeader("RemoveThis", "blah").customHeader("AlterMessage", "true").requestInputStream(stream);
    }

    @Test
    public void testAlterMessage() throws Exception {
        //given
        setLocalhostBlockingTo("off");
        setMessageMarkingTo("on");
        setResponseMessageVolatilityTo("on");
        setOperationModeTo("wilma");
        uploadInterceptorToWilma(INTERCEPTOR_CLASS, INTERCEPTOR_RESOURCE_BASE + INTERCEPTOR_CLASS);
        uploadStubConfigToWilma(STUB_CONFIG);
        setInterceptorModeTo("on");
        //when - send the request
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);
        ResponseHolder response = callWilmaWithPostMethod(requestParameters2);
        //then - receive and analyse the response
        //identify the message id first
        String[] lines = response.getResponseMessage().split(" ");

        setMessageMarkingTo("off");

        assertNotNull(lines);
        assertTrue(lines.length == 4);
        String id = lines[0];
        logComment("Used message id: " + id);
        // now get req and resp messages stored by wilma,
        // url is similar like http://wilma.server.url:1234/config/messages/20140620121508.0000req.txt?source=true
        String reqRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id + "req.txt?source=true";
        String respRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id + "resp.txt?source=true";
        ResponseHolder wilmaReq = getSlowMessageFromWilma(reqRequestUrl);
        ResponseHolder wilmaResp = getSlowMessageFromWilma(respRequestUrl);
        //now we can analyse
        assertNotNull("Problem during waiting for the request.", wilmaReq);
        assertNotNull("Problem during waiting for the response.", wilmaResp);
        assertTrue("Request was not arrived.", wilmaReq.getResponseMessage().contains(TEST_SERVER_ALTERED_REQUEST));
        assertTrue("Response was not arrived.", wilmaResp.getResponseMessage().contains(TEST_SERVER_RESPONSE));
    }

}
