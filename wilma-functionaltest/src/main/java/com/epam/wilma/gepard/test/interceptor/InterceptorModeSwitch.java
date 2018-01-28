package com.epam.wilma.gepard.test.interceptor;
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

import com.epam.gepard.generic.GepardTestClass;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Switches the operation mode on Wilma.
 *
 * @author Tamas Kohegyi
 */
public class InterceptorModeSwitch {

    /**
     * Set interceptor handling mode on/off, based on parameter.
     *
     * @param tc is the caller Test Case
     * @param url to Wilma instance
     * @return with the response code
     * @throws Exception in case of problem
     */
    public String switchInterceptorMode(final GepardTestClass tc, final String url) throws Exception {
        String responseCode;
        HttpClient httpClient = new HttpClient();
        GetMethod httpGet = new GetMethod(url);
        int statusCode;
        statusCode = httpClient.executeMethod(httpGet);
        String onOrOff = url.substring(url.lastIndexOf('/') + 1);
        if (tc != null) {
            tc.logStep("Switching interceptor usage mode in Wilma to " + onOrOff);
        }
        responseCode = "status code: " + statusCode + "\n";
        return responseCode;
    }
}
