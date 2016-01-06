package com.epam.wilma.browsermob.transformer;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Updates BrowserMob specific HTTP request using {@link WilmaHttpRequest}.
 *
 * @author Tunde_Kovacs, Tamas Kohegyi
 */
@Component
public class BrowserMobRequestUpdater {

    /**
     * Updates BrowserMob specific HTTP request. Adds wilma logger id to headers and updates URI.
     *
     * @param browserMobHttpRequest what will be updated
     * @param wilmaRequest          contains refresher data
     */
    public void updateRequest(final BrowserMobHttpRequest browserMobHttpRequest, final WilmaHttpRequest wilmaRequest) {
        // update the headers of the original request with extra headers added by Req interceptors
        // when we redirect the request to the stub, we always add the message id to the extra headers part
        Map<String, String> extraHeaders = wilmaRequest.getExtraHeaders();
        if (extraHeaders != null && !extraHeaders.isEmpty()) { //many cases there is nothing to add
            for (Map.Entry<String, String> stringStringEntry : extraHeaders.entrySet()) {
                browserMobHttpRequest.getMethod().addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        Map<String, String> extraHeadersToRemove = wilmaRequest.getExtraHeadersToRemove();
        if (extraHeadersToRemove != null && !extraHeadersToRemove.isEmpty()) { //many cases there is nothing to remove
            for (Map.Entry<String, String> stringStringEntry : extraHeadersToRemove.entrySet()) {
                Header header = browserMobHttpRequest.getMethod().getFirstHeader(stringStringEntry.getKey());
                if (header != null) {
                    browserMobHttpRequest.getMethod().removeHeader(header);
                }
            }
        }

        //update the body
        String newBody = wilmaRequest.getNewBody();
        if (newBody != null) {
            if (browserMobHttpRequest.getMethod() instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase) browserMobHttpRequest.getMethod();
                enclosingRequest.setEntity(new StringEntity(wilmaRequest.getNewBody(), StandardCharsets.UTF_8));
            }
        }
        //set response volatility approach
        browserMobHttpRequest.setResponseVolatile(wilmaRequest.isResponseVolatile());
        //switch between original uri (proxy mode selected) or wilma internal uri (stub mode selected)
        browserMobHttpRequest.getMethod().setURI(wilmaRequest.getUri());
    }
}
