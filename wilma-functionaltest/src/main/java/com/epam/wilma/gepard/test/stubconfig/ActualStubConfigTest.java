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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.epam.gepard.annotations.TestClass;

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import org.junit.Test;

/**
 * Checks if stub config at start is the same as at run time.
 * @author Tunde_Kovacs
 *
 */
@TestClass(id = "ActualStubConfig", name = "Stub config")
public class ActualStubConfigTest extends WilmaTestCase {

    private static final String EXAMPLE_2 = "resources/example2.xml";
    private static final String STUB_CONFIG = "resources/actualStubConfig.json";

    @Test
    public void testStubConfig() throws Exception {
        uploadStubConfigToWilma(STUB_CONFIG);
        setExpectedResponseMessageFromFile("resources/ActualStubConfigTestResource.json");
        RequestParameters requestParameters = createRequestParameters();
        callWilmaWithPostMethodAndAssertResponse(requestParameters);
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaInternalUrl() + "config/public/stub/stubconfig.json?groupname=Default";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "xml";
        String acceptHeader = "json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_2)).contentType(contentType).acceptHeader(acceptHeader)
                .contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

}
