package com.epam.wilma.extras.replicator.repeater;

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
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Sends a new HTTP request.
 *
 * @author Tamas Kohegyi
 */
public class HttpRequestSender {

    private final Logger logger = LoggerFactory.getLogger(HttpRequestSender.class);

    /**
     * Sends a new HTTP request to a server through a proxy.
     *
     * @param wilmaHttpRequest a set of parameters that will set the content of the request
     *                         and specify the proxy it should go through
     * @param targetUrl is the target url to call the secondary server
     * @return with the response of the secondary call
     */
    public WilmaHttpResponse callSecondaryServer(final WilmaHttpRequest wilmaHttpRequest, final String targetUrl) {
        String messageId = wilmaHttpRequest.getWilmaMessageId();
        String serverIpAddress = wilmaHttpRequest.getRemoteAddr();
        WilmaHttpResponse response = null;

        try {
            HttpClient httpClient = new HttpClient();

            if (wilmaHttpRequest.getRequestLine().startsWith("GET")) {
                //get method
                GetMethod httpGet = new GetMethod(targetUrl);
                // copy all headers
                for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
                    String headerValue = wilmaHttpRequest.getHeader(headerKey);
                    httpGet.addRequestHeader(headerKey, headerValue);
                }

                httpClient.executeMethod(httpGet);
                response = transferResponse(httpGet, messageId, serverIpAddress);
            } else if (wilmaHttpRequest.getRequestLine().startsWith("POST")) {
                PostMethod httpPost = new PostMethod(targetUrl);
                InputStream inputStream = new ByteArrayInputStream(wilmaHttpRequest.getBody().getBytes("UTF-8"));
                String encoding = wilmaHttpRequest.getHeader("Content-Encoding");
                if ((encoding != null) && (encoding.toLowerCase().contains("gzip"))) {
                    inputStream = encode(inputStream);
                }
                String contentType = wilmaHttpRequest.getHeader("Content-Type");
                if (contentType == null) {
                    contentType = "text/plain";
                }

                InputStreamRequestEntity entity = new InputStreamRequestEntity(inputStream, contentType);
                httpPost.setRequestEntity(entity);

                // copy all headers
                for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
                    String headerValue = wilmaHttpRequest.getHeader(headerKey);
                    httpPost.addRequestHeader(headerKey, headerValue);
                }

                httpClient.executeMethod(httpPost);
                response = transferResponse(httpPost, messageId, serverIpAddress);
            }
        } catch (IOException e) {
            logger.error("Secondary Request Issue", e);
        }

        return response;
    }

    private InputStream encode(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(baos);
        //... Code to read from your original uncompressed data and write to gout.
        IOUtils.copy(inputStream, gout);
        gout.finish();
        //Convert to InputStream.
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private String getInputStreamAsString(final InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream);
    }

    private WilmaHttpResponse transferResponse(HttpMethodBase method, String messageId, String serverIpAddress) {
        WilmaHttpResponse result = new WilmaHttpResponse(false);
        try {
            for (Header header : method.getRequestHeaders()) {
                result.addRequestHeader(header.getName(), header.getValue());
            }
            for (Header header : method.getResponseHeaders()) {
                result.addHeader(header.getName(), header.getValue());
            }

            String body = getInputStreamAsString(method.getResponseBodyAsStream());
            int status = method.getStatusCode();
            result.setBody(body);
            result.setContentType(method.getResponseHeader("Content-Type").getValue());
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
