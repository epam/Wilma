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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import org.apache.http.Header;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Updates MitmProxy specific HTTP request using {@link com.epam.wilma.domain.http.WilmaHttpRequest}.
 *
 * @author Tamas Kohegyi
 */
@Component
public class MitmProxyResponseUpdater {

    private final Logger logger = LoggerFactory.getLogger(MitmProxyResponseUpdater.class);

    /**
     * Updates MitmProxy specific HTTP response. Adds extra headers to the response only.
     * Note and WARNING: update (proxy) response is an experimental feature only
     *
     * @param browserMobHttpResponse what will be updated
     * @param wilmaResponse          contains refresher data
     */
    public void updateResponse(final MitmJavaProxyHttpResponse browserMobHttpResponse, final WilmaHttpResponse wilmaResponse) {
        //check if we have right to alter the response
        if (!wilmaResponse.isVolatile()) {
            return;
        }
        // From now on, the response is volatile

        // update the headers of the original response with extra headers added/removed by Resp interceptors
        updateHeaderPart(browserMobHttpResponse, wilmaResponse);

        //update the body too
        updateBodyPart(browserMobHttpResponse, wilmaResponse);
    }

    private void updateHeaderPart(final MitmJavaProxyHttpResponse browserMobHttpResponse, final WilmaHttpResponse wilmaResponse) {
        Map<String, HttpHeaderChange> headerChangeMap = wilmaResponse.getHeaderChanges();
        if (headerChangeMap != null && !headerChangeMap.isEmpty()) { //we have header change requests
            for (Map.Entry<String, HttpHeaderChange> headerChangeEntry : headerChangeMap.entrySet()) {
                String headerKey = headerChangeEntry.getKey();
                HttpHeaderChange headerChange = headerChangeEntry.getValue();
                Header header = browserMobHttpResponse.getRawResponse().getFirstHeader(headerKey);
                handleSingleHeaderChange(browserMobHttpResponse, headerKey, headerChange, header);
            }
        }
    }

    private void handleSingleHeaderChange(final MitmJavaProxyHttpResponse browserMobHttpResponse, String headerKey, HttpHeaderChange headerChange, Header originalHeader) {
        if (headerChange instanceof HttpHeaderToBeUpdated) {
            // it is HttpHeaderToBeChanged, so added, or updated
            if (originalHeader != null) {
                ((HttpHeaderToBeUpdated) headerChange).setOriginalValue(originalHeader.getValue());
            }
            browserMobHttpResponse.getRawResponse().addHeader(headerKey, ((HttpHeaderToBeUpdated) headerChange).getNewValue());
            headerChange.setApplied();
        } else {
            // it is HttpHeaderToBeRemoved
            if (originalHeader != null) {
                browserMobHttpResponse.getRawResponse().removeHeader(originalHeader);
                headerChange.setApplied();
            }
        }
    }

    private void updateBodyPart(final MitmJavaProxyHttpResponse browserMobHttpResponse, final WilmaHttpResponse wilmaResponse) {
        try {
            byte[] newBody = wilmaResponse.getNewBody();
            browserMobHttpResponse.setBody(newBody);
            //content-type
            browserMobHttpResponse.setContentType(wilmaResponse.getContentType());
            //status code
            browserMobHttpResponse.setStatus(wilmaResponse.getStatusCode());
            browserMobHttpResponse.getRawResponse().setStatusCode(wilmaResponse.getStatusCode());
            //reason-phrase
            browserMobHttpResponse.setReasonPhrase(wilmaResponse.getReasonPhrase());
        } catch (IOException e) {
            //ups, were unable to set new response correctly ...
            logger.warn("Message ont-the-fly update was failed for message: {}", wilmaResponse.getWilmaMessageId(), e);
        }
    }
}
