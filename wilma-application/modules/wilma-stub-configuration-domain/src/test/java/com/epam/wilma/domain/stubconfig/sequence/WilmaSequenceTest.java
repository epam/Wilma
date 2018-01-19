package com.epam.wilma.domain.stubconfig.sequence;

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

import static org.mockito.BDDMockito.given;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.sequence.WilmaSequencePairs;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Unit test for {@link WilmaSequence}.
 * @author Adam_Csaba_Kiraly
 */
public class WilmaSequenceTest {

    private static final String STUBBED_REQUEST_LOGGER_ID = "id";
    private WilmaSequence underTest;
    private final WilmaSequencePairs messageStore = Mockito.mock(WilmaSequencePairs.class);
    private final boolean isVolatile = false;

    @BeforeMethod
    public void setUp() throws Exception {
        String sequenceKey = "key";
        Timestamp timestamp = new Timestamp(10);
        underTest = new WilmaSequence(sequenceKey, timestamp, messageStore);
    }

    @Test
    public void testCheckShouldNotLookForResponsePairOfTheGivenLoggerId() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        RequestResponsePair pair = new RequestResponsePair(new WilmaHttpRequest());
        messages.put(STUBBED_REQUEST_LOGGER_ID, pair);
        given(messageStore.getMessages()).willReturn(messages);
        //WHEN
        boolean result = underTest.checkIfAllResponsesArrived(STUBBED_REQUEST_LOGGER_ID);
        //THEN
        assertTrue(result);
    }

    @Test
    public void testCheckShouldReturnFalseWhenAResponseIsNull() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new TreeMap<>();
        RequestResponsePair pair = new RequestResponsePair(new WilmaHttpRequest());
        pair.setResponse(new WilmaHttpResponse(isVolatile));
        RequestResponsePair pair2 = new RequestResponsePair(new WilmaHttpRequest());
        messages.put("id2", pair);
        messages.put("id3", pair2);
        given(messageStore.getMessages()).willReturn(messages);
        //WHEN
        boolean result = underTest.checkIfAllResponsesArrived(STUBBED_REQUEST_LOGGER_ID);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testCheckShouldReturnTrueWhenNoneOfTheResponsesAreNull() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new TreeMap<>();
        RequestResponsePair pair = new RequestResponsePair(new WilmaHttpRequest());
        pair.setResponse(new WilmaHttpResponse(isVolatile));
        RequestResponsePair pair2 = new RequestResponsePair(new WilmaHttpRequest());
        pair2.setResponse(new WilmaHttpResponse(isVolatile));
        messages.put("id2", pair);
        messages.put("id3", pair2);
        given(messageStore.getMessages()).willReturn(messages);
        //WHEN
        boolean result = underTest.checkIfAllResponsesArrived(STUBBED_REQUEST_LOGGER_ID);
        //THEN
        assertTrue(result);
    }
}
