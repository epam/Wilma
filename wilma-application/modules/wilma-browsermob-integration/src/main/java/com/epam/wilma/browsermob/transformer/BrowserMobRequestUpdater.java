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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Updates BrowserMob specific HTTP request using {@link WilmaHttpRequest}.
 *
 * @author Tunde_Kovacs
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
        for (Map.Entry<String, String> stringStringEntry : extraHeaders.entrySet()) {
            browserMobHttpRequest.getMethod().addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        //switch between original uri (proxy mode selected) or wilma internal uri (stub mode selected)
        browserMobHttpRequest.getMethod().setURI(wilmaRequest.getUri());
    }
}
