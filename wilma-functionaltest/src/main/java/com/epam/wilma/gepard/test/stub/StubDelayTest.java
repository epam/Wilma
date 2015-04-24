package com.epam.wilma.gepard.test.stub;
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

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.annotations.TestParameter;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Tests wilma's stub config.
 *
 * @author Tunde_Kovacs
 */
@TestClass(id = "Stub Delay", name = "Stub")
public class StubDelayTest extends WilmaTestCase {

    private static final String XSL_FILE_NAME = "exampleBase.xsl";
    private static final String XSL = "resources/stub/TC1/exampleBase.xsl";
    private static final String TEMPLATE = "resources/stub/templateExample.xml";
    private static final String TEMPLATE_FILE_NAME = "templateExample.xml";

    private String request;

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
    @TestParameter(id = "PAR5")
    private String tcStubConfig;
    @TestParameter(id = "PAR6")
    private String tcRequest;
    @TestParameter(id = "PAR7")
    private String tcResponse;

    public void testStubShouldDelayResponse() throws Exception {
        setOperationModeTo("wilma");
        uploadResources();
        RequestParameters requestParameters = createRequest();
        long startTime = System.currentTimeMillis();
        callWilmaWithPostMethod(requestParameters);
        long estimatedTime = System.currentTimeMillis() - startTime;
        assertTrue(estimatedTime > 5000);
    }

    private RequestParameters createRequest() throws Exception {
        request = tcRequest;
        setOriginalRequestMessageForStubConfig(request);
        setExpectedResponseMessageFromFile(tcResponse);
        return createRequestParameters();
    }

    private void uploadResources() throws Exception {
        uploadTemplateToWilma(XSL_FILE_NAME, XSL);
        uploadTemplateToWilma(TEMPLATE_FILE_NAME, TEMPLATE);
        uploadStubConfigToWilma(tcStubConfig);
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
                .xmlIS(new FileInputStream(request)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

}
