package com.epam.wilma.gepard.test.altermessage;
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
 * Enables/disables response message volatility switch.
 * @author Tamas Kohegyi
 *
 */
public class ResponseMessageVolatilitySwitch {

    private static final int STATUS_OK = 200;
    private static final String URL_TEMPLATE = "%sconfig/admin/responsevolatility/%s";

    /**
     * Set response message volatility to on/off.
     *
     * @param tc is the caller Test Case
     * @param host Wilma host:port string
     * @param state is the expected state of the switch
     * @return with response code
     * @throws Exception in case of error
     */
    public String setResponseMessageVolatilityTo(final WilmaTestLogDecorator tc, final String host, final String state) throws Exception {
        String url = String.format(URL_TEMPLATE, host, state);
        tc.logStep("Switching response message volatility in Wilma to: " + url.split("responsevolatility/")[1]);
        tc.logGetRequestEvent(url);

        HttpClient httpClient = new HttpClient();
        GetMethod httpGet = new GetMethod(url);
        int statusCode = httpClient.executeMethod(httpGet);
        String responseCode = "status code: " + statusCode + "\n";
        if (statusCode != STATUS_OK) {
            tc.naTestCase("Response message volatility switch failed.");
        }
        tc.logComment("Response message volatility is: " + url.split("responsevolatility/")[1]);
        return responseCode;
    }
}
