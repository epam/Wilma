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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Functional tests for checking the possibility of disabling individual messages.
 */
@TestClass(id = "Stub Interceptor", name = "Disable Individual Message Logging Test")
public class DisableIndividualMessageLoggingTest extends WilmaTestCase {
    private static final String STUB_CONFIG = "resources/interceptor/IndividualMessageLogging/stubConfigInterceptorOn.json";
    private static final String TEST_SERVER_RESPONSE = "resources/interceptor/usage/resetSequenceResponse.txt";
    private static final String INTERCEPTOR_RESOURCE_BASE = "resources/interceptor/IndividualMessageLogging/";
    private static final String INTERCEPTOR_CLASS = "IndividualMessageLoggingInterceptor.class";
    private static final String MESSAGE_NOT_YET_AVAILABLE = "Requested file not found.";
    private static final String REQUEST_LOGGED_TEXT = "resources/interceptor/IndividualMessageLogging/requestLogged.txt";
    private static final String REQUEST_NOTLOGGED_TEXT = "resources/interceptor/IndividualMessageLogging/requestNotLogged.txt";

    private RequestParameters createRequest(final String response, final String fileToSend) throws Exception {
        setOriginalRequestMessageEmpty();
        setExpectedResponseMessageFromFile(response);
        return createRequestParameters(fileToSend);
    }

    protected RequestParameters createRequestParameters(final String fileToSend) throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain;charset=UTF-8";
        String acceptHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(fileToSend)).contentType(contentType)
                .acceptHeader(acceptHeader).contentEncoding("").acceptEncoding("");
    }

    private void given() throws Exception {
        setLocalhostBlockingTo("off");
        setMessageMarkingTo("on");
        setOperationModeTo("wilma");
        uploadInterceptorToWilma(INTERCEPTOR_CLASS, INTERCEPTOR_RESOURCE_BASE + INTERCEPTOR_CLASS);
        uploadStubConfigToWilma(STUB_CONFIG);
        setInterceptorModeTo("on");
    }

    @Test
    public void testDisablingIndividualMessageLogging() throws Exception {
        //given
        given();
        //when - send 2 requests, first to "SKIPLOG", the second to DONT SKIP LOG
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE, REQUEST_NOTLOGGED_TEXT);
        ResponseHolder response = callWilmaWithPostMethod(requestParameters2);
        RequestParameters requestParameters3 = createRequest(TEST_SERVER_RESPONSE, REQUEST_LOGGED_TEXT);
        ResponseHolder response3 = callWilmaWithPostMethod(requestParameters3);
        //then - receive and analyse the response
        //identify the message ids first
        String[] lines = response.getResponseMessage().split("\n");
        String[] lines3 = response3.getResponseMessage().split("\n");

        setMessageMarkingTo("off");

        assertNotNull(lines);
        assertTrue(lines.length == 1);
        assertTrue(lines3.length == 1);
        String id = lines[0].split(" ")[0];
        String id3 = lines3[0].split(" ")[0];
        logComment("Used message id for the not logged message is: " + id);
        logComment("Used message id for the logged message is: " + id3);
        // now get req and resp messages stored by wilma,
        // url is similar like http://wilma.server.url:1234/config/messages/20140620121508.0000req.txt?source=true
        String reqRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id3 + "req.txt?source=true";
        String respRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id3 + "resp.txt?source=true";
        ResponseHolder wilmaReq = getSlowMessageFromWilma(reqRequestUrl);
        ResponseHolder wilmaResp = getSlowMessageFromWilma(respRequestUrl);
        //now we can analyse that the second is arrived
        assertNotNull("Problem during waiting for the request.", wilmaReq);
        assertNotNull("Problem during waiting for the response.", wilmaResp);
        assertTrue("Request was not arrived.", !wilmaReq.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE));
        assertTrue("Response was not arrived.", !wilmaResp.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE));
        //and check if the first is not - neither req, nor response
        reqRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id + "req.txt?source=true";
        respRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id + "resp.txt?source=true";
        requestParameters2.useProxy(false);
        requestParameters2.testServerUrl(reqRequestUrl);
        wilmaResp = callWilmaWithGetMethod(requestParameters2);
        if (!wilmaResp.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE)) {
            fail("Unfortunately we found the Request file logged, however it should not be logged.");
        }
        requestParameters2.testServerUrl(respRequestUrl);
        wilmaResp = callWilmaWithGetMethod(requestParameters2);
        if (!wilmaResp.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE)) {
            fail("Unfortunately we found the Response file logged, however it should not be logged.");
        }
    }

}
