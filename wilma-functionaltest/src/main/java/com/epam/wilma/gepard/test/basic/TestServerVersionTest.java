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

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Provides functional test for checking the version of wilma test server.
 *
 * @author Tamas_Kohegyi
 */
@TestClass(id = "SUT Version", name = "Wilma Test Server version test")
public class TestServerVersionTest extends WilmaTestCase {

    @Test
    public void testTestServerVersion() throws Exception {
        //given
        clearAllOldStubConfigs();
        setLocalhostBlockingTo("off");
        setOperationModeTo("wilma");
        RequestParameters requestParameters = createRequestParameters();
        //when
        ResponseHolder responseVersion = callWilmaWithGetMethod(requestParameters); //send request, receive response and check if expected result test is in the result
        //then
        String version = responseVersion.getResponseMessage();
        logComment("Version = " + version);
        Assert.assertTrue("Response code from Test Server is not acceptable: " + responseVersion.getResponseCode(),
                responseVersion.getResponseCode().startsWith("status code: 200"));
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrlBase() + "ok";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String acceptHeader = "text/plain";
        String contentEncoding = "";
        String acceptEncoding = "gzip";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .acceptHeader(acceptHeader).acceptEncoding(acceptEncoding).contentEncoding(contentEncoding);
    }

}
