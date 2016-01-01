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

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is Wilma's representation of HTTP package.
 *
 * @author Tamas_Bihari
 */
public class WilmaHttpEntity implements Serializable {

    public static final String WILMA_SEQUENCE_ID = "Wilma-Sequence";
    public static final String WILMA_LOGGER_ID = "Wilma-Logger-ID";
    private final Map<String, String> headers = new HashMap<>();
    private String body;
    private String newBody;
    private InputStream inputStream;
    private String wilmaMessageId;
    private String wilmaMessageCustomPostfix;
    // ip of the source/client
    private String remoteAddr;
    private boolean loggingEnabled = true; //whether the logging of this specific message is enabled or not, default: enabled

    /**
     * Adds a WilmaHttpHeader to the list of headers.
     *
     * @param key   key of the HTTP header
     * @param value value of the HTTP header
     */
    public void addHeader(final String key, final String value) {
        headers.put(key, value);
    }

    /**
     * Returns the header with the given key.
     *
     * @param key key of the header to get
     * @return the header value
     */
    public String getHeader(final String key) {
        return headers.get(key);
    }

    /**
     * Returns a copy of the headers.
     *
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

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(final String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getWilmaMessageId() {
        return wilmaMessageId;
    }

    public void setWilmaMessageId(final String wilmaMessageId) {
        this.wilmaMessageId = wilmaMessageId;
    }

    /**
     * Gets the ID added by Wilma to the message, regardless if it is a request or response.
     *
     * @return with the unique ID of the message to be logged.
     */
    public String getWilmaMessageLoggerId() {
        String loggerID = null;
        if (this instanceof WilmaHttpResponse) {
            loggerID = getWilmaMessageId() + "resp" + generatePostfix();
        }
        if (this instanceof WilmaHttpRequest) {
            loggerID = getWilmaMessageId() + "req" + generatePostfix();
        }
        return loggerID;
    }

    private String generatePostfix() {
        String postfix = getWilmaMessageCustomPostfix();
        if (postfix != null) {
            postfix = "_" + postfix;  // "_postfix
        } else {
            postfix = "";
        }
        return postfix;
    }

    public String getWilmaMessageCustomPostfix() {
        return wilmaMessageCustomPostfix;
    }

    public void setWilmaMessageCustomPostfix(final String wilmaMessageCustomPostfix) {
        this.wilmaMessageCustomPostfix = wilmaMessageCustomPostfix;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(final boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    public String getNewBody() {
        return newBody;
    }

    /**
     * Modifies the message body on-the-fly. Works with limitations only (plain text request was tested only,
     * without any compression method.
     *
     * @param newBody is the new message content
     */
    public void setNewBody(String newBody) {
        this.newBody = newBody;
        this.body = this.newBody;
    }
}
