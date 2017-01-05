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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.stereotype.Component;

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
        // Update the headers of the original request with extra headers added/removed by Req interceptors
        // Note, that when we redirect the request to the stub, we always add the message id to the extra headers part

        Map<String, HttpHeaderChange> headerChangeMap = wilmaRequest.getHeaderChanges();
        if (headerChangeMap != null && !headerChangeMap.isEmpty()) { //we have header change requests
            for (Map.Entry<String, HttpHeaderChange> headerChangeEntry : headerChangeMap.entrySet()) {
                String headerKey = headerChangeEntry.getKey();
                HttpHeaderChange headerChange = headerChangeEntry.getValue();
                Header header = browserMobHttpRequest.getMethod().getFirstHeader(headerKey);

                if (headerChange instanceof HttpHeaderToBeUpdated) {
                    // it is HttpHeaderToBeChanged, so added, or updated
                    if (header != null) {
                        ((HttpHeaderToBeUpdated) headerChange).setOriginalValue(header.getValue());
                    }
                    browserMobHttpRequest.getMethod().addHeader(headerKey, ((HttpHeaderToBeUpdated) headerChange).getNewValue());
                    headerChange.setApplied();
                } else {
                    // it is HttpHeaderToBeRemoved
                    if (header != null) {
                        browserMobHttpRequest.getMethod().removeHeader(header);
                        headerChange.setApplied();
                    }
                }
            }
        }

        //update the body
        byte[] newBody = wilmaRequest.getNewBody();
        if (newBody != null) {
            if (browserMobHttpRequest.getMethod() instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase) browserMobHttpRequest.getMethod();
                enclosingRequest.setEntity(new ByteArrayEntity(wilmaRequest.getNewBody()));
            }
        }
        //set response volatility approach
        browserMobHttpRequest.setResponseVolatile(wilmaRequest.isResponseVolatile());
        //switch between original uri (proxy mode selected) or wilma internal uri (stub mode selected)
        browserMobHttpRequest.getMethod().setURI(wilmaRequest.getUri());
    }
}
