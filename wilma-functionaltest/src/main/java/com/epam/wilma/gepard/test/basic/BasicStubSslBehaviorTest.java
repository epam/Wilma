package com.epam.wilma.gepard.test.basic;

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
 * Sends example3.xml to the wilma webApplication over https.
 */
@TestClass(id = "Basic - Stub - SSL", name = "Stub Answers")
public class BasicStubSslBehaviorTest extends WilmaTestCase {
    private static final String EXAMPLE_3_XML = "resources/example3.xml";
    private static final String RESOURCE_FILE_NAME = "example3.xml";
    private static final String STUB_CONFIG = "resources/stubConfig.json";

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcContentType = getDataDrivenTestParameter("PAR1");
    private String tcAcceptHeader = getDataDrivenTestParameter("PAR2");
    private String tcContentEncoding = getDataDrivenTestParameter("PAR3");
    private String tcAcceptEncoding = getDataDrivenTestParameter("PAR4");

    /**
     * B, send the req2-xml message to Apache (use wilma as proxy), but when the this request arrives to Wilma,
     * Wilma sends resp-xml2 back as response instead of forwarding te request to Apache.
     * (don't forget to log the messages)
     *
     * @throws Exception
     */
    @Test
    public void testBasicStubBehaviorWithSsl() throws Exception {
        //given
        clearAllOldStubConfigs();
        setOperationModeTo("wilma");
        uploadTemplateToWilma(RESOURCE_FILE_NAME, EXAMPLE_3_XML);
        uploadStubConfigToWilma(STUB_CONFIG);
        setOriginalRequestMessageFromFile(EXAMPLE_3_XML);
        setExpectedResponseMessageFromFile("resources/uc3_2TestResponse.txt");
        RequestParameters requestParameters = createRequestParameters();
        //when and then
        callWilmaWithPostMethodAndAssertResponse(requestParameters); //send request, receive response and check if expected result test is in the result
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaSSLTestServerUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + tcContentType;
        String acceptHeader = "application/" + tcAcceptHeader;
        String contentEncoding = tcContentEncoding;
        String acceptEncoding = tcAcceptEncoding;
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_3_XML)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }
}
