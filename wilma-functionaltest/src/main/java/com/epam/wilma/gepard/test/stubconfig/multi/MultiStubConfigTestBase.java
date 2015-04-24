package com.epam.wilma.gepard.test.stubconfig.multi;
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

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.MultiStubRequestParameters;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains the basic functionality for tests of multiple stub configuration uses.
 *
 * @author Tibor_Kovacs
 */
public abstract class MultiStubConfigTestBase extends WilmaTestCase {

    private static final String STUB_CONFIG_FIRST = "resources/enabledisable/stubConfigFirst.xml";

    /**
     * As the method name says, it will clear all existing Stub Configuration groups from Wilma.
     *
     * @throws Exception in case problem occurs.
     */
    public void clearAllOldStubConfigs() throws Exception {
        RequestParameters requestParameters = createRequestParametersToGetAllStubDescriptors();
        ResponseHolder responseVersion = callWilmaWithPostMethod(requestParameters);
        String answer = responseVersion.getResponseMessage();
        for (String groupname : getGroupNamesFromJson(answer)) {
            MultiStubRequestParameters multiStubRequestParameters = createDropRequestParameters(groupname);
            callWilmaWithPostMethod(multiStubRequestParameters);
            logComment(groupname + "'s config has been dropped.");
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
     * Prepare request parameters to get info on all active stub descriptors of Wilma.
     * Note: maybe a Get method would be better.
     *
     * @return with the prepared request parameters.
     * @throws FileNotFoundException in case error occurs.
     */
    protected RequestParameters createRequestParametersToGetAllStubDescriptors() throws FileNotFoundException {
        String testServerUrl = getWilmaStubConfigDescriptorsUrl();
        String wilmaHost = getClassData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getClassData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .xmlIS(new FileInputStream(STUB_CONFIG_FIRST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

    /**
     * Prepare request parameters to drop the actual stub descriptors.
     * Note: maybe a Get method would be better.
     *
     * @param groupname the name of the group to be dropped.
     * @return with request parameters in order to drop the specified stub descriptor group.
     * @throws FileNotFoundException in case error occurs
     */
    protected MultiStubRequestParameters createDropRequestParameters(final String groupname) throws FileNotFoundException {
        String testServerUrl = getWilmaDropStubConfigUrl();
        String wilmaHost = getClassData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getClassData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .xmlIS(new FileInputStream(STUB_CONFIG_FIRST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(groupname);
    }
}
