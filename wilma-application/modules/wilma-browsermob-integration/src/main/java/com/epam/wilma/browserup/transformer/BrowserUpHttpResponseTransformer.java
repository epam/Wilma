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
import com.epam.wilma.browsermob.configuration.MessageConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.proxy.helper.WilmaResponseFactory;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Executes transformations between a BrowserUp specific HTTP response and Wilma's own representation of an HTTP response.
 *
 * @author Tamas Kohegyi
 */
@Component
public class BrowserUpHttpResponseTransformer {

    @Autowired
    private WilmaResponseFactory responseFactory;
    @Autowired
    private MessageConfigurationAccess configurationAccess;

    /**
     * Transforms a BrowserMob specific HTTP response to Wilma's own representation of an HTTP response.
     *
     * @param response the BrowserMob specific HTTP response to transform
     * @return Wilma's own representation of the HTTP response
     */
    public WilmaHttpResponse transformResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo, PreservedInformation preservedInformation) {
        WilmaHttpResponse result = responseFactory.createNewWilmaHttpResponse(true);

        HttpHeaders httpHeaders = response.headers();
        for (Map.Entry<String,String> entry : httpHeaders.entries()) {
            result.addHeader(entry.getKey(), entry.getValue());
        }

        HttpRequest httpRequest = messageInfo.getOriginalRequest();
        for (Map.Entry<String,String> entry : httpRequest.headers().entries()) {
            result.addRequestHeader(entry.getKey(), entry.getValue());
        }

        result.setRequestLine(httpRequest.uri());
        result.setProxyRequestLine(httpRequest.uri()); //TODO, WARNING, original URI is not yet preserved ! response.getProxyRequestURI().toString()
        String body = new String(contents.getBinaryContents());
        int status = response.status().code();
        result.setBody(body);
        result.setInputStream(new ByteArrayInputStream(contents.getBinaryContents()));
        result.setContentType(contents.getContentType());
        result.setStatusCode(status);

        //set Wilma Message Id
        String instancePrefix = prepareInstancePrefix();
        String wilmaEntryId = ""; //TODO this may cause problem, since we must have wilma entry ID info! instancePrefix + response.getEntry().getWilmaEntryId()
        result.setWilmaMessageId(instancePrefix + wilmaEntryId);

        //set remote addr
        String ipAddress = ""; //TODO - need to know server ip address somehow - response.getEntry().getServerIPAddress();
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
