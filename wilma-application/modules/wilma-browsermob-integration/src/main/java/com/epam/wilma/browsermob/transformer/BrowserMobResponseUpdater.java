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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        // update the headers of the original response with extra headers added by Resp interceptors
        Map<String, String> extraHeaders = wilmaResponse.getExtraHeaders();
        if (extraHeaders != null) { //many cases there is nothing to add
            for (Map.Entry<String, String> stringStringEntry : extraHeaders.entrySet()) {
                browserMobHttpResponse.getRawResponse().addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        String newBody = wilmaResponse.getNewBody();
        if (newBody != null) {
            try {
                browserMobHttpResponse.setAnswer(newBody.getBytes(StandardCharsets.UTF_8));
                browserMobHttpResponse.getRawResponse().setEntity(new StringEntity(wilmaResponse.getNewBody(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                //ups, were unable to set new response correctly ...
                logger.warn("Message ont-the-fly update was failed for message: " + wilmaResponse.getWilmaMessageId(), e);
            }
        }
    }
}
