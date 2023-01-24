package com.epam.wilma.sequence.factory;
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

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.domain.sequence.WilmaSequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link SequenceFactory}.
 *
 * @author Tibor_Kovacs
 */
public class SequenceFactoryTest {

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private CurrentDateProvider dateProvider;

    @InjectMocks
    private SequenceFactory underTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        given(dateProvider.getCurrentTimeInMillis()).willReturn(1000L);
    }

    @Test
    public void testCreateNewSequence() {
        //GIVEN
        String testKey = "TestKey";
        String loggerId = "testId";
        given(request.getWilmaMessageId()).willReturn(loggerId);
        //WHEN
        WilmaSequence result = underTest.createNewSequence(testKey, request, 1000L);
        //THEN
        assertEquals(testKey, result.getSequenceKey());
        assertFalse(result.isExpired(new Timestamp(1000L)));
        RequestResponsePair firstPair = result.getPairs().get(loggerId);
        assertEquals(request, firstPair.getRequest());
    }
}
