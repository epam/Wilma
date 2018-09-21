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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.epam.gepard.annotations.TestClass;

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import org.junit.Test;

/**
 * Tests for Test Server error cases, i.e. if Wilma can handle incoming Exxx messages.
 * @author Tamas_Bihari
 *
 */
@TestClass(id = "Server error cases", name = "Test")
public class ServerErrorTest extends WilmaTestCase {
    private static final String BYPASS = "WilmaBypass=true";
    private static final String AVAIL_SUMMARY_REQUEST_1_XML = "resources/dummy_file.txt";

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcRequestedErrorCode = getDataDrivenTestParameter("PAR1");
    private String tcRequestedUrl = getDataDrivenTestParameter("PAR2");

    /**
     * Test if Wilma can properly handle incoming Exxx error messages from the server.
     *
     * @throws Exception in case of error
     */
    @Test
    public void testServerError() throws Exception {
        RequestParameters requestParameters = createRequestParameters();
        callWilmaWithGetMethod(requestParameters);
        checkResponseCode(Integer.valueOf(tcRequestedErrorCode));
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrlBase() + tcRequestedUrl;
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain";
        String acceptHeader = "";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(AVAIL_SUMMARY_REQUEST_1_XML)).contentType(contentType).acceptHeader(acceptHeader)
                .contentEncoding(contentEncoding).acceptEncoding(acceptEncoding).specialHeader(BYPASS);
    }

}
