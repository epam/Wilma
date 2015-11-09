package com.epam.wilma.domain.http;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is Wilma's representation of HTTP package.
 * @author Tamas_Bihari
 *
 */
public class WilmaHttpEntity implements Serializable {

    public static final String WILMA_SEQUENCE_ID = "Wilma-Sequence";
    public static final String WILMA_LOGGER_ID = "Wilma-Logger-ID";

    private String body;
    private InputStream inputStream;
    private final Map<String, String> headers = new HashMap<>();
    private String wilmaMessageId;

    /**
     * Adds a WilmaHttpHeader to the list of headers.
     * @param key key of the HTTP header
     * @param value value of the HTTP header
     */
    public void addHeader(final String key, final String value) {
        headers.put(key, value);
    }

    /**
     * Returns the header with the given key.
     * @param key key of the header to get
     * @return the header value
     */
    public String getHeader(final String key) {
        return headers.get(key);
    }

    /**
     * Returns a copy of the headers.
     * @return the map that holds the headers
     */
    public Map<String, String> getHeaders() {
        Map<String, String> clone = new HashMap<>();
        clone.putAll(headers);
        return clone;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setWilmaMessageId(final String wilmaMessageId) { this.wilmaMessageId = wilmaMessageId; }

    public String getWilmaMessageId() { return wilmaMessageId; }

    /**
     * Gets the ID added by Wilma to the message, regardless if it is a request or response.
     * @return with the unique ID of the message.
     */
    public String getWilmaMessageLoggerId() {
        String loggerID = null;
        if (this instanceof WilmaHttpResponse) {
            loggerID = getWilmaMessageId() + "resp";
        }
        if (this instanceof WilmaHttpRequest) {
            loggerID = getWilmaMessageId() + "req";
        }
        return loggerID;
    }
}
