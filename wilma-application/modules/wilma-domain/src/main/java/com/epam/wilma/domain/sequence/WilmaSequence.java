package com.epam.wilma.domain.sequence;

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

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * This class represents a sequence.
 * @author Tibor_Kovacs
 *
 */
public class WilmaSequence {

    private final AtomicReference<Timestamp> timestamp = new AtomicReference<>();
    private final String sequenceKey;
    private final WilmaSequencePairs messageStore;

    /**
     * Constructs a new instance of {@link WilmaSequence}.
     * @param sequenceKey is the key which was created by a SequenceHandler
     * @param timestamp shows the time in milliseconds when the sequence will be expired
     * @param messageStore contains the collection of {@link RequestResponsePair}s.
     */
    public WilmaSequence(final String sequenceKey, final Timestamp timestamp, final WilmaSequencePairs messageStore) {
        this.sequenceKey = sequenceKey;
        this.timestamp.set(timestamp);
        this.messageStore = messageStore;
    }

    public String getSequenceKey() {
        return sequenceKey;
    }

    /**
     * This method give back the the collection of {@link RequestResponsePair}s of the store.
     * @return with an UnmodifiableList of {@link RequestResponsePair}s
     */
    public Map<String, RequestResponsePair> getPairs() {
        return messageStore.getMessages();
    }

    /**
     * Shows whether the sequence is expired or not.
     * @param actualTimestamp is the timestamp of current time.
     * @return If the sequence is expired, its timestamp is before than the given timestamp.
     */
    public boolean isExpired(final Timestamp actualTimestamp) {
        return this.timestamp.get().before(actualTimestamp);
    }

    /**
     * This method sets the timestamp attribute in atomic way.
     * @param timestamp is the new timestamp
     */
    public void setTimeout(final Timestamp timestamp) {
        this.timestamp.set(timestamp);
    }

    /**
     * Put a WilmaHttpRequestResponsePair into messageStore.
     * @param loggerId is that ID which was given by Wilma
     * @param pair is the new WilmaHttpRequestResponsePair object which contains the arrived request
     */
    public void addPair(final String loggerId, final RequestResponsePair pair) {
        messageStore.addStore(loggerId, pair);
    }

    /**
     * Put the given response into the messageStore.
     * @param response is the copy of the response
     */
    public void addResponseToPair(final WilmaHttpResponse response) {
        messageStore.putIntoMessages(response);
    }

    /**
     * Checks if all of the requests' responses arrived with the exception of the request with the given loggerId.
     * @param loggerId the given loggerId
     * @return true if all of the responses arrived (with the exception of the request's with the given loggerId), false otherwise
     */
    public boolean checkIfAllResponsesArrived(final String loggerId) {
        Map<String, RequestResponsePair> pairs = getPairs();
        pairs.remove(loggerId);
        Iterator<RequestResponsePair> pairIterator = pairs.values().iterator();
        boolean result = true;
        while (pairIterator.hasNext() && result) {
            RequestResponsePair pair = pairIterator.next();
            result = pair.getResponse() != null;
        }
        return result;
    }

}
