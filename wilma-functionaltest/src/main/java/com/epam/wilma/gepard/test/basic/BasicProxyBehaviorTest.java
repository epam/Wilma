package com.epam.wilma.gepard.test.basic;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.annotations.TestParameter;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;

/**
 * Sends the example2.xml to the test server over http.
 * @author tkohegyi
 */
@TestClass(id = "Basic - Proxy", name = "Proxy Answers")
public class BasicProxyBehaviorTest extends WilmaTestCase {

    private static final String STUB_CONFIG_XML = "resources/stubConfig.xml";
    private static final String RESOURCE = "resources/example4.xml";
    private static final String RESOURCE_FILE_NAME = "example4.xml";
    private static final String EXAMPLE_2 = "resources/example2.xml";

    @TestParameter(id = "PAR0")
    private String tcName;
    @TestParameter(id = "PAR1")
    private String tcContentType;
    @TestParameter(id = "PAR2")
    private String tcAcceptHeader;
    @TestParameter(id = "PAR3")
    private String tcContentEncoding;
    @TestParameter(id = "PAR4")
    private String tcAcceptEncoding;

    /**
     * A, send the req1-xml message to Apache (use wilma as proxy), but when the this request arrives to Wilma,
     * Wilma sends resp-xml1 back as response instead of forwarding te request to Apache.
     * (don't forget to log the messages)
     * @throws Exception
     */

    public void testBasicProxyBehavior() throws Exception {
        //given
        setLocalhostBlockingTo("off");
        uploadTemplateToWilma(RESOURCE_FILE_NAME, RESOURCE);
        uploadStubConfigToWilma(STUB_CONFIG_XML);
        setOperationModeTo("wilma");
        setOriginalRequestMessageFromFile(EXAMPLE_2);
        setExpectedResponseMessageFromFile("resources/uc3_1TestResponse.txt");
        RequestParameters requestParameters = createRequestParameters();
        if (tcName.contains("SSL")) {
            naTestCase("SSL part testing is not prepared.");
        }
        //when and then
        callWilmaWithPostMethodAndAssertResponse(requestParameters); //send request, receive response and check if expected result test is in the result
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrl();
        String wilmaHost = getClassData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getClassData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + tcContentType;
        String acceptHeader = "application/" + tcAcceptHeader;
        String contentEncoding = tcContentEncoding;
        String acceptEncoding = tcAcceptEncoding;
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .xmlIS(new FileInputStream(EXAMPLE_2)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }
}
