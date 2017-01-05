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

import com.epam.wilma.browsermob.configuration.MessageConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.browsermob.transformer.helper.InputStreamConverter;
import com.epam.wilma.browsermob.transformer.helper.WilmaRequestFactory;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Executes transformations between a BrowserMob specific HTTP request and Wilma's own representation of an HTTP request.
 *
 * @author Marton_Sereg
 */
@Component
public class HttpRequestTransformer {

    @Autowired
    private InputStreamConverter inputStreamConverter;
    @Autowired
    private WilmaRequestFactory requestFactory;
    @Autowired
    private MessageConfigurationAccess configurationAccess;

    /**
     * Transforms a BrowserMob specific HTTP request to Wilma's own representation of an HTTP request.
     *
     * @param browserMobHttpRequest the BrowserMob specific HTTP request to transform
     * @return Wilma's own representation of the HTTP request
     * @throws ApplicationException when request body cannot be read
     */
    public WilmaHttpRequest transformRequest(final BrowserMobHttpRequest browserMobHttpRequest) throws ApplicationException {
        WilmaHttpRequest result = requestFactory.createNewWilmaHttpRequest();
        HttpRequestBase requestBase = browserMobHttpRequest.getMethod();
        result.setRequestLine(requestBase.getRequestLine().toString());
        for (Header header : requestBase.getAllHeaders()) {
            result.addHeader(header.getName(), header.getValue());
        }
        InputStream clonedInputStream = browserMobHttpRequest.getPlayGround();
        result.setInputStream(clonedInputStream);
        result.setBody(inputStreamConverter.getStringFromStream(clonedInputStream));
        result.setUri(requestBase.getURI());
        result.setResponseVolatile(browserMobHttpRequest.getResponseVolatile());
        //prepare instance prefix
        MessagePropertyDTO properties = configurationAccess.getProperties();
        String instancePrefix = properties.getInstancePrefix();
        if (instancePrefix != null) {
            instancePrefix += "_";  // "prefix_"
        } else {
            instancePrefix = "";
        }

        //set Wilma Message Id
        result.setWilmaMessageId(instancePrefix + browserMobHttpRequest.getWilmaMessageId());

        //set remote addr
        String ipAddress = browserMobHttpRequest.getProxyRequest().getRemoteAddr();
        result.setRemoteAddr(ipAddress);

        return result;
    }

}
