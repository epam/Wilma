package com.epam.wilma.gepard;

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

import com.epam.wilma.gepard.test.helper.WilmaConfigurationHelperDecorator;
import com.epam.wilma.gepard.testclient.MultiStubRequestParameters;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import com.epam.wilma.gepard.testclient.TestClientBootstrap;

/**
 * This class represents a TestCase, which supports HTML logs, and beforeTestCaseSet
 * and afterTestCaseSet event.
 *
 * @author Tamas Kohegyi
 */
public abstract class WilmaTestCase extends WilmaConfigurationHelperDecorator {

    /**
     * Sends a POST request to Wilma.
     *
     * @param requestParameters is the request
     * @return with the response
     * @throws Exception in case of error
     */
    protected ResponseHolder callWilmaWithPostMethod(final RequestParameters requestParameters) throws Exception {
        return new TestClientBootstrap().bootstrap(this, requestParameters);
    }

    /**
     * Run Wilma Tests those tests multiple stub configurations. For this purpose special request parameters are used.
     *
     * @param requestParameters for multi-stub config behavior
     * @return with the response
     * @throws Exception in case of any error
     */
    protected ResponseHolder callWilmaWithPostMethod(final MultiStubRequestParameters requestParameters) throws Exception {
        return new TestClientBootstrap().bootstrap(this, requestParameters);
    }

    /**
     * Sends a GET request to Wilma.
     *
     * @param requestParameters is the request
     * @return with the response
     * @throws Exception in case of error
     */
    protected ResponseHolder callWilmaWithGetMethod(final RequestParameters requestParameters) throws Exception {
        return new TestClientBootstrap().bootstrapGet(this, requestParameters);
    }

    /**
     * Sends a POST request to Wilma, and checks if the response message withs to the expected one.
     *
     * @param requestParameters is the request
     * @throws Exception in case of problem
     */
    protected void callWilmaWithPostMethodAndAssertResponse(final RequestParameters requestParameters) throws Exception {
        ResponseHolder actual = callWilmaWithPostMethod(requestParameters);
        assertExpectedResultMessage(actual.getResponseMessage());
    }

    /**
     * Gets the Wilma Test Server Url.
     *
     * @return with URL of the test server.
     */
    protected String getWilmaTestServerUrl() {
        return String.format("http://%s:%s/example", getClassData().getEnvironment().getProperty("wilma.test.server.host"),
                getClassData().getEnvironment().getProperty("wilma.test.server.port"));
    }

    /**
     * Gets the Wilma Test SSL Server Url.
     *
     * @return with URL of the ssl test server.
     */
    protected String getWilmaSSLTestServerUrl() {
        return String.format("https://%s:%s/example", getClassData().getEnvironment().getProperty("wilma.test.server.host"),
                getClassData().getEnvironment().getProperty("wilma.test.server.ssl.port"));
    }

    protected String getWilmaTestServerUrlBase() {
        return String.format("http://%s:%s/", getClassData().getEnvironment().getProperty("wilma.test.server.host"),
                this.getClassData().getEnvironment().getProperty("wilma.test.server.port"));
    }


}
