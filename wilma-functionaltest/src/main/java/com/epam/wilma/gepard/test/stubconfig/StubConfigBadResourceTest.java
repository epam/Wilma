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

import java.io.FileNotFoundException;

import com.epam.gepard.annotations.TestClass;

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import org.junit.Test;

/**
 * Test resources which are expected to cause errors when uploaded.
 *
 * @author Adam_Csaba_Kiraly
 *
 */
@TestClass(id = "StubConfigBadResource", name = "Stub config")
public class StubConfigBadResourceTest extends WilmaTestCase {

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcContentType = getDataDrivenTestParameter("PAR1");
    private String tcAcceptHeader = getDataDrivenTestParameter("PAR2");
    private String tcContentEncoding = getDataDrivenTestParameter("PAR3");
    private String tcAcceptEncoding = getDataDrivenTestParameter("PAR4");
    private String tcExternalResourceUrl = getDataDrivenTestParameter("PAR5");
    private String tcResource = getDataDrivenTestParameter("PAR6");
    private String tcResponse = getDataDrivenTestParameter("PAR7");

    @Test
    public void testStubConfigBadResource() throws Exception {
        //given
        setExpectedResponseMessageFromFile(tcResponse);
        //when
        ResponseData responseData = uploadResourceWithExpectedError(getWilmaInternalUrl() + tcExternalResourceUrl, tcResource);
        //then
        String result = responseData.getMessage();
        assertExpectedResultMessage(result);
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
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

}
