package com.epam.wilma.extras.replicator.secondaryClient;

/*==========================================================================
Copyright 2016 EPAM Systems

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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.extras.replicator.gzip.GzipDecompressor;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Sends a secondary HTTP(s) request.
 *
 * @author Tamas Kohegyi
 */
class SecondaryRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(SecondaryRequestHandler.class);
    private GzipDecompressor gzipDecompressor = new GzipDecompressor();

    /**
     * Send and receives the secondary message.
     *
     * @param wilmaHttpRequest is th request
     * @param method           is httpGet or httpPut
     * @return withe the response
     * @throws IOException is issue occurs
     */
    WilmaHttpResponse sendReceive(WilmaHttpRequest wilmaHttpRequest, HttpRequestBase method) throws IOException {

        String messageId = wilmaHttpRequest.getWilmaMessageId();
        String serverIpAddress = wilmaHttpRequest.getRemoteAddr();

        // copy all headers
        for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
            String headerValue = wilmaHttpRequest.getHeader(headerKey);
            method.addHeader(headerKey, headerValue);
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(method);
        return transferResponse(response, method, messageId, serverIpAddress);
    }

    private WilmaHttpResponse transferResponse(HttpResponse response, HttpRequestBase method, String messageId, String serverIpAddress) {
        WilmaHttpResponse result = new WilmaHttpResponse(false);
        try {
            for (Header header : method.getAllHeaders()) {
                result.addHeader(header.getName(), header.getValue());
            }

            int status = response.getStatusLine().getStatusCode();
            result.setContentType(response.getFirstHeader("Content-Type").getValue());

            InputStream inputStream = response.getEntity().getContent();

            String encoding = response.getFirstHeader("Content-Encoding").getValue();
            if ((encoding != null) && (encoding.toLowerCase().contains("gzip"))) {
                inputStream = gzipDecompressor.decompress(inputStream);
            }
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8");
            String body = writer.toString();
            result.setBody(body);
            result.setStatusCode(status);

            //set Wilma Message Id
            result.setWilmaMessageId(messageId);

            //set remote addr
            result.setRemoteAddr(serverIpAddress);

        } catch (IOException e) {
            logger.error("Secondary Response Transfer Issue", e);
        }
        return result;
    }

}
