package com.epam.wilma.gepard.test.stubconfig.multi.order;
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
 * This class upload two new stub configurations then moves the first configuration to down once then checks if the second configuration will answer.
 *
 * @author Tibor_Kovacs
 */
@TestClass(id = "Multi_StubConfig", name = "Moves the first configuration to down once")
public class MovedTheFirstConfigToDownTest extends WilmaTestCase {

    private static final String STUB_CONFIG_FIRST_GROUP_NAME = "testFirst";
    private static final String STUB_CONFIG_SECOND_GROUP_NAME = "testSecond";
    private static final String STUB_CONFIG_FIRST = "resources/enabledisable/stubConfigFirst.json";
    private static final String STUB_CONFIG_SECOND = "resources/enabledisable/stubConfigSecond.json";

    @Test
    public void testChangeTheOrderOneTimes() throws Exception {
        clearAllOldStubConfigs();
        uploadStubConfigToWilma(STUB_CONFIG_FIRST);
        uploadStubConfigToWilma(STUB_CONFIG_SECOND);

        MultiStubRequestParameters parameters = createChangeStatusRequestParameters("-1", STUB_CONFIG_FIRST_GROUP_NAME);
        callWilmaWithPostMethod(parameters);

        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder response = callWilmaWithPostMethod(requestParameters);
        String resultAnswer = response.getResponseMessage();

        String expected = STUB_CONFIG_SECOND_GROUP_NAME;
        Assert.assertEquals(expected, resultAnswer);
    }

    protected MultiStubRequestParameters createChangeStatusRequestParameters(final String direction, final String groupname) throws FileNotFoundException {
        String testServerUrl = getWilmaChangeStubConfigOrderUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG_FIRST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(groupname).direction(direction);
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaStubConfigDescriptorsUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG_FIRST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }
}
