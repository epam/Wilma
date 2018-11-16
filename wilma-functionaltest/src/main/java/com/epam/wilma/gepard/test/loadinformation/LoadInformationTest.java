package com.epam.wilma.gepard.test.loadinformation;

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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * Provides functional test for checking the loadInformation of wilma.
 *
 * @author Tibor_Kovacs
 */
@TestClass(id = "LoadInfo", name = "Wilma load information checking")
public class LoadInformationTest extends WilmaTestCase {

    private static final String EXAMPLE_2_XML = "resources/example2.xml";

    @Test
    public void testLoadInformation() throws Exception {
        setLocalhostBlockingTo("off");
        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder responseVersion = callWilmaWithGetMethod(requestParameters); //send request, receive response and check if expected result test is in the result
        String answer = responseVersion.getResponseMessage();

        long actualLoad = getActualLoadFromJson(answer);
        logComment("ActualLoad = " + actualLoad);
        int actualDeletedFilesCount = getDeletedFileFromJson(answer);
        logComment("DeletedFilesCount = " + actualDeletedFilesCount);
        int actualCountOfMessages = getCountOfMessagesFromJson(answer);
        logComment("CountOfMessages = " + actualCountOfMessages);

        assertTrue(actualLoad >= 0);
        assertTrue(actualDeletedFilesCount >= 0);
        assertTrue(actualCountOfMessages >= 0);
    }

    private long getActualLoadFromJson(final String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readValue(response, JsonNode.class);
        return actualObj.path("actualLoad").getLongValue();
    }

    private int getDeletedFileFromJson(final String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readValue(response, JsonNode.class);
        return actualObj.path("deletedFilesCount").getIntValue();
    }

    private int getCountOfMessagesFromJson(final String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readValue(response, JsonNode.class);
        return actualObj.path("countOfMessages").getIntValue();
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaInternalUrl() + "config/public/actualload";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_2_XML)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }
}
