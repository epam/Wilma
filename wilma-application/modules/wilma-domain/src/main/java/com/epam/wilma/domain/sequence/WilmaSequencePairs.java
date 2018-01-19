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

import com.epam.wilma.domain.http.WilmaHttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class stores the messages of a sequence in a collection and provides some method to modify this collection.
 * @author Tibor_Kovacs
 *
 */
public class WilmaSequencePairs {

    private final Map<String, RequestResponsePair> messages;

    /**
     * Constructs a new instance of {@link WilmaSequencePairs}.
     */
    public WilmaSequencePairs() {
        this.messages = new ConcurrentHashMap<>();
    }

    /**
     * Put a WilmaHttpRequestResponsePair in the collection of messages.
     * @param loggerId is that ID which was given by Wilma
     * @param pair is that pair what you want to put into the store.
     */
    public void addStore(final String loggerId, final RequestResponsePair pair) {
        messages.put(loggerId, pair);
    }

    public Map<String, RequestResponsePair> getMessages() {
        return new HashMap<>(messages);
    }

    /**
     * Looks for the right WilmaHttpRequestResponsePair and sets its response.
     * @param response is the given response
     */
    public void putIntoMessages(final WilmaHttpResponse response) {
        String searchLoggerId = response.getWilmaMessageId();
        RequestResponsePair pair = messages.get(searchLoggerId);
        if (pair != null) {
            pair.setResponse(response);
        }
    }

}
