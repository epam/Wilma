package com.epam.wilma.gepard.testclient;
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

import com.epam.wilma.gepard.WilmaTestCase;

/**
 * Bootstraps the client application by loading the properties file, and calling the business logic.
 *
 * @author Marton_Sereg
 */
public class TestClientBootstrap {

    /**
     * Bootstraps the client application by loading the properties file, and calling the business logic - POST method.
     *
     * @param tc                is the caller test case
     * @param requestParameters is the parameter set of the request
     * @throws Exception if something goes wrong
     * @return with the Response info
     */
    public ResponseHolder bootstrap(final WilmaTestCase tc, final RequestParameters requestParameters) throws Exception {
        ResponseHolder response;
        try {
            HttpPostRequestSender httpRequestSender = new HttpPostRequestSender();
            response = httpRequestSender.callWilmaTestServer(tc, requestParameters);
        } catch (NumberFormatException e) {
            throw new Exception("wilma.port property cannot be read. " + e.getMessage());
        }
        return response;
    }

    /**
     * Bootstraps the client application by loading the properties file, and calling the business logic - POST method.
     *
     * @param tc                is the caller test case
     * @param requestParameters is the parameter set of the request
     * @throws Exception if something goes wrong
     * @return with the Response info
     */
    public ResponseHolder bootstrap(final WilmaTestCase tc, final MultiStubRequestParameters requestParameters) throws Exception {
        ResponseHolder response;
        try {
            MultiStubHttpPostRequestSender httpRequestSender = new MultiStubHttpPostRequestSender();
            response = httpRequestSender.callWilmaTestServer(tc, requestParameters);
        } catch (NumberFormatException e) {
            throw new Exception("wilma.port property cannot be read. " + e.getMessage());
        }
        return response;
    }

    /**
     * Bootstraps the client application by loading the properties file, and calling the business logic - GET Method.
     *
     * @param tc                is the caller test case
     * @param requestParameters is the parameter set of the request
     * @throws Exception if something goes wrong
     * @return with the Response info
     */
    public ResponseHolder bootstrapGet(final WilmaTestCase tc, final RequestParameters requestParameters) throws Exception {
        ResponseHolder response;
        try {
            HttpGetRequestSender httpRequestSender = new HttpGetRequestSender();
            response = httpRequestSender.callWilmaTestServer(tc, requestParameters);
        } catch (NumberFormatException e) {
            throw new Exception("wilma.port property cannot be read. " + e.getMessage());
        }
        return response;
    }

}
