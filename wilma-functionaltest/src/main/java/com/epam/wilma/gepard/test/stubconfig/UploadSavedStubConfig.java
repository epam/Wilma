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
import com.epam.wilma.gepard.testclient.MultiStubRequestParameters;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Uploads the saved stub configuration back to wilma in order to keep the consistency of the
 * application.
 *
 * @author Tunde_Kovacs, tkohegyi
 */
@TestClass(id = "StubConfig", name = "Upload saved stub configuration")
public class UploadSavedStubConfig extends WilmaTestCase {

    private static final String STUB_CONFIG = "resources/savedStubConfig.json";
    private static final int ACCEPTED = 200;
    private static final int ALMOST_ACCEPTED = 500;

    /**
     * Clears all the existing stub configurations of Wilma.
     *
     * @throws Exception in case of an error.
     */
    @Test
    public void testClearAllStubConfigAndUpload() throws Exception {
        //clear all stubconfig
        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder responseVersion = callWilmaWithPostMethod(requestParameters); //Get the actual DialogDescriptors
        String answer = responseVersion.getResponseMessage();
        for (String groupName : getGroupNamesFromJson(answer)) {
            MultiStubRequestParameters multiStubRequestParameters = createMultiStubRequestParameters(groupName);
            callWilmaWithPostMethod(multiStubRequestParameters); //Delete the uploaded stub configuration
            logComment(groupName + "'s config has been dropped.");
        }
        //upload preserved stubconfig
        uploadStubConfigToWilmaAndAllowEmptyStuvConfig(STUB_CONFIG);
        //if we are here, then the stub config is restored, so we can delete it
        Path path = FileSystems.getDefault().getPath(STUB_CONFIG);
        try {
            Files.deleteIfExists(path);
        } catch (FileSystemException e) {
            logComment("Ups, cannot delete the file, reason: " + e.getLocalizedMessage());
        }
    }

    private List<String> getGroupNamesFromJson(final String response) throws Exception {
        List<String> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readValue(response, JsonNode.class);
        JsonNode configs = actualObj.path("configs");
        Iterator<JsonNode> iterator = configs.getElements();
        while (iterator.hasNext()) {
            result.add(iterator.next().path("groupname").getTextValue());
        }
        return result;
    }

    /**
     * Note: maybe get request would be better for this.
     *
     * @return with the prepared request parameters
     * @throws FileNotFoundException in case of problem
     */
    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaStubConfigDescriptorsUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream("resources/DropableStubConfig.json")).contentType(contentType).acceptHeader(acceptHeader)
                .contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

    /**
     * Prepare request of deleting a specific stub configuration group.
     *
     * @param groupName is the name of the stub configuration that should be deleted
     * @return with the prepared request parameters
     * @throws FileNotFoundException in case of error
     */
    protected MultiStubRequestParameters createMultiStubRequestParameters(final String groupName) throws FileNotFoundException {
        String testServerUrl = getWilmaDropStubConfigUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(groupName);
    }

    /**
     * Upload Stub Configuration file to Wilma, also accept uploading empty stub config.
     *
     * @param stubConfig is the file
     * @throws Exception if any problem occurs
     */
    public void uploadStubConfigToWilmaAndAllowEmptyStuvConfig(final String stubConfig) throws Exception {
        logStep("Upload Stub Configuration to Wilma.");
        String url = getWilmaInternalUrl() + STUB_CONFIG_URL;
        boolean loaded = false;

        ResponseData responseData = uploadResourceWithExpectedError(url, stubConfig);

        if (ACCEPTED == responseData.getStatusCode()) {
            logComment("Preserved StubConfig loaded back.");
            loaded = true;
        }
        if (ALMOST_ACCEPTED == responseData.getStatusCode() && responseData.getMessage().startsWith("Please provide a non-empty stub descriptor!")) {
            logComment("Preserved EMPTY StubConfig loaded back.");
            loaded = true;
        }

        if (!loaded) {
            throw new Exception("Cannot upload resource " + stubConfig + " to " + url);
        }

    }

}
