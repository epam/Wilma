package com.epam.wilma.browserup.transformer;
/*==========================================================================
Copyright since 2020, EPAM Systems

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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import io.netty.handler.codec.http.HttpResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Updates BrowserUp specific HTTP request using {@link com.epam.wilma.domain.http.WilmaHttpRequest}.
 *
 * @author Tamas Kohegyi
 */
@Component
public class BrowserUpResponseUpdater {

    /**
     * Updates BrowserUp specific HTTP response. Adds extra headers to the response only.
     */
    public void updateResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo, WilmaHttpResponse wilmaResponse) {
        if (!wilmaResponse.isVolatile()) {
            return;
        }
        // From now on, the response is volatile

        // update the headers of the original response with extra headers added/removed by Resp interceptors
        Map<String, HttpHeaderChange> headerChangeMap = wilmaResponse.getHeaderChanges();
        if (headerChangeMap != null && !headerChangeMap.isEmpty()) { //we have header change requests
            for (Map.Entry<String, HttpHeaderChange> headerChangeEntry : headerChangeMap.entrySet()) {
                String headerKey = headerChangeEntry.getKey();
                HttpHeaderChange headerChange = headerChangeEntry.getValue();
                String header = response.headers().get(headerKey);

                if (headerChange instanceof HttpHeaderToBeUpdated) {
                    // it is HttpHeaderToBeChanged, so added, or updated
                    if (header != null) {
                        ((HttpHeaderToBeUpdated) headerChange).setOriginalValue(header);
                    }
                    response.headers().add(headerKey, ((HttpHeaderToBeUpdated) headerChange).getNewValue());
                    headerChange.setApplied();
                } else {
                    // it is HttpHeaderToBeRemoved
                    if (header != null) {
                        response.headers().remove(headerKey);
                        headerChange.setApplied();
                    }
                }
            }
        }

        byte[] newBody = wilmaResponse.getNewBody();
        if (newBody != null) {
            contents.setTextContents(new String(newBody));
            contents.setBinaryContents(newBody.clone());
        }
    }
}
