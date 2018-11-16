package com.epam.wilma.gepard.test.stubconfig.multi;
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
import com.epam.wilma.gepard.testclient.MultiStubRequestParameters;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class upload a new stub config then call that service which will drop the uploaded config.
 *
 * @author Tibor_Kovacs
 */
@TestClass(id = "Multi_StubConfig", name = "Drop the selected stub configuration")
public class DropStubConfigTest extends WilmaTestCase {

    private static final String STUB_CONFIG = "resources/DropableStubConfig.json";
    private static final String STUB_GROUP_NAME = "Droppable";

    @Test
    public void testDropStubConfigTest() throws Exception {
        RequestParameters requestParameters = createRequestParameters();
        //Remove possible existing group: "Droppable"
        MultiStubRequestParameters multiStubRequestParameters = createMultiStubRequestParameters();
        callWilmaWithPostMethod(multiStubRequestParameters); //Delete the uploaded stub configuration

        //Get the actual DialogDescriptors
        ResponseHolder responseVersion = callWilmaWithPostMethod(requestParameters); //Get the actual DialogDescriptors
        String expectedAnswer = responseVersion.getResponseMessage();

        //upload a new config into Default group
        uploadStubConfigToWilma(STUB_CONFIG); //Upload a new stub configuration
        logComment("Test config has uploaded!");

        //check that it is uploaded
        requestParameters = createRequestParameters();
        responseVersion = callWilmaWithPostMethod(requestParameters); //Get the actual DialogDescriptors again
        String resultAnswer = responseVersion.getResponseMessage();

        Assert.assertNotEquals(expectedAnswer, resultAnswer);

        //remove the config
        multiStubRequestParameters = createMultiStubRequestParameters();
        callWilmaWithPostMethod(multiStubRequestParameters); //Delete the uploaded stub configuration

        //reread the config
        requestParameters = createRequestParameters();
        responseVersion = callWilmaWithPostMethod(requestParameters); //Get the actual DialogDescriptors again
        resultAnswer = responseVersion.getResponseMessage();

        Assert.assertEquals(expectedAnswer, resultAnswer);
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaStubConfigDescriptorsUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

    protected MultiStubRequestParameters createMultiStubRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaDropStubConfigUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(STUB_GROUP_NAME);
    }
}
