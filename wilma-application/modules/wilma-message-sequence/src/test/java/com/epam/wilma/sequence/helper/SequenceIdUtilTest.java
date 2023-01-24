package com.epam.wilma.sequence.helper;
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

import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link SequenceIdUtil}.
 *
 * @author Tibor_Kovacs
 */
public class SequenceIdUtilTest {

    @Mock
    private SequenceDescriptor sequenceDescriptor;
    @Mock
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    private final SequenceIdUtil underTest = new SequenceIdUtil();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDescriptorKey() {
        //GIVEN
        String sequenceId = "TestGroup_testDescriptor_1|abc";
        //WHEN
        String result = underTest.getDescriptorKey(sequenceId);
        //THEN
        assertEquals("TestGroup_testDescriptor_1", result);
    }

    @Test
    public void testGetHandlerKey() {
        //GIVEN
        String sequenceId = "TestGroup_testDescriptor_1|abc";
        //WHEN
        String result = underTest.getHandlerKey(sequenceId);
        //THEN
        assertEquals("abc", result);
    }

    @Test
    public void testCreateSequenceId() {
        //GIVEN
        String handlerKey = "Key";
        given(sequenceDescriptor.getGroupName()).willReturn("groupname");
        given(sequenceDescriptor.getName()).willReturn("name");
        given(sequenceDescriptorKeyUtil.createDescriptorKey("groupname", "name")).willReturn("groupname_name");
        ReflectionTestUtils.setField(underTest, "sequenceDescriptorKeyUtil", sequenceDescriptorKeyUtil);
        //WHEN
        String result = underTest.createSequenceId(handlerKey, sequenceDescriptor);
        //THEN
        assertEquals("groupname_name|Key", result);
    }
}
