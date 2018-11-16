package com.epam.wilma.gepard.test.stubconfig;
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
 * Tests wilma's stub config - uploading and handling external Jars.
 */
@TestClass(id = "StubConfigJarResource", name = "Stub config")
public class StubConfigJarResourceTest extends WilmaTestCase {

    private String stubConfigXml;

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcContentType = getDataDrivenTestParameter("PAR1");
    private String tcAcceptHeader = getDataDrivenTestParameter("PAR2");
    private String tcContentEncoding = getDataDrivenTestParameter("PAR3");
    private String tcAcceptEncoding = getDataDrivenTestParameter("PAR4");
    private String tcExternalResourceFileName = getDataDrivenTestParameter("PAR5");
    private String tcRequest = getDataDrivenTestParameter("PAR6");
    private String tcResource = getDataDrivenTestParameter("PAR7");
    private String tcResponse = getDataDrivenTestParameter("PAR8");
    private String tcExpectedResponseCode = getDataDrivenTestParameter("PAR9");

    @Test
    public void testStubConfigJarResource() throws Exception {
        uploadJarToWilma(tcExternalResourceFileName, tcResource);
        stubConfigXml = tcRequest;
        setOriginalRequestMessageFromFile(stubConfigXml);
        setExpectedResponseMessageFromFile(tcResponse);
        RequestParameters requestParameters = createRequestParameters();
        callWilmaWithPostMethodAndAssertResponse(requestParameters);
        checkResponseCode(Integer.valueOf(tcExpectedResponseCode));
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaInternalUrl() + STUB_CONFIG_URL;
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + tcContentType;
        String acceptHeader = "application/" + tcAcceptHeader;
        String contentEncoding = tcContentEncoding;
        String acceptEncoding = tcAcceptEncoding;
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(stubConfigXml)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

}
