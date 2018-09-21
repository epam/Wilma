package com.epam.wilma.gepard.test.stub;
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
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Tests three dialog descriptor usage: hitcount, timeout, disabled.
 *
 * @author Tunde_Kovacs
 */
@TestClass(id = "Stub DD Usage", name = "Stub - Dialog descriptor usage: hitcount, timeout, disabled")
public class DDUsageTest extends WilmaTestCase {

    private static final String STUB_CONFIG_WITH_HIT_COUNT = "resources/stub/usage/stubConfigWithHitcount.json";
    private static final String STUB_CONFIG_WITH_TIMEOUT = "resources/stub/usage/stubConfigWithTimeout.json";
    private static final String STUB_CONFIG_WITH_DISABLED = "resources/stub/usage/stubConfigWithDisabled.json";
    private static final String REQUEST = "resources/example2.xml";
    private static final String STUB_RESPONSE = "resources/stub/usage/stubResponse.txt";
    private static final String TEST_SERVER_RESPONSE = "resources/uc3_1TestResponse.txt";

    @Test
    public void testStubDDHitCount() throws Exception {
        uploadStubConfigToWilma(STUB_CONFIG_WITH_HIT_COUNT);
        RequestParameters requestParameters1 = createRequest(STUB_RESPONSE);
        callWilmaWithPostMethodAndAssertResponse(requestParameters1);
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);
        callWilmaWithPostMethodAndAssertResponse(requestParameters2);
    }

    @Test
    public void testStubDDTimeout() throws Exception {
        uploadStubConfigToWilma(STUB_CONFIG_WITH_TIMEOUT);
        RequestParameters requestParameters1 = createRequest(STUB_RESPONSE);
        callWilmaWithPostMethodAndAssertResponse(requestParameters1);
        waitOneMinute();
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);
        callWilmaWithPostMethodAndAssertResponse(requestParameters2);
    }

    @Test
    public void testStubDDDisabled() throws Exception {
        uploadStubConfigToWilma(STUB_CONFIG_WITH_DISABLED);
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);
        callWilmaWithPostMethodAndAssertResponse(requestParameters2);
    }

    private RequestParameters createRequest(final String response) throws Exception {
        setOriginalRequestMessageFromFile(REQUEST);
        setExpectedResponseMessageFromFile(response);
        return createRequestParameters();
    }

    private void waitOneMinute() throws InterruptedException {
        Thread.sleep(60000);
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/xml";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(REQUEST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

}
