package com.epam.wilma.domain.http;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
    private final Map<String, String> requestHeaders = new HashMap<>();
    //holder of extra headers to be added to the response
    private final Map<String, String> extraHeaders = new HashMap<>();
    //holder of extra headers to be removed from the request
    private final Map<String, String> extraHeadersToRemove = new HashMap<>();

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

    /**
     * Adds a WilmaHttpHeader to the list of extra headers.
     * @param key key of the HTTP header
     * @param value value of the HTTP header
     */
    public void addExtraHeader(final String key, final String value) {
        extraHeaders.put(key, value);
    }

    /**
     * Returns the extra header with the given key.
     * @param key key of the header to get
     * @return the header value
     */
    public String getExtraHeader(final String key) {
        return extraHeaders.get(key);
    }

    /**
     * Returns a copy of the extra headers.
     * @return the map that holds the headers
     */
    public Map<String, String> getExtraHeaders() {
        Map<String, String> clone = new HashMap<>();
        clone.putAll(extraHeaders);
        return clone;
    }

    /**
     * Adds a WilmaHttpHeader to the list of extra headers to be removed.
     * @param key key of the HTTP header
     */
    public void addExtraHeaderToRemove(final String key) {
        extraHeadersToRemove.put(key, key);
    }

    /**
     * Returns the extra header to be removed with the given key.
     * @param key key of the header to get
     * @return the header value
     */
    public String getExtraHeaderToRemove(final String key) {
        return extraHeadersToRemove.get(key);
    }

    /**
     * Returns a copy of the extra headers to be removed.
     * @return the map that holds the headers
     */
    public Map<String, String> getExtraHeadersToRemove() {
        Map<String, String> clone = new HashMap<>();
        clone.putAll(extraHeadersToRemove);
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

}
