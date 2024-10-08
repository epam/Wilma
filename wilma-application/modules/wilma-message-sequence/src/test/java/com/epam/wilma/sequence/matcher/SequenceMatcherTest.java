package com.epam.wilma.sequence.matcher;
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
import com.epam.wilma.sequence.SequenceManager;
import com.epam.wilma.sequence.helper.SequenceIdUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for the class {@link SequenceMatcher}.
 *
 * @author Tibor_Kovacs
 */
public class SequenceMatcherTest {
    private final String[] keys = {"Seq1", "Seq2"};
    @Mock
    private SequenceManager manager;
    @Mock
    private WilmaSequence sequence;
    @Mock
    private SequenceIdUtil sequenceKeyResolver;

    @InjectMocks
    private SequenceMatcher underTest;

    private Map<String, WilmaSequence> sequences;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sequences = new HashMap<>();
        sequences.put(keys[1], sequence);
        given(sequenceKeyResolver.getHandlerKey(Mockito.anyString())).willReturn("");
    }

    @Test
    public void testMatchSequenceKeyWithDescriptorWhenDescriptorNameIsWrong() {
        //GIVEN
        String descriptorName = "something";
        given(manager.getSequences(descriptorName)).willReturn(new HashMap<>());
        //WHEN
        WilmaSequence result = underTest.matchSequenceKeyWithDescriptor(descriptorName, keys);
        //THEN
        assertNull(result);
    }

    @Test
    public void testMatchSequenceKeyWithDescriptorShouldReturnWithTheSearchedSequence() {
        //GIVEN
        String descriptorName = "test";
        given(manager.getSequences(descriptorName)).willReturn(sequences);
        given(sequenceKeyResolver.getHandlerKey(Mockito.anyString())).willReturn("").willReturn(keys[1]);
        //WHEN
        WilmaSequence result = underTest.matchSequenceKeyWithDescriptor(descriptorName, keys);
        //THEN
        assertNotNull(result);
    }

    @Test
    public void testMatchSequenceKeyWithDescriptorShouldReturnWithNullWhenKeysAreWrong() {
        //GIVEN
        String descriptorName = "test";
        sequences = new HashMap<>();
        sequences.put("something", sequence);
        given(manager.getSequences(descriptorName)).willReturn(sequences);
        //WHEN
        WilmaSequence result = underTest.matchSequenceKeyWithDescriptor(descriptorName, keys);
        //THEN
        assertNull(result);
    }
}
