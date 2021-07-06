package com.epam.wilma.extras.shortcircuit;
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

import com.epam.wilma.domain.http.WilmaHttpResponse;

import java.util.Map;

/**
 * This class holds a single response information, to be usageCount in stub response, and captured from a real response.
 *
 * @author tkohegyi, created on 2016. 02. 17
 */
class ShortCircuitResponseInformation {

    private String hashCode;
    private String contentType;
    private int statusCode;
    private String body;
    private Map<String, String> headers;

    private long timeout;
    private long usageCount;

    /**
     * Creates a new response information, based on the original response, and specifying a timeout.
     * Timeout value is the system time, when this response become obsolete.
     *
     * @param wilmaHttpResponse is the original response object
     * @param timeout           is the calculated timeout value
     * @param hashCode          is the header hash code of the message
     */
    ShortCircuitResponseInformation(WilmaHttpResponse wilmaHttpResponse, long timeout, String hashCode) {
        //need to clone the response perfectly
        setContentType(wilmaHttpResponse.getContentType());
        setStatusCode(wilmaHttpResponse.getStatusCode());
        setBody(wilmaHttpResponse.getBody());
        Map<String, String> headers = wilmaHttpResponse.getHeaders();
        //These two header is not necessary to be preserved
        headers.remove("Content-Encoding");
        headers.remove("Content-Length");
        headers.remove("Content-Type");
        headers.remove("Server");
        setHeaders(headers);
        setHashCode(hashCode);
        this.timeout = timeout;
        usageCount = 0;
    }

    /**
     * Creates a new empty response information, just the timeout is specified.
     *
     * @param timeout is the calculated timeout value
     */
    ShortCircuitResponseInformation(long timeout) {
        this.timeout = timeout;
    }

    long getTimeout() {
        return timeout;
    }

    public String getHashCode() {
        return hashCode;
    }

    void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    String getContentType() {
        return contentType;
    }

    void setContentType(String contentType) {
        this.contentType = contentType;
    }

    int getStatusCode() {
        return statusCode;
    }

    void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    String getBody() {
        return body;
    }

    void setBody(String body) {
        this.body = body;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    void increaseUsageCount() {
        usageCount++;
    }

    long getUsageCount() {
        return usageCount;
    }
}
