package com.epam.wilma.gepard.test.stubconfig.multi.status;
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
 * This class checks the /config/stub/changestatus servlet with wrong parameter in nextstatus.
 *
 * @author Tibor_Kovacs
 */
@TestClass(id = "Multi_StubConfig", name = "Checks the error message of response in case of wrong format in nextstatus parameter")
public class StatusErrorResponseCheckerTest extends WilmaTestCase {
    private static final String STUB_CONFIG_FIRST_GROUP_NAME = "testFirst";
    private static final String STUB_CONFIG_FIRST = "resources/enabledisable/stubConfigFirst.json";
    private static final String ERROR_MSG = "Wrong format of direction parameter. \"nextstatus\" must be a boolean!";

    @Test
    public void testErrorResponseFromChangeStatusServlet() throws Exception {
        MultiStubRequestParameters parameters = createChangeStatusRequestParameters("falseeeeee", STUB_CONFIG_FIRST_GROUP_NAME);
        ResponseHolder response = callWilmaWithPostMethod(parameters);
        String resultAnswer = response.getResponseMessage();

        Assert.assertEquals(ERROR_MSG, resultAnswer);
    }

    protected MultiStubRequestParameters createChangeStatusRequestParameters(final String status, final String groupname) throws FileNotFoundException {
        String testServerUrl = getWilmaChangeStubConfigStatusUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG_FIRST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(groupname).status(status);
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
