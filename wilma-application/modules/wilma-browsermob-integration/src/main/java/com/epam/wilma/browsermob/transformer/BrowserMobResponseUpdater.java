package com.epam.wilma.browsermob.transformer;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Updates BrowserMob specific HTTP request using {@link com.epam.wilma.domain.http.WilmaHttpRequest}.
 *
 * @author Tamas Kohegyi
 */
@Component
public class BrowserMobResponseUpdater {

    private final Logger logger = LoggerFactory.getLogger(BrowserMobResponseUpdater.class);

    /**
     * Updates BrowserMob specific HTTP response. Adds extra headers to the response only.
     *
     * @param browserMobHttpResponse what will be updated
     * @param wilmaResponse          contains refresher data
     */
    public void updateResponse(final BrowserMobHttpResponse browserMobHttpResponse, final WilmaHttpResponse wilmaResponse) {

        //Note: update (proxy) response is an experimental feature only
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
                Header header = browserMobHttpResponse.getRawResponse().getFirstHeader(headerKey);

                if (headerChange instanceof HttpHeaderToBeUpdated) {
                    // it is HttpHeaderToBeChanged, so added, or updated
                    if (header != null) {
                        ((HttpHeaderToBeUpdated) headerChange).setOriginalValue(header.getValue());
                    }
                    browserMobHttpResponse.getRawResponse().addHeader(headerKey, ((HttpHeaderToBeUpdated) headerChange).getNewValue());
                    headerChange.setApplied();
                } else {
                    // it is HttpHeaderToBeRemoved
                    if (header != null) {
                        browserMobHttpResponse.getRawResponse().removeHeader(header);
                        headerChange.setApplied();
                    }
                }
            }
        }

        byte[] newBody = wilmaResponse.getNewBody();
        if (newBody != null) {
            try {
                browserMobHttpResponse.setAnswer(newBody);
                browserMobHttpResponse.getRawResponse().setEntity(new ByteArrayEntity(wilmaResponse.getNewBody()));
            } catch (IOException e) {
                //ups, were unable to set new response correctly ...
                logger.warn("Message ont-the-fly update was failed for message: " + wilmaResponse.getWilmaMessageId(), e);
            }
        }
    }
}
