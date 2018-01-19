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

import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.sequence.SequenceManager;

/**
 * Units test for {@link SequenceInformationCollector}.
 * @author Tibor_Kovacs
 *
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

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCollectInformationsWhenThereIsNoDescriptor() {
        //GIVEN
        given(sequenceManager.getDescriptors()).willReturn(new HashMap<String, SequenceDescriptor>());
        //WHEN
        Map<String, Object> result = underTest.collectInformation();
        //THEN
        int sum = (int) result.get(SequenceInformationCollector.SUM_KEY);
        Assert.assertEquals(sum, 0);
        Map<String, Integer> groups = (Map<String, Integer>) result.get(SequenceInformationCollector.GROUPS_KEY);
        Assert.assertEquals(groups.size(), 0);
    }

    @Test
    public void testCollectInformationsWhenThereAreDescriptors() {
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
        Assert.assertEquals(sum, 1);
        Map<String, Integer> groups = (Map<String, Integer>) result.get(SequenceInformationCollector.GROUPS_KEY);
        Assert.assertEquals(groups.size(), 1);
    }
}
