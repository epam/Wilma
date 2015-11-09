package com.epam.wilma.browsermob.transformer;
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

import com.epam.wilma.browsermob.transformer.helper.WilmaResponseFactory;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Executes transformations between a BrowserMob specific HTTP response and Wilma's own representation of an HTTP response.
 *
 * @author Tunde_Kovacs
 */
@Component
public class HttpResponseTransformer {

    @Autowired
    private WilmaResponseFactory responseFactory;

    /**
     * Transforms a BrowserMob specific HTTP response to Wilma's own representation of an HTTP response.
     *
     * @param response the BrowserMob specific HTTP response to transform
     * @return Wilma's own representation of the HTTP response
     */
    public WilmaHttpResponse transformResponse(final BrowserMobHttpResponse response) {
        WilmaHttpResponse result = responseFactory.createNewWilmaHttpResponse();
        if (response.getRawResponse() != null) {
            for (Header header : response.getRawResponse().getAllHeaders()) {
                result.addHeader(header.getName(), header.getValue());
            }
        }

        for (Header header : response.getRequestHeaders()) {
            result.addRequestHeader(header.getName(), header.getValue());
        }

        String body = response.getBody();
        int status = response.getStatus();
        result.setBody(body);
        result.setContentType(response.getContentType());
        result.setStatusCode(status);
        result.setWilmaMessageId(response.getEntry().getWilmaEntryId());
        return result;
    }
}
