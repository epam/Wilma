package com.epam.wilma.domain.http;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a HTTP response in Wilma's representation.
 * @author Tunde_Kovacs
 * @author Tamas_Kohegyi
 */
public class WilmaHttpResponse extends WilmaHttpEntity {

    private String contentType;
    private int statusCode;
    //holder of request headers
    private final Map<String, String> requestHeaders = new HashMap<>();
    //information about the original URI request received to the Wilma (to the proxy)
    private String proxyRequestLine;
    private final boolean isVolatile;

    /**
     * Constructor of the WilmaHttpResponse, where the only important thing is to know
     * if the response is volatile or not.
     * In case it is not, content/change of extraHeaders and extraHeadersToRemove has no effect on the real response.
     *
     * @param isVolatile is true or false.
     */
    public WilmaHttpResponse(boolean isVolatile) {
        this.isVolatile = isVolatile;
    }

    /**
     * Adds a WilmaHttpHeader to the list of request headers.
     * @param key key of the HTTP header
     * @param value value of the HTTP header
     */
    public void addRequestHeader(final String key, final String value) {
        requestHeaders.put(key, value);
    }

    /**
     * Returns the header with the given key.
     * @param key key of the header to get
     * @return the header value
     */
    public String getRequestHeader(final String key) {
        return requestHeaders.get(key);
    }

    /**
     * Returns a copy of the headers.
     * @return the map that holds the headers
     */
    public Map<String, String> getRequestHeaders() {
        Map<String, String> clone = new HashMap<>();
        clone.putAll(requestHeaders);
        return clone;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public String getSequenceId() {
        return requestHeaders.get(WILMA_SEQUENCE_ID);
    }

    public boolean isVolatile() {
        return isVolatile;
    }

    /**
     * Modifies the message body on-the-fly. Works with limitations only (plain text request was tested only,
     * without any compression method).
     *
     * @param newBodyArray is the prepared (gzipped, etc, as necessary) content of the new response
     * @param newBody is the new message content - the human readable part
     */
    public void setNewBody(byte[] newBodyArray, String newBody) {
        if (isVolatile()) {
            super.setNewBody(newBodyArray, newBody);
        }
    }

    public String getProxyRequestLine() {
        return proxyRequestLine;
    }

    public void setProxyRequestLine(final String proxyRequestLine) {
        this.proxyRequestLine = proxyRequestLine;
    }
}
