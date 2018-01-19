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

import com.epam.wilma.domain.evaluation.Evaluable;
import com.epam.wilma.domain.sequence.WilmaSequence;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is Wilma's representation of an HTTP request.
 * @author Marton_Sereg, Tamas Kohegyi
 *
 */
public class WilmaHttpRequest extends WilmaHttpEntity {

    private URI uri;

    private boolean rerouted;
    private boolean responseVolatile; //setting of volatility of the response message

    private Map<Evaluable, Boolean> evaluationResults = new HashMap<>();
    private WilmaSequence sequence;

    /**
     * This method adds the given sequenceId to the extra headers.
     * @param sequenceId is the given sequence key.
     */
    public void addSequenceId(final String sequenceId) {
        addHeaderUpdate(WILMA_SEQUENCE_ID, sequenceId);
    }

    public String getSequenceId() {
        return getHeaderUpdateValue(WILMA_SEQUENCE_ID);
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

    public boolean isResponseVolatile() {
        return responseVolatile;
    }

    public void setResponseVolatile(boolean responseVolatile) {
        this.responseVolatile = responseVolatile;
    }

    public WilmaSequence getSequence() {
        return sequence;
    }

    public void setSequence(WilmaSequence sequence) {
        this.sequence = sequence;
    }
}
