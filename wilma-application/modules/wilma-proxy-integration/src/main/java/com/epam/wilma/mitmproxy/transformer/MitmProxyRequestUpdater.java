package com.epam.wilma.mitmproxy.transformer;
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
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Updates MitmProxy specific HTTP request using {@link WilmaHttpRequest}.
 *
 * @author Tunde_Kovacs, Tamas Kohegyi
 */
@Component
public class MitmProxyRequestUpdater {

    /**
     * Updates MitmProxy specific HTTP request. Adds wilma logger id to headers and updates URI.
     *
     * @param browserMobHttpRequest what will be updated
     * @param wilmaRequest          contains refresher data
     */
    public void updateRequest(final MitmJavaProxyHttpRequest browserMobHttpRequest, final WilmaHttpRequest wilmaRequest) {
        // Update the headers of the original request with extra headers added/removed by Req interceptors
        // Note, that when we redirect the request to the stub, we always add the message id to the extra headers part
        updateHeaderPart(browserMobHttpRequest, wilmaRequest);

        //update the body
        updateBodyPart(browserMobHttpRequest, wilmaRequest);

        //set and finalize the response volatility approach
        browserMobHttpRequest.setResponseVolatile(wilmaRequest.isResponseVolatile());

        //set switch between original uri (proxy mode selected) or wilma internal uri (stub mode selected)
        browserMobHttpRequest.getMethod().setURI(wilmaRequest.getUri());
    }

    private void updateHeaderPart(final MitmJavaProxyHttpRequest browserMobHttpRequest, final WilmaHttpRequest wilmaRequest) {
        Map<String, HttpHeaderChange> headerChangeMap = wilmaRequest.getHeaderChanges();
        if (headerChangeMap != null && !headerChangeMap.isEmpty()) { //we have header change requests
            for (Map.Entry<String, HttpHeaderChange> headerChangeEntry : headerChangeMap.entrySet()) {
                String headerKey = headerChangeEntry.getKey();
                HttpHeaderChange headerChange = headerChangeEntry.getValue();
                Header header = browserMobHttpRequest.getMethod().getFirstHeader(headerKey);
                handleSingleHeaderChange(browserMobHttpRequest, headerKey, headerChange, header);
            }
        }
    }

    private void handleSingleHeaderChange(final MitmJavaProxyHttpRequest browserMobHttpRequest, String headerKey, HttpHeaderChange headerChange, Header originalHeader) {
        if (headerChange instanceof HttpHeaderToBeUpdated) {
            // it is HttpHeaderToBeChanged, so added, or updated
            if (originalHeader != null) {
                ((HttpHeaderToBeUpdated) headerChange).setOriginalValue(originalHeader.getValue());
            }
            browserMobHttpRequest.getMethod().addHeader(headerKey, ((HttpHeaderToBeUpdated) headerChange).getNewValue());
            headerChange.setApplied();
        } else {
            // it is HttpHeaderToBeRemoved
            if (originalHeader != null) {
                browserMobHttpRequest.getMethod().removeHeader(originalHeader);
                headerChange.setApplied();
            }
        }
    }

    private void updateBodyPart(final MitmJavaProxyHttpRequest browserMobHttpRequest, final WilmaHttpRequest wilmaRequest) {
        byte[] newBody = wilmaRequest.getNewBody();
        if ((newBody != null) && (browserMobHttpRequest.getMethod() instanceof HttpEntityEnclosingRequestBase)) {
            HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase) browserMobHttpRequest.getMethod();
            enclosingRequest.setEntity(new ByteArrayEntity(wilmaRequest.getNewBody()));
        }
    }

}
