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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Request parameters for a HTTP request to be sent out.
 */
public class RequestParameters {

    private String testServerUrl;
    private boolean useProxy;
    private String wilmaHost;
    private Integer wilmaPort;
    private InputStream inputStream;
    private String contentType;
    private String acceptHeader;
    private String contentEncoding;
    private String acceptEncoding;
    private String specialHeader;
    private final Map<String, String> customHeaders = new HashMap<>();

    /**
     * Adds the <tt>testServerUrl</tt> to the request parameters. Part of the building process.
     *
     * @param testServerUrl the server url the request will be sent to
     * @return the request parameter
     */
    public RequestParameters testServerUrl(final String testServerUrl) {
        this.testServerUrl = testServerUrl;
        return this;
    }

    /**
     * Adds the <tt>useProxy</tt> field to the request parameters. Part of the building process.
     *
     * @param useProxy decides whether the proxy should be used when sending
     *                 the request to the server or not
     * @return the request parameter
     */
    public RequestParameters useProxy(final boolean useProxy) {
        this.useProxy = useProxy;
        return this;
    }

    /**
     * Adds the <tt>wilmaHost</tt> to the request parameters. Part of the building process.
     *
     * @param wilmaHost the proxy host name the request will go through
     * @return the request parameter
     */
    public RequestParameters wilmaHost(final String wilmaHost) {
        this.wilmaHost = wilmaHost;
        return this;
    }

    /**
     * Adds the <tt>wilmaPort</tt> to the request parameters. Part of the building process.
     *
     * @param wilmaPort the proxy port number the request will go through
     * @return the request parameter
     */
    public RequestParameters wilmaPort(final Integer wilmaPort) {
        this.wilmaPort = wilmaPort;
        return this;
    }

    /**
     * Adds the input stream to the request parameters. Part of the building process.
     *
     * @param inputStream the input stream of the request
     * @return the request parameter
     */
    public RequestParameters requestInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    /**
     * Adds the content type to the request parameters. Part of the building process.
     *
     * @param contentType the contentType of the request body
     * @return the request parameter
     */
    public RequestParameters contentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Adds the accept header to the request parameters. Part of the building process.
     *
     * @param acceptHeader the acceptHeader of the request body
     * @return the request parameter
     */
    public RequestParameters acceptHeader(final String acceptHeader) {
        this.acceptHeader = acceptHeader;
        return this;
    }

    /**
     * Adds the contentEncoding header to the request parameters. Part of the building process.
     *
     * @param contentEncoding the encoding of the message body
     * @return the request parameter
     */
    public RequestParameters contentEncoding(final String contentEncoding) {
        this.contentEncoding = contentEncoding;
        return this;
    }

    /**
     * Adds the "Accept-Encoding" header to the request parameters. Part of the building process.
     *
     * @param acceptEncoding the encoding of the message body
     * @return the request parameter
     */
    public RequestParameters acceptEncoding(final String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
        return this;
    }

    /**
     * Adds a special header to the request parameters. Part of the building process.
     *
     * @param specialHeader the encoding of the message body
     * @return the request parameter
     */
    public RequestParameters specialHeader(final String specialHeader) {
        this.specialHeader = specialHeader;
        return this;
    }

    /**
     * Adds a custom header to the request parameters. Part of the building process.
     *
     * @param name is the name part of the customer header.
     * @param value is the value part of the customer header.
     * @return the request parameter.
     */
    public RequestParameters customHeader(final String name, final String value) {
        customHeaders.put(name, value);
        return this;
    }

    public Set<Entry<String, String>> getCustomHeaders() {
        return customHeaders.entrySet();
    }

    public String getTestServerUrl() {
        return testServerUrl;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public String getWilmaHost() {
        return wilmaHost;
    }

    public Integer getWilmaPort() {
        return wilmaPort;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public String getSpecialHeader() {
        return specialHeader;
    }
}
