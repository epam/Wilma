package com.epam.wilma.gepard.testclient;
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

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.compression.fis.FastInfosetCompressor;
import com.epam.wilma.gepard.testclient.compression.gzip.GzipCompressor;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class that tests Wilma multi stub-config configurations.
 */
public class MultiStubHttpPostRequestSender {

    private final FastInfosetCompressor fastInfosetCompressor = new FastInfosetCompressor();
    private final GzipCompressor gzipCompressor = new GzipCompressor();

    /**
     * Sends a new HTTP request to a server through a proxy. Also logs request and response with gepard framework.
     *
     * @param tc                the running test case
     * @param requestParameters a set of parameters that will set the content of the request
     *                          and specify the proxy it should go through
     * @return with Response Holder class.
     * @throws IOException                  in case error occurs
     * @throws ParserConfigurationException in case error occurs
     * @throws SAXException                 in case error occurs
     */
    public ResponseHolder callWilmaTestServer(final WilmaTestCase tc, final MultiStubRequestParameters requestParameters)
        throws IOException, ParserConfigurationException, SAXException {
        String responseCode;
        ResponseHolder responseMessage;

        HttpClient httpClient = new HttpClient();
        PostMethod httpPost = new PostMethod(requestParameters.getTestServerUrl());
        if (requestParameters.isUseProxy()) {
            httpClient.getHostConfiguration().setProxy(requestParameters.getWilmaHost(), requestParameters.getWilmaPort());
        }
        createRequest(requestParameters, httpPost);
        httpClient.getHttpConnectionManager().getParams()
                .setSendBufferSize(Integer.valueOf(tc.getTestClassExecutionData().getEnvironment().getProperty("http.socket.sendbuffer")));
        httpClient.getHttpConnectionManager().getParams()
                .setReceiveBufferSize(Integer.valueOf(tc.getTestClassExecutionData().getEnvironment().getProperty("http.socket.receivebuffer")));
        int statusCode;
        statusCode = httpClient.executeMethod(httpPost);
        responseCode = "status code: " + statusCode + "\n";
        responseMessage = createResponse(httpPost);
        responseMessage.setResponseCode(responseCode);
        tc.setActualResponseCode(statusCode);
        Header contentTypeHeader = httpPost.getResponseHeader("Content-Type");
        if (contentTypeHeader != null) {
            tc.setActualResponseContentType(contentTypeHeader.getValue());
        }
        Header sequenceHeader = httpPost.getResponseHeader("Wilma-Sequence");
        if (sequenceHeader != null) {
            tc.setActualDialogDescriptor(sequenceHeader.getValue());
        }

        return responseMessage;
    }

    private void createRequest(final MultiStubRequestParameters requestParameters, final PostMethod httpPost) throws IOException,
            ParserConfigurationException, SAXException {
        InputStream inputStream = requestParameters.getInputStream();
        if (requestParameters.getContentType().contains("fastinfoset")) {
            inputStream = fastInfosetCompressor.compress(inputStream);
        }
        if (requestParameters.getContentEncoding().contains("gzip")) {
            inputStream = gzipCompressor.compress(inputStream);
            httpPost.setRequestHeader("Content-Encoding", requestParameters.getContentEncoding());
        }
        final InputStreamRequestEntity entity = new InputStreamRequestEntity(inputStream, requestParameters.getContentType());
        httpPost.setRequestEntity(entity);
        httpPost.setRequestHeader("Accept", requestParameters.getAcceptHeader());
        httpPost.addRequestHeader("Accept-Encoding", requestParameters.getAcceptEncoding());
        if (requestParameters.getSpecialHeader() != null) {
            httpPost.addRequestHeader("Special-Header", requestParameters.getSpecialHeader());
        }
        httpPost.addParameter("nextstatus", requestParameters.getStatus());
        httpPost.addParameter("direction", requestParameters.getDirection());
        httpPost.addParameter("groupname", requestParameters.getGroupName());
    }

    private ResponseHolder createResponse(final PostMethod httpPost) throws IOException {
        ResponseHolder responseHolder = new ResponseHolder();
        String responseMessage;
        InputStream responseBodyAsStream = httpPost.getResponseBodyAsStream();
        if (httpPost.getResponseHeader("Content-Encoding") != null && httpPost.getResponseHeader("Content-Encoding").getValue().contains("gzip")) {
            responseBodyAsStream = gzipCompressor.decompress(responseBodyAsStream);
        }
        if (httpPost.getResponseHeader("Content-type") != null && httpPost.getResponseHeader("Content-type").getValue().contains("fastinfoset")) {
            try {
                responseMessage = fastInfosetCompressor.decompress(responseBodyAsStream);
            } catch (TransformerException e) {
                responseMessage = httpPost.getResponseBodyAsString();
            }
        } else {
            responseMessage = getInputStreamAsString(responseBodyAsStream);
        }
        responseHolder.setResponseMessage(responseMessage);
        responseHolder.setHttpPost(httpPost);
        return responseHolder;
    }

    private String getInputStreamAsString(final InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, "UTF-8");
    }
}
