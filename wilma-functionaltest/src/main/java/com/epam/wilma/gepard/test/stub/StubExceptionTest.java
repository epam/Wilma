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
 * Tests wilma's stub config.
 *
 * @author Tunde_Kovacs
 */
@TestClass(id = "Stub", name = "Stub with exceptions")
public class StubExceptionTest extends WilmaTestCase {

    private String request;

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcContentType = getDataDrivenTestParameter("PAR1");
    private String tcAcceptHeader = getDataDrivenTestParameter("PAR2");
    private String tcContentEncoding = getDataDrivenTestParameter("PAR3");
    private String tcAcceptEncoding = getDataDrivenTestParameter("PAR4");
    private String tcStubConfig = getDataDrivenTestParameter("PAR5");
    private String tcRequest = getDataDrivenTestParameter("PAR6");
    private String tcResponse = getDataDrivenTestParameter("PAR7");
    private String tcResource = getDataDrivenTestParameter("PAR8");
    private String tcResourceUrl = getDataDrivenTestParameter("PAR9");

    @Test
    public void testStubWithErrorDuringConditionChecking() throws Exception {
        uploadResources();
        RequestParameters requestParameters = createRequest();
        setExpectedResponseMessageFromFile(tcResponse);
        callWilmaWithPostMethodAndAssertResponse(requestParameters);
    }

    private RequestParameters createRequest() throws Exception {
        request = tcRequest;
        setOriginalRequestMessageForStubConfig(request);
        return createRequestParameters();
    }

    private void uploadResources() throws Exception {
        uploadResource(getWilmaInternalUrl() + tcResourceUrl, tcResource);
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
