package com.epam.wilma.service.http;

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

import com.google.common.base.Optional;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * An HTTP "user-agent" for Wilma server, containing more request methods.
 *
 * @author Tamas_Pinter
 *
 */
public class WilmaHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(WilmaHttpClient.class);

    private HttpClient httpclient = new HttpClient();

    /**
     * Calls the given URL via HTTP GET method and returns {@code String}
     * {@code Optional} of the response.
     *
     * @param url the given URL
     * @return an {@code Optional} instance containing the HTTP method's
     *         response; otherwise returns {@link Optional#absent}.
     */
    public Optional<String> sendGetterRequest(final String url) {
        String response = null;

        GetMethod method = new GetMethod(url);

        try {
            int statusCode = httpclient.executeMethod(method);
            if (HttpStatus.SC_OK == statusCode) {
                InputStream inputResponse = method.getResponseBodyAsStream();
                response = IOUtils.toString(inputResponse, "UTF-8");
            }
        } catch (HttpException e) {
            LOG.error("Protocol exception occurred when called: " + url, e);
        } catch (IOException e) {
            LOG.error("I/O (transport) error occurred when called: " + url, e);
        } finally {
            method.releaseConnection();
        }

        return Optional.fromNullable(response);
    }

    /**
     * Calls the given URL via HTTP GET method and returns {@code true} if the
     * request was successful.
     *
     * @param url the given URL
     * @return {@code true} if the request is successful, otherwise return {@code false}
     */
    public boolean sendSetterRequest(final String url) {
        boolean requestSuccessful;

        GetMethod method = new GetMethod(url);
        requestSuccessful = executeCall(method, url);
        method.releaseConnection();

        return requestSuccessful;
    }

    private boolean executeCall(final HttpMethodBase methodBase, final String url) {
        boolean requestSuccessful = false;
        try {
            int statusCode = httpclient.executeMethod(methodBase);
            if (HttpStatus.SC_OK == statusCode) {
                requestSuccessful = true;
            }
        } catch (HttpException e) {
            LOG.error("Protocol exception occurred when called: " + url, e);
        } catch (IOException e) {
            LOG.error("I/O (transport) error occurred when called: " + url, e);
        }

        return requestSuccessful;
    }

    /**
     * Posting the given file to the given URL via HTTP POST method and returns
     * {@code true} if the request was successful.
     *
     * @param url the given URL
     * @param file the given file
     * @return {@code true} if the request is successful, otherwise return {@code false}
     */
    public boolean uploadFile(String url, File file) {
        boolean requestSuccessful = false;

        PostMethod method = new PostMethod(url);

        try {
            Part[] parts = {new FilePart("file", file)};
            method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

            int statusCode = httpclient.executeMethod(method);
            if (HttpStatus.SC_OK == statusCode) {
                requestSuccessful = true;
            }
        } catch (HttpException e) {
            LOG.error("Protocol exception occurred when called: " + url, e);
        } catch (IOException e) {
            LOG.error("I/O (transport) error occurred when called: " + url, e);
        } finally {
            method.releaseConnection();
        }

        return requestSuccessful;
    }

    /**
     * Posting the given stream to the given URL via HTTP POST method and returns
     * {@code true} if the request was successful.
     *
     * @param url the given URL
     * @param resource the given string that will be uploaded as resource
     * @return {@code true} if the request is successful, otherwise return {@code false}
     */
    public boolean uploadString(String url, String resource) {
        boolean requestSuccessful = false;

        PostMethod method = new PostMethod(url);

        try {
            method.setRequestEntity(new StringRequestEntity(resource, null, null));

            int statusCode = httpclient.executeMethod(method);
            if (HttpStatus.SC_OK == statusCode) {
                requestSuccessful = true;
            }
        } catch (HttpException e) {
            LOG.error("Protocol exception occurred when called: " + url, e);
        } catch (IOException e) {
            LOG.error("I/O (transport) error occurred when called: " + url, e);
        } finally {
            method.releaseConnection();
        }

        return requestSuccessful;
    }

    public void setHttpClient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

}
