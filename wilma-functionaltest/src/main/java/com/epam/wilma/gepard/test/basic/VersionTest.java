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

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Provides functional test for checking the version of wilma.
 *
 * @author Tunde_Kovacs
 */
@TestClass(id = "SUT Version", name = "Wilma version test - intro")
public class VersionTest extends WilmaTestCase {
    private static final String EXAMPLE_2 = "resources/example2.xml";

    @Test
    public void testWilmaVersion() throws Exception {
        //given
        RequestParameters requestParameters = createRequestParameters();
        //when
        ResponseHolder responseVersion = callWilmaWithGetMethod(requestParameters); //send request, receive response and check if expected result test is in the result
        //then
        String version = getVersionFromJson(responseVersion.getResponseMessage());
        AllTestRunner.setSystemUnderTestVersion(version);
        logComment("Version = " + version);
    }

    private String getVersionFromJson(final String version) {
        int positionOfColon = version.lastIndexOf(":");
        return version.substring(positionOfColon + 2, version.length() - 2);
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaVersionInfoUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "xml";
        String acceptHeader = "xml";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_2)).contentType(contentType).acceptHeader(acceptHeader)
                .contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

}
