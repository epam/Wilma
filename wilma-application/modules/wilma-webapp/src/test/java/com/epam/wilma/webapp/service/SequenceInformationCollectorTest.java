package com.epam.wilma.webapp.service;

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

import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.sequence.SequenceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Units test for {@link SequenceInformationCollector}.
 *
 * @author Tibor_Kovacs
 */
public class SequenceInformationCollectorTest {

    @Mock
    private SequenceManager sequenceManager;
    @Mock
    private SequenceDescriptor descriptor;
    @Mock
    private WilmaSequence sequence;

    @InjectMocks
    private SequenceInformationCollector underTest;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCollectInformationWhenThereIsNoDescriptor() {
        //GIVEN
        given(sequenceManager.getDescriptors()).willReturn(new HashMap<String, SequenceDescriptor>());
        //WHEN
        Map<String, Object> result = underTest.collectInformation();
        //THEN
        int sum = (int) result.get(SequenceInformationCollector.SUM_KEY);
        assertEquals(0, sum);
        Map<String, Integer> groups = (Map<String, Integer>) result.get(SequenceInformationCollector.GROUPS_KEY);
        assertEquals(0, groups.size());
    }

    @Test
    public void testCollectInformationWhenThereAreDescriptors() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new HashMap<>();
        descriptors.put("test", descriptor);
        Map<String, WilmaSequence> sequences = new HashMap<>();
        sequences.put("testKey", sequence);
        given(descriptor.getSequences()).willReturn(sequences);
        given(sequenceManager.getDescriptors()).willReturn(descriptors);
        //WHEN
        Map<String, Object> result = underTest.collectInformation();
        //THEN
        int sum = (int) result.get(SequenceInformationCollector.SUM_KEY);
        assertEquals(1, sum);
        Map<String, Integer> groups = (Map<String, Integer>) result.get(SequenceInformationCollector.GROUPS_KEY);
        assertEquals(1, groups.size());
    }
}
