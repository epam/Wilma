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

import com.epam.wilma.domain.evaluation.Evaluable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is Wilma's representation of an HTTP request.
 * @author Marton_Sereg, Tamas Kohegyi
 *
 */
public class WilmaHttpRequest extends WilmaHttpEntity {

    private String requestLine;

    private URI uri;

    private boolean rerouted;

    //holder of extra headers to be added to the request
    private final Map<String, String> extraHeaders = new HashMap<>();
    //holder of extra headers to be removed from the request
    private final Map<String, String> extraHeadersToRemove = new HashMap<>();

    private Map<Evaluable, Boolean> evaluationResults = new HashMap<>();

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
     * This method adds the given sequenceId to the extra headers.
     * @param sequenceId is the given sequence key.
     */
    public void addSequenceId(final String sequenceId) {
        addExtraHeader(WILMA_SEQUENCE_ID, sequenceId);
    }

    public String getSequenceId() {
        return extraHeaders.get(WILMA_SEQUENCE_ID);
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

    //method + target url + protocol
    public String getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(final String requestLine) {
        this.requestLine = requestLine;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(final URI uri) {
        this.uri = uri;
    }

    public boolean isRerouted() {
        return rerouted;
    }

    public void setRerouted(final boolean rerouted) {
        this.rerouted = rerouted;
    }

    /**
     * Removes and returns the given dialog descriptor's evaluation result for the request.
     * @param dialogDescriptor the given dialog descriptor
     * @return the result of the evaluation, or null if this request wasn't evaluated with the given dialog descriptor
     */
    public Boolean popEvaluationResult(final Evaluable dialogDescriptor) {
        return evaluationResults.remove(dialogDescriptor);
    }

    /**
     * Stores the request's evaluation result.
     * @param dialogDescriptor the given dialog descriptor that evaluated the request
     * @param result evaluation of the request by the given dialog descriptor
     */
    public void pushEvaluationResult(final Evaluable dialogDescriptor, final boolean result) {
        evaluationResults.put(dialogDescriptor, result);
    }

    /**
     * Clears the evaluation results by setting it to null.
     */
    public void clearEvaluationResults() {
        evaluationResults = null;
    }

}
