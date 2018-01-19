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

import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeRemoved;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is Wilma's representation of HTTP package.
 *
 * @author Tamas_Bihari, Tamas_Kohegyi
 */
public class WilmaHttpEntity implements Serializable {

    public static final String WILMA_SEQUENCE_ID = "Wilma-Sequence";
    public static final String WILMA_LOGGER_ID = "Wilma-Logger-ID";
    //holder of the request line
    private String requestLine;
    //holder of headers of the message (req or resp)
    private final Map<String, String> headers = new HashMap<>();
    //holder of header changes
    private final Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
    private String body;
    private byte[] newBody;
    private InputStream inputStream;
    private String wilmaMessageId;
    private String wilmaMessageCustomPostfix;
    // ip of the source/client
    private String remoteAddr;
    private boolean loggingEnabled = true; //whether the logging of this specific message is enabled or not, default: enabled

    //method + target url + protocol
    public String getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(final String requestLine) {
        this.requestLine = requestLine;
    }

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

    /**
     * Set the body - beware that this does not update the request/response, just updates the body that will be written
     * into the message log file. In case you would like to alter the request/response body, use the setNewBody method.
     * in addition, in case you would like to alter the response body, you must enable response volatility in advance,
     * either globally, or by intercepting the message request and set the response volatility there.
     *
     * @param body is the body content.
     */
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

    public byte[] getNewBody() {
        return newBody;
    }

    /**
     * Modifies the message body on-the-fly. Works with limitations only (plain text request was tested only,
     * without any compression method).
     * The two parameter must have the same meaning, first is ready to be delivered as request/response,
     * second is human readable (or not).
     * Beware that response body modification takes effect only if the response is volatile.
     *
     * @param newBody is the new message content, should be well prepared (so gzipped, FIS compressed) as it is necessary.
     * @param newBodyReadable is the human readable body of the message, will be used in log
     */
    public void setNewBody(byte[] newBody, String newBodyReadable) {
        this.newBody = newBody;
        this.body = newBodyReadable;
    }

    /**
     * Adds a Http header change request.
     * @param key key of the HTTP header
     * @param value value of the HTTP header
     */
    public void addHeaderUpdate(final String key, final String value) {
        HttpHeaderToBeUpdated headerToBeUpdated = new HttpHeaderToBeUpdated(value);
        addHeaderChange(key, headerToBeUpdated);
    }

    /**
     * Adds a Http header remove request.
     * @param key key of the HTTP header to be removed
     */
    public void addHeaderRemove(final String key) {
        HttpHeaderToBeRemoved headerToBeRemoved = new HttpHeaderToBeRemoved();
        headerChanges.put(key, headerToBeRemoved);
    }

    /**
     * Returns with the new value of a Http header change request with the given key.
     * @param key key of the header to get
     * @return the header value
     */
    public String getHeaderUpdateValue(final String key) {
        String value = null;
        HttpHeaderChange headerChange = headerChanges.get(key);
        if (headerChange instanceof HttpHeaderToBeUpdated) {
            value = ((HttpHeaderToBeUpdated) headerChange).getNewValue();
        }
        return value;
    }

    public Map<String, HttpHeaderChange> getHeaderChanges() {
        return headerChanges;
    }

    /**
     * Add a new Http header change request. The request may be either:
     * - HttpHeaderToBeUpdated, to update an existing, or add a new header, or
     * - HttpHeaderToBeDeleted, when you would like to remove a header.
     *
     * @param key is the name of the header
     * @param value defines the meaning of the change (add+change / remove).
     */
    public void addHeaderChange(String key, HttpHeaderChange value) {
        headerChanges.put(key, value);
    }
}
