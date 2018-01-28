package com.epam.wilma.domain.sequence;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * This class store a {@link WilmaHttpRequest} and a {@link WilmaHttpResponse}.
 * @author Tibor_Kovacs
 *
 */
public class RequestResponsePair {

    private WilmaHttpRequest request;
    private WilmaHttpResponse response;

    /**
     * Constructs a new instance of {@link RequestResponsePair}.
     * @param request is the given request.
     */
    public RequestResponsePair(final WilmaHttpRequest request) {
        this.request = request;
    }

    public WilmaHttpRequest getRequest() {
        return request;
    }

    public void setRequest(final WilmaHttpRequest request) {
        this.request = request;
    }

    public WilmaHttpResponse getResponse() {
        return response;
    }

    public void setResponse(final WilmaHttpResponse response) {
        this.response = response;
    }
}
