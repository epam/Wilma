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
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Test;

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

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcContentType = getDataDrivenTestParameter("PAR1");
    private String tcAcceptHeader = getDataDrivenTestParameter("PAR2");
    private String tcContentEncoding = getDataDrivenTestParameter("PAR3");
    private String tcAcceptEncoding = getDataDrivenTestParameter("PAR4");
    private String tcStubConfig = getDataDrivenTestParameter("PAR5");
    private String tcRequest = getDataDrivenTestParameter("PAR6");
    private String tcResponse = getDataDrivenTestParameter("PAR7");
    private String tcResponseCode = getDataDrivenTestParameter("PAR8");
    private String tcXsl = getDataDrivenTestParameter("PAR9");

    @Test
    public void testStubWhenRequestIsExampleShouldReturnCorrectResponse() throws Exception {
        setOperationModeTo("wilma");
        uploadResources();
        RequestParameters requestParameters = createRequest();
        ResponseHolder actual = callWilmaWithPostMethod(requestParameters);
        checkXml(actual.getResponseMessage());
    }

    @Test
    public void testStubResponseCode() throws Exception {
        setOperationModeTo("wilma");
        uploadResources();
        RequestParameters requestParameters = createRequest();
        callWilmaWithPostMethod(requestParameters);
        checkResponseCode(Integer.valueOf(tcResponseCode));
    }

    @Test
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
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + tcContentType;
        String acceptHeader = "application/" + tcAcceptHeader;
        String contentEncoding = tcContentEncoding;
        String acceptEncoding = tcAcceptEncoding;
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(request)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

}
