package com.epam.wilma.gepard.test.operation;
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

import com.epam.wilma.gepard.test.helper.WilmaTestLogDecorator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Switches the operation mode on wilma.
 *
 * @author Tunde_Kovacs
 */
public class OperationModeSwitch {

    private static final int STATUS_OK = 200;

    /**
     * Set Wilma operation mode (Wilma/stub/proxy).
     * @param tc is the caller Test Case
     * @param url to Wilma
     * @return with the response code
     * @throws Exception in case error happens
     */
    public String switchOperationMode(final WilmaTestLogDecorator tc, final String url) throws Exception {
        tc.logStep("Switching operation mode in Wilma to: " + url.split("switch/")[1]);
        tc.logGetRequestEvent(url);

        HttpClient httpClient = new HttpClient();
        GetMethod httpGet = new GetMethod(url);
        int statusCode = httpClient.executeMethod(httpGet);
        String responseCode = "status code: " + statusCode + "\n";
        if (statusCode != STATUS_OK) {
            tc.naTestCase("Operation mode switch failed.");
        }
        tc.logComment("Operation mode is: " + url.split("switch/")[1]);
        return responseCode;
    }
}
