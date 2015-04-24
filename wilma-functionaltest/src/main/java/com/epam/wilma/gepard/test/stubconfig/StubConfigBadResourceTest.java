package com.epam.wilma.gepard.test.stubconfig;
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

import java.io.FileNotFoundException;

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.annotations.TestParameter;

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;

/**
 * Test resources which are expected to cause errors when uploaded.
 *
 * @author Adam_Csaba_Kiraly
 *
 */
@TestClass(id = "StubConfigBadResource", name = "Stub config")
public class StubConfigBadResourceTest extends WilmaTestCase {

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
    private String tcExternalResourceUrl;
    @TestParameter(id = "PAR6")
    private String tcResource;
    @TestParameter(id = "PAR7")
    private String tcResponse;

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
        String wilmaHost = getClassData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getClassData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + tcContentType;
        String acceptHeader = "application/" + tcAcceptHeader;
        String contentEncoding = tcContentEncoding;
        String acceptEncoding = tcAcceptEncoding;
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

}
