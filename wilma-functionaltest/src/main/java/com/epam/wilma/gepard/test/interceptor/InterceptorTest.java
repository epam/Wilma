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
 * Functional tests for Interceptor functionality.
 */
@TestClass(id = "Stub Interceptor", name = "Stub - Interceptor usage test")
public class InterceptorTest extends WilmaTestCase {
    private static final String STUB_CONFIG_DEFAULT_INTERCEPTOR_SETUP_ON_ON = "resources/interceptor/usage/stubConfigOnOnInterceptor.json";
    private static final String STUB_CONFIG_DEFAULT_INTERCEPTOR_SETUP_ON_OFF = "resources/interceptor/usage/stubConfigOnOffInterceptor.json";
    private static final String STUB_CONFIG_DEFAULT_INTERCEPTOR_SETUP_OFF_ON = "resources/interceptor/usage/stubConfigOffOnInterceptor.json";
    private static final String TEST_SERVER_RESPONSE = "resources/interceptor/usage/resetSequenceResponse.txt";
    private static final String INTERCEPTOR_RESOURCE_BASE = "resources/interceptor/usage/";
    private static final String INTERCEPTOR_REQ_CLASS = "DummyReqInterceptor.class";
    private static final String INTERCEPTOR_RESP_CLASS = "DummyRespInterceptor.class";

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcInterceptorOnOff = getDataDrivenTestParameter("PAR1");
    private String tcReqRespInterceptor = getDataDrivenTestParameter("PAR2");
    private String tcReqInResp = getDataDrivenTestParameter("PAR3");
    private String tcRespInResp = getDataDrivenTestParameter("PAR4");

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

    private void setInterceptorMode() throws Exception {
        String onOffCommand = tcInterceptorOnOff;
        setInterceptorModeTo(onOffCommand);
    }

    private void setupInterceptors() throws Exception {
        // upload interceptor classes - to be sure
        uploadInterceptorToWilma(INTERCEPTOR_REQ_CLASS, INTERCEPTOR_RESOURCE_BASE + INTERCEPTOR_REQ_CLASS);
        uploadInterceptorToWilma(INTERCEPTOR_RESP_CLASS, INTERCEPTOR_RESOURCE_BASE + INTERCEPTOR_RESP_CLASS);
        // interceptors first: 1 resp only, 2 req only, 3 req+resp together
        String testMode = STUB_CONFIG_DEFAULT_INTERCEPTOR_SETUP_ON_ON; // default: 3
        if ("1".equals(tcReqRespInterceptor)) {
            testMode = STUB_CONFIG_DEFAULT_INTERCEPTOR_SETUP_OFF_ON;
        }
        if ("2".equals(tcReqRespInterceptor)) {
            testMode = STUB_CONFIG_DEFAULT_INTERCEPTOR_SETUP_ON_OFF;
        }
        uploadStubConfigToWilma(testMode);
        // interceptor
        setInterceptorMode();
    }

    @Test
    public void testInterceptorUsage() throws Exception {
        setLocalhostBlockingTo("off");

        setMessageMarkingTo("on");

        setOperationModeTo("wilma");
        /* given - setup interceptors, set interceptor usage on/off, set session on/off */
        setupInterceptors();
        /* when - send request */
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);

        ResponseHolder response = callWilmaWithGetMethod(requestParameters2);
        /* then - receive and analyze response */
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
        boolean reqIntercepted = wilmaReq.getResponseMessage().contains("WILMA_TESTINTERCEPTOR_REQ=yes");
        boolean respIntercepted = wilmaResp.getResponseMessage().contains("WILMA_TESTINTERCEPTOR_RESP=yes");
        if ("yes".equals(tcReqInResp)) {
            //need req interceptor is req
            assertTrue("Req not intercepted, however interception is expected.", reqIntercepted);
        } else {
            assertTrue("Req intercepted, however no interception is expected.", !reqIntercepted);
        }
        if ("yes".equals(tcRespInResp)) {
            //need resp interceptor is resp
            assertTrue("Resp not intercepted, however interception is expected.", respIntercepted);
        } else {
            assertTrue("Resp intercepted, however no interception is expected.", !respIntercepted);
        }
    }

}
