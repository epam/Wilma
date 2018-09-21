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

/**
 * Class holding Request parameters and usable for testing multi-stub-configuration settings.
 */
public class MultiStubRequestParameters {

    private String direction = "";
    private String groupName = "";
    private String status = "";
    private RequestParameters requestParameters = new RequestParameters();

    /**
     * Adds the <tt>testServerUrl</tt> to the request parameters. Part of the building process.
     * @param testServerUrl the server url the request will be sent to
     * @return the request parameter
     */
    public MultiStubRequestParameters testServerUrl(final String testServerUrl) {
        requestParameters.testServerUrl(testServerUrl);
        return this;
    }

    /**
     * Adds the <tt>useProxy</tt> field to the request parameters. Part of the building process.
     * @param useProxy decides whether the proxy should be used when sending
     * the request to the server or not
     * @return the request parameter
     */
    public MultiStubRequestParameters useProxy(final boolean useProxy) {
        requestParameters.useProxy(useProxy);
        return this;
    }

    /**
     * Adds the <tt>wilmaHost</tt> to the request parameters. Part of the building process.
     * @param wilmaHost the proxy host name the request will go through
     * @return the request parameter
     */
    public MultiStubRequestParameters wilmaHost(final String wilmaHost) {
        requestParameters.wilmaHost(wilmaHost);
        return this;
    }

    /**
     * Adds the <tt>wilmaPort</tt> to the request parameters. Part of the building process.
     * @param wilmaPort the proxy port number the request will go through
     * @return the request parameter
     */
    public MultiStubRequestParameters wilmaPort(final Integer wilmaPort) {
        requestParameters.wilmaPort(wilmaPort);
        return this;
    }

    /**
     * Adds the input stream to the request parameters. Part of the building process.
     * @param inputStream the input stream of the request
     * @return the request parameter
     */
    public MultiStubRequestParameters requestInputStream(final InputStream inputStream) {
        requestParameters.requestInputStream(inputStream);
        return this;
    }

    /**
     * Adds the content type to the request parameters. Part of the building process.
     * @param contentType the contentType of the request body
     * @return the request parameter
     */
    public MultiStubRequestParameters contentType(final String contentType) {
        requestParameters.contentType(contentType);
        return this;
    }

    /**
     * Adds the accept header to the request parameters. Part of the building process.
     * @param acceptHeader the acceptHeader of the request body
     * @return the request parameter
     */
    public MultiStubRequestParameters acceptHeader(final String acceptHeader) {
        requestParameters.acceptHeader(acceptHeader);
        return this;
    }

    /**
     * Adds the contentEncoding header to the request parameters. Part of the building process.
     * @param contentEncoding the encoding of the message body
     * @return the request parameter
     */
    public MultiStubRequestParameters contentEncoding(final String contentEncoding) {
        requestParameters.contentEncoding(contentEncoding);
        return this;
    }

    /**
     * Adds the "Accept-Encoding" header to the request parameters. Part of the building process.
     * @param acceptEncoding the encoding of the message body
     * @return the request parameter
     */
    public MultiStubRequestParameters acceptEncoding(final String acceptEncoding) {
        requestParameters.acceptEncoding(acceptEncoding);
        return this;
    }

    /**
     * Adds groupname to the request parameters. Part of the building process.
     * @param groupName is the group name
     * @return the request parameter
     */
    public MultiStubRequestParameters groupName(final String groupName) {
        this.groupName = groupName;
        return this;
    }

    /**
     * Adds status to the request parameters. Part of the building process.
     * @param status is enabled or disabled attribute of stub configuration
     * @return the request parameter
     */
    public MultiStubRequestParameters status(final String status) {
        this.status = status;
        return this;
    }

    /**
     * Adds direction to the request parameters. Part of the building process.
     * @param direction is minus or positive integer and means the direction of stub configuration moving.
     * @return the request parameter
     */
    public MultiStubRequestParameters direction(final String direction) {
        this.direction = direction;
        return this;
    }

    public String getTestServerUrl() {
        return requestParameters.getTestServerUrl();
    }

    public boolean isUseProxy() {
        return requestParameters.isUseProxy();
    }

    public String getWilmaHost() {
        return requestParameters.getWilmaHost();
    }

    public Integer getWilmaPort() {
        return requestParameters.getWilmaPort();
    }

    public InputStream getInputStream() {
        return requestParameters.getInputStream();
    }

    public String getContentType() {
        return requestParameters.getContentType();
    }

    public String getAcceptHeader() {
        return requestParameters.getAcceptHeader();
    }

    public String getContentEncoding() {
        return requestParameters.getContentEncoding();
    }

    public String getAcceptEncoding() {
        return requestParameters.getAcceptEncoding();
    }

    public String getSpecialHeader() {
        return requestParameters.getSpecialHeader();
    }

    public String getGroupName() {
        return groupName;
    }

    public String getStatus() {
        return status;
    }

    public String getDirection() {
        return direction;
    }
}
