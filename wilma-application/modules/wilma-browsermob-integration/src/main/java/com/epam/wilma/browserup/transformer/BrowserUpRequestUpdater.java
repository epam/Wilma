package com.epam.wilma.browserup.transformer;
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

import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Updates BrowserMob specific HTTP request using {@link WilmaHttpRequest}.
 *
 * @author Tamas Kohegyi
 */
@Component
public class BrowserUpRequestUpdater {

    /**
     * Updates BrowserUp specific HTTP request. Adds wilma logger id to headers and updates URI.
     */
    public void updateRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo, WilmaHttpRequest wilmaHttpRequest) {
        // Update the headers of the original request with extra headers added/removed by Req interceptors
        // Note, that when we redirect the request to the stub, we always add the message id to the extra headers part

        Map<String, HttpHeaderChange> headerChangeMap = wilmaHttpRequest.getHeaderChanges();
        if (headerChangeMap != null && !headerChangeMap.isEmpty()) { //we have header change requests
            for (Map.Entry<String, HttpHeaderChange> headerChangeEntry : headerChangeMap.entrySet()) {
                String headerKey = headerChangeEntry.getKey();
                HttpHeaderChange headerChange = headerChangeEntry.getValue();
                String header = request.headers().get(headerKey);

                if (headerChange instanceof HttpHeaderToBeUpdated) {
                    // it is HttpHeaderToBeChanged, so added, or updated
                    if (header != null) {
                        ((HttpHeaderToBeUpdated) headerChange).setOriginalValue(header);
                    }
                    request.headers().add(headerKey, ((HttpHeaderToBeUpdated) headerChange).getNewValue());
                    headerChange.setApplied();
                } else {
                    // it is HttpHeaderToBeRemoved
                    if (header != null) {
                        request.headers().remove(headerKey);
                        headerChange.setApplied();
                    }
                }
            }
        }

        //update the body
        byte[] newBody = wilmaHttpRequest.getNewBody();
        if (newBody != null) {
            contents.setBinaryContents(wilmaHttpRequest.getNewBody().clone());
        }
        //set response volatility approach
        //N/A response is always volatile
        //switch between original uri (proxy mode selected) or wilma internal uri (stub mode selected)
        request.setUri(wilmaHttpRequest.getUri().toString());
    }
}
