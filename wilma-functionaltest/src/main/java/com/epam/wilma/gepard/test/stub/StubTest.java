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
import com.epam.wilma.gepard.testclient.ResponseHolder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Tests wilma's stub config.
 *
 * @author Tunde_Kovacs
 */
@TestClass(id = "Stub", name = "Stub")
public class StubTest extends WilmaTestCase {

    private static final String DIALOG_DESCRIPTOR = "dialog-descriptor1";
    private static final String XSL_FILE_NAME = "exampleBase.xsl";
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
    @TestParameter(id = "PAR8")
    private String tcResponseCode;
    @TestParameter(id = "PAR9")
    private String tcXsl;

    public void testStubWhenRequestIsExampleShouldReturnCorrectResponse() throws Exception {
        setOperationModeTo("wilma");
        uploadResources();
        RequestParameters requestParameters = createRequest();
        ResponseHolder actual = callWilmaWithPostMethod(requestParameters);
        checkXml(actual.getResponseMessage());
    }

    public void testStubResponseCode() throws Exception {
        setOperationModeTo("wilma");
        uploadResources();
        RequestParameters requestParameters = createRequest();
        callWilmaWithPostMethod(requestParameters);
        checkResponseCode(Integer.valueOf(tcResponseCode));
    }

    public void testStubDialogDescriptor() throws Exception {
        setOperationModeTo("wilma");
        uploadResources();
        RequestParameters requestParameters = createRequest();
        callWilmaWithPostMethod(requestParameters);
        checkResponseDialogDescriptor(DIALOG_DESCRIPTOR);
    }

    private RequestParameters createRequest() throws Exception {
        request = tcRequest;
        setOriginalRequestMessageForStubConfig(request);
        setExpectedResponseMessageFromFile(tcResponse);
        return createRequestParameters();
    }

    private void uploadResources() throws Exception {
        uploadTemplateToWilma(TEMPLATE_FILE_NAME, TEMPLATE);
        uploadTemplateToWilma(XSL_FILE_NAME, tcXsl);
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
