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
import com.epam.wilma.proxy.configuration.MessageConfigurationAccess;
import com.epam.wilma.proxy.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.proxy.helper.WilmaResponseFactory;
import org.apache.http.Header;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Executes transformations between a MitmProxy specific HTTP response and Wilma's own representation of an HTTP response.
 *
 * @author Tunde_Kovacs
 */
@Component
public class HttpResponseTransformer {

    @Autowired
    private WilmaResponseFactory responseFactory;
    @Autowired
    private MessageConfigurationAccess configurationAccess;

    /**
     * Transforms a MitmProxy specific HTTP response to Wilma's own representation of an HTTP response.
     *
     * @param response the MitmProxy specific HTTP response to transform
     * @return Wilma's own representation of the HTTP response
     */
    public WilmaHttpResponse transformResponse(final MitmJavaProxyHttpResponse response) {
        boolean isResponseVolatile = response.isResponseVolatile();
        WilmaHttpResponse result = responseFactory.createNewWilmaHttpResponse(isResponseVolatile);
        if (response.getRawResponse() != null) {
            result.setReasonPhrase(response.getRawResponse().getStatusLine().getReasonPhrase());
            for (Header header : response.getRawResponse().getAllHeaders()) {
                result.addHeader(header.getName(), header.getValue());
            }
        }

        for (Header header : response.getRequestHeaders()) {
            result.addRequestHeader(header.getName(), header.getValue());
        }

        result.setRequestLine(response.getMethod().getRequestLine().toString());
        result.setProxyRequestLine(response.getProxyRequestURI().toString());
        String body = response.getBodyString();
        int status = response.getStatus();
        result.setBody(body);
        result.setContentType(response.getContentType());
        result.setStatusCode(status);

        //set Wilma Message Id
        String instancePrefix = prepareInstancePrefix();
        result.setWilmaMessageId(instancePrefix + response.getEntry().getMessageId());

        //set remote addr
        String ipAddress = response.getEntry().getServerIPAddress();
        result.setRemoteAddr(ipAddress);

        return result;
    }

    private String prepareInstancePrefix() {
        //prepare instance prefix
        MessagePropertyDTO properties = configurationAccess.getProperties();
        String instancePrefix = properties.getInstancePrefix();
        if (instancePrefix != null) {
            instancePrefix += "_";  // "prefix_"
        } else {
            instancePrefix = "";
        }
        return instancePrefix;
    }

}
