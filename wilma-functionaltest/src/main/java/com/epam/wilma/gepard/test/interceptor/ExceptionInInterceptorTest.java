package com.epam.wilma.gepard.test.interceptor;

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

import java.io.FileNotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Functional tests for checking Wilma behavior when an interceptor throws exception.
 */
@TestClass(id = "Stub Interceptor", name = "Exception in Interceptor")
public class ExceptionInInterceptorTest extends WilmaTestCase {
    private static final String STUB_CONFIG = "resources/interceptor/usage/stubConfigExInterceptorOn.json";
    private static final String TEST_SERVER_RESPONSE = "resources/interceptor/usage/resetSequenceResponse.txt";
    private static final String INTERCEPTOR_RESOURCE_BASE = "resources/interceptor/usage/";
    private static final String INTERCEPTOR_CLASS = "ThrowingNPEInterceptor.class";
    private static final String MESSAGE_NOT_YET_AVAILABLE = "Requested file not found.";

    private RequestParameters createRequest(final String response) throws Exception {
        setOriginalRequestMessageEmpty();
        setExpectedResponseMessageFromFile(response);
        return createRequestParameters();
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrlBase() + "resetsequences";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain;charset=UTF-8";
        String acceptHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        String contentEncoding = "";
        String acceptEncoding = "gzip";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).acceptHeader(acceptHeader).acceptEncoding(acceptEncoding).contentEncoding(contentEncoding);
    }

    @Test
    public void testExceptionHandlingAtInterceptors() throws Exception {
        //given
        setLocalhostBlockingTo("off");
        setMessageMarkingTo("on");
        setOperationModeTo("wilma");
        uploadInterceptorToWilma(INTERCEPTOR_CLASS, INTERCEPTOR_RESOURCE_BASE + INTERCEPTOR_CLASS);
        uploadStubConfigToWilma(STUB_CONFIG);
        setInterceptorModeTo("on");
        //when - send the request
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);
        ResponseHolder response = callWilmaWithGetMethod(requestParameters2);
        //then - receive and analyse the response
        //identify the message id first
        String[] lines = response.getResponseMessage().split("\n");

        setMessageMarkingTo("off");

        assertNotNull(lines);
        assertTrue(lines.length == 3);
        String id = lines[2].substring(26);
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
        assertTrue("Request was not arrived.", !wilmaReq.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE));
        assertTrue("Response was not arrived.", !wilmaResp.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE));
    }

}
