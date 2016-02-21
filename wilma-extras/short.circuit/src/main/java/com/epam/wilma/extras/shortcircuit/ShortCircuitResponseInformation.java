package com.epam.wilma.extras.shortcircuit;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.domain.http.WilmaHttpResponse;

import java.util.Map;
import java.util.Set;

/**
 * This class holds a single response information, to be used in stub response, and captured from a real response.
 * @author tkohegyi, created on 2016. 02. 17
 */
public class ShortCircuitResponseInformation {

    private WilmaHttpResponse wilmaHttpResponse;
    private long timeout;

    /**
     * Creates a new response information, based on the original response, and specifying a timeout.
     * Timeout value is the system time, when this response become obsolete.
     *
     * @param wilmaHttpResponse is the original response object
     * @param timeout is the calculated timeout value
     */
    public ShortCircuitResponseInformation(WilmaHttpResponse wilmaHttpResponse, long timeout) {
        //need to clone the response perfectly
        WilmaHttpResponse storedResponse = new WilmaHttpResponse(false);
        storedResponse.setContentType(wilmaHttpResponse.getContentType());
        storedResponse.setStatusCode(wilmaHttpResponse.getStatusCode());
        storedResponse.setBody(wilmaHttpResponse.getBody());
        Map<String, String> map = wilmaHttpResponse.getHeaders();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            wilmaHttpResponse.addHeader(key, map.get(key));
        }
        this.wilmaHttpResponse = storedResponse;
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public WilmaHttpResponse getWilmaHttpResponse() {
        return wilmaHttpResponse;
    }
}
