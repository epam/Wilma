package com.epam.wilma.test.client;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;

/**
 * Sends a new HTTP request to a server through a proxy.
 * @author Marton_Sereg
 *
 */
public class HttpRequestSender {

    private final Logger logger = LoggerFactory.getLogger(HttpRequestSender.class);

    /**
     * Sends a new HTTP request to a server through a proxy.
     * @param requestParameters a set of parameters that will set the content of the request
     * and specify the proxy it should go through
     */
    public void callWilmaTestServer(final RequestParameters requestParameters, final TestClientParameters clientParameters) {
        try {
            HttpClient httpClient = new HttpClient();
            String serverUrl = requestParameters.getTestServerUrl();
            logger.info("Server URL: " + serverUrl);
            PostMethod httpPost = new PostMethod(serverUrl);
            if (clientParameters.isUseProxy()) {
                String proxyHost = requestParameters.getWilmaHost();
                Integer proxyPort = requestParameters.getWilmaPort();
                logger.info("Proxy: " + proxyHost + ":" + proxyPort);
                httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
            }
            InputStream inputStream = requestParameters.getInputStream();
            if (requestParameters.getContentType().contains("fastinfoset")) {
                logger.info("Compressing it by using FIS.");
                inputStream = compress(inputStream);
            }
            if (requestParameters.getContentEncoding().contains("gzip")) {
                logger.info("Encoding it by using gzip.");
                inputStream = encode(inputStream);
                httpPost.setRequestHeader("Content-Encoding", requestParameters.getContentEncoding());
            }
            InputStreamRequestEntity entity = new InputStreamRequestEntity(inputStream, requestParameters.getContentType());
            httpPost.setRequestEntity(entity);
            String acceptHeader = requestParameters.getAcceptHeader();
            logger.info("Accept (header): " + acceptHeader);
            httpPost.setRequestHeader("Accept", acceptHeader);
            String acceptEncoding = requestParameters.getAcceptEncoding();
            logger.info("Accept-Encoding: " + acceptEncoding);
            httpPost.addRequestHeader("Accept-Encoding", acceptEncoding);
            logger.info("Add 'AlterMessage' header.");
            httpPost.addRequestHeader("AlterMessage", "true"); //always request alter message from Wilma
            //httpPost.addRequestHeader("0", "WilmaBypass=true");

            httpClient.getHttpConnectionManager().getParams().setSendBufferSize(clientParameters.getRequestBufferSize());
            httpClient.getHttpConnectionManager().getParams().setReceiveBufferSize(clientParameters.getResponseBufferSize());

            int statusCode = httpClient.executeMethod(httpPost);
            logger.info("Response Status Code: " + statusCode);
            if (clientParameters.getAllowResponseLogging()) {
                logger.info(getInputStreamAsString(httpPost.getResponseBodyAsStream()));
            }
        } catch (UnsupportedEncodingException e) {
            throw new SystemException("Unsupported encoding.", e);
        } catch (HttpException e) {
            throw new SystemException("Http exception occurred.", e);
        } catch (IOException e) {
            throw new SystemException("InputStream cannot be read.", e);
        }
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

    private InputStream compress(final InputStream source) {
        try {
            OutputStream fis = new ByteArrayOutputStream();
            SAXDocumentSerializer saxDocumentSerializer = new SAXDocumentSerializer();
            saxDocumentSerializer.setOutputStream(fis);
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(source, saxDocumentSerializer);
            return new ByteArrayInputStream(((ByteArrayOutputStream) fis).toByteArray());
        } catch (ParserConfigurationException e) {
            throw new SystemException("error", e);
        } catch (SAXException e) {
            throw new SystemException("error", e);
        } catch (IOException e) {
            throw new SystemException("error", e);
        }
    }
}
