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

import static org.mockito.BDDMockito.given;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;

/**
 * Provides unit tests for the class {@link SequenceIdUtil}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceIdUtilTest {

    @Mock
    private SequenceDescriptor sequenceDescriptor;
    @Mock
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    private SequenceIdUtil underTest = new SequenceIdUtil();

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDescriptorKey() {
        //GIVEN
        String sequenceId = "TestGroup_testDescriptor_1|abc";
        //WHEN
        String result = underTest.getDescriptorKey(sequenceId);
        //THEN
        Assert.assertEquals(result, "TestGroup_testDescriptor_1");
    }

    @Test
    public void testGetHandlerKey() {
        //GIVEN
        String sequenceId = "TestGroup_testDescriptor_1|abc";
        //WHEN
        String result = underTest.getHandlerKey(sequenceId);
        //THEN
        Assert.assertEquals(result, "abc");
    }

    @Test
    public void testCreateSequenceId() {
        //GIVEN
        String handlerKey = "Key";
        given(sequenceDescriptor.getGroupName()).willReturn("groupname");
        given(sequenceDescriptor.getName()).willReturn("name");
        given(sequenceDescriptorKeyUtil.createDescriptorKey("groupname", "name")).willReturn("groupname_name");
        Whitebox.setInternalState(underTest, "sequenceDescriptorKeyUtil", sequenceDescriptorKeyUtil);
        //WHEN
        String result = underTest.createSequenceId(handlerKey, sequenceDescriptor);
        //THEN
        Assert.assertEquals(result, "groupname_name|Key");
    }
}
