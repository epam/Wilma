package com.epam.wilma.sequence.cleaner;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.sequence.WilmaSequence;

/**
 * Provides unit tests for the class {@link SequenceCleaner}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceCleanerTest {

    @Mock
    private CurrentDateProvider dateProvider;
    @Mock
    private SequenceDescriptor descriptor;
    @Mock
    private WilmaSequence firstSequence;
    @Mock
    private WilmaSequence secondSequence;

    private Map<String, SequenceDescriptor> descriptors;

    @InjectMocks
    private SequenceCleaner underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(dateProvider.getCurrentTimeInMillis()).willReturn(1000L);
        descriptors = new ConcurrentHashMap<>();
    }

    @Test
    public void testCleanTheExpiredSequences() {
        //GIVEN
        descriptors.put("TestKey1", descriptor);
        List<WilmaSequence> sequences = new ArrayList<>();
        sequences.add(firstSequence);
        sequences.add(secondSequence);
        given(descriptor.getSequencesInCollection()).willReturn(sequences);
        given(firstSequence.isExpired(new Timestamp(1000L))).willReturn(false);
        given(secondSequence.isExpired(new Timestamp(1000L))).willReturn(true);
        //WHEN
        underTest.cleanTheExpiredSequences(descriptors);
        //THEN
        Assert.assertEquals(sequences.size(), 1);
    }

}
