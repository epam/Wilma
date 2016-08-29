package com.epam.wilma.extras.replicator;

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
import com.epam.wilma.extras.replicator.gzip.GzipCompressor;
import com.epam.wilma.extras.replicator.gzip.GzipDecompressor;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;

/**
 * Sends a secondary HTTP(s) request.
 *
 * @author Tamas Kohegyi
 */
class SecondaryRequestSender {

    private final Logger logger = LoggerFactory.getLogger(SecondaryRequestSender.class);
    private GzipCompressor gzipCompressor = new GzipCompressor();
    private GzipDecompressor gzipDecompressor = new GzipDecompressor();

    /**
     * Sends a new HTTP request to a server through a proxy.
     *
     * @param wilmaHttpRequest a set of parameters that will set the content of the request
     *                         and specify the proxy it should go through
     * @return with the response of the secondary call
     */
    WilmaHttpResponse callSecondaryServer(final WilmaHttpRequest wilmaHttpRequest) {
        String messageId = wilmaHttpRequest.getWilmaMessageId();
        String serverIpAddress = wilmaHttpRequest.getRemoteAddr();
        URI uri = wilmaHttpRequest.getUri();
        logger.debug("URI: ", uri.toString());
        WilmaHttpResponse wilmaResponse = null;

        HttpClient httpClient = new DefaultHttpClient();

        try {
            if (wilmaHttpRequest.getRequestLine().startsWith("GET")) {
                //get method
                HttpGet httpGet = new HttpGet(uri.toString());
                // copy all headers
                for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
                    String headerValue = wilmaHttpRequest.getHeader(headerKey);
                    httpGet.addHeader(headerKey, headerValue);
                }

                HttpResponse response = httpClient.execute(httpGet);
                wilmaResponse = transferResponse(response, httpGet, messageId, serverIpAddress);

            } else if (wilmaHttpRequest.getRequestLine().startsWith("POST")) {
                HttpPost httpPost = new HttpPost(uri.toString());
                InputStream inputStream = new ByteArrayInputStream(wilmaHttpRequest.getBody().getBytes("UTF-8"));
                String encoding = wilmaHttpRequest.getHeader("Content-Encoding");
                if ((encoding != null) && (encoding.toLowerCase().contains("gzip"))) {
                    inputStream = gzipCompressor.compress(inputStream);
                }
                String contentType = wilmaHttpRequest.getHeader("Content-Type");
                if (contentType == null) {
                    contentType = "text/plain";
                }

                InputStreamEntity reqEntity = new InputStreamEntity(inputStream, -1, ContentType.create(contentType));

                httpPost.setEntity(reqEntity);

                // copy all headers
                for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
                    String headerValue = wilmaHttpRequest.getHeader(headerKey);
                    httpPost.addHeader(headerKey, headerValue);
                }

                HttpResponse response = httpClient.execute(httpPost);
                wilmaResponse = transferResponse(response, httpPost, messageId, serverIpAddress);
            }
        } catch (IOException e) {
            logger.error("Secondary Request Issue", e);
        }

        return wilmaResponse;
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
