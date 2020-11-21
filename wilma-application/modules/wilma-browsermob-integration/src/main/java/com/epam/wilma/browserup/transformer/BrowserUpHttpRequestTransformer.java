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
import com.epam.browsermob.messagemarker.idgenerator.TimeStampBasedIdGenerator;
import com.epam.wilma.browsermob.configuration.MessageConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.browserup.proxy.helper.BrowserUpProxyServer;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.proxy.helper.WilmaRequestFactory;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Executes transformations between a BrowserUp specific HTTP request and Wilma's own representation of an HTTP request.
 *
 * @author Tamas Kohegyi
 */
@Component
public class BrowserUpHttpRequestTransformer {

    private static final Logger log = LoggerFactory.getLogger(BrowserUpProxyServer.class);
    private static final TimeStampBasedIdGenerator TIME_STAMP_BASED_ID_GENERATOR = new TimeStampBasedIdGenerator();

    @Autowired
    private WilmaRequestFactory requestFactory;
    @Autowired
    private MessageConfigurationAccess configurationAccess;

    /**
     * Transforms a BrowserMob specific HTTP request to Wilma's own representation of an HTTP request.
     *
     * @return Wilma's own representation of the HTTP request
     * @throws ApplicationException when request body cannot be read
     */
    public WilmaHttpRequest transformRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo, PreservedInformation preservedInformation) throws ApplicationException {
        WilmaHttpRequest result = requestFactory.createNewWilmaHttpRequest();
        result.setRequestLine(request.uri());
        HttpHeaders httpHeaders = request.headers();
        for (Map.Entry<String, String> entry : httpHeaders.entries()) {
            result.addHeader(entry.getKey(), entry.getValue());
        }
        String body = new String(contents.getBinaryContents());
        InputStream bodyStream = new ByteArrayInputStream(contents.getBinaryContents());
        result.setInputStream(bodyStream);
        result.setBody(body);
        try {
            result.setUri(new URI(request.uri()));
        } catch (URISyntaxException e) {
            throw new ApplicationException("Invalid URI arrived at request.", e);
        }
        result.setResponseVolatile(true); //it is always volatile
        //prepare instance prefix
        MessagePropertyDTO properties = configurationAccess.getProperties();
        String instancePrefix = properties.getInstancePrefix();
        if (instancePrefix != null) {
            instancePrefix += "_";  // "prefix_"
        } else {
            instancePrefix = "";
        }
        //messageInfo.getChannelHandlerContext().channel().id().toString();
        //set Wilma Message Id
        result.setWilmaMessageId(instancePrefix + TIME_STAMP_BASED_ID_GENERATOR.nextIdentifier());

        //set remote addr
        result.setRemoteAddr(null); //TODO, cannot determine remote address, this might be problematic

        return result;
    }

}
