package com.epam.wilma.router.helper;
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
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import org.springframework.stereotype.Component;

import java.util.Map.Entry;

/**
 * Copies a {@link WilmaHttpRequest}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class WilmaHttpRequestCloner {

    /**
     * Copies a {@link WilmaHttpRequest}'s headers and body to new instance.
     * @param request the original request
     * @return the new instance
     */
    public WilmaHttpRequest cloneRequest(final WilmaHttpRequest request) {
        WilmaHttpRequest result = new WilmaHttpRequest();
        result.setRequestLine(request.getRequestLine());
        result.setUri(request.getUri());
        result.setBody(request.getBody());
        result.setWilmaMessageId(request.getWilmaMessageId());
        copyHeaders(request, result);
        return result;
    }

    private void copyHeaders(final WilmaHttpRequest request, final WilmaHttpRequest result) {
        for (Entry<String, String> header : request.getHeaders().entrySet()) {
            result.addHeader(header.getKey(), header.getValue());
        }
        for (Entry<String, HttpHeaderChange> headerChanges : request.getHeaderChanges().entrySet()) {
            result.addHeaderChange(headerChanges.getKey(), headerChanges.getValue());
        }
    }
}
