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
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class sends a request to that servlet which is listening at /stub/ an expects an error response with 503 code.
 *
 * @author Tibor_Kovacs
 */
@TestClass(id = "Stub", name = "Error handling when Wilma gets a request onto /stub/* mapping")
public class WilmaStubDeclineTest extends WilmaTestCase {

    private static final String EXAMPLE_2 = "resources/example2.xml";

    @Test
    public void testGetTheExpectedErrorCodeAndMessage() throws Exception {
        String expectedErrorMessage = "Wilma has declined this request.";
        int expectedErrorCode = 503;
        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder responseHolder = callWilmaWithGetMethod(requestParameters);
        String resultErrorMessage = responseHolder.getResponseMessage();
        int resultErrorCode = responseHolder.getHttpGet().getStatusCode();

        Assert.assertEquals(expectedErrorMessage, resultErrorMessage);
        Assert.assertEquals(expectedErrorCode, resultErrorCode);
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaInternalUrl() + "stub/";
        Boolean useProxy = false;
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "xml";
        String acceptHeader = "xml";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(useProxy).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_2)).contentType(contentType).acceptHeader(acceptHeader)
                .contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }
}
