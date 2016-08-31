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
import com.epam.wilma.extras.replicator.gzip.GzipCompressor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Client that handles the secondary HTTP(s) requests.
 *
 * @author Tamas Kohegyi
 */
public class SecondaryClient {

    private final Logger logger = LoggerFactory.getLogger(SecondaryClient.class);
    private GzipCompressor gzipCompressor = new GzipCompressor();

    private SecondaryRequestHandler secondaryRequestHandler = new SecondaryRequestHandler();

    /**
     * Sends a new HTTP request to a server through a proxy.
     *
     * @param wilmaHttpRequest a set of parameters that will set the content of the request
     *                         and specify the proxy it should go through
     * @return with the response of the secondary call
     */
    public WilmaHttpResponse callSecondaryServer(final WilmaHttpRequest wilmaHttpRequest) {
        URI uri = wilmaHttpRequest.getUri();
        WilmaHttpResponse wilmaResponse = null;

        try {
            if (wilmaHttpRequest.getRequestLine().startsWith("GET")) {
                //get method
                HttpGet httpGet = new HttpGet(uri.toString());

                //send/receive message
                wilmaResponse = secondaryRequestHandler.sendReceive(wilmaHttpRequest, httpGet);
            } else if (wilmaHttpRequest.getRequestLine().startsWith("POST")) {
                //post method
                HttpPost httpPost = new HttpPost(uri.toString());

                //prepare body
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

                //send/receive message
                wilmaResponse = secondaryRequestHandler.sendReceive(wilmaHttpRequest, httpPost);
            }
        } catch (IOException e) {
            logger.error("Secondary Request Issue", e);
        }

        return wilmaResponse;
    }

}
