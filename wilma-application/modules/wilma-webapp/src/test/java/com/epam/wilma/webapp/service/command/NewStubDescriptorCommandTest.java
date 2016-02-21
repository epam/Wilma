package com.epam.wilma.webapp.service.command;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.stubconfig.StubDescriptorFactory;

/**
 * Provides unit tests for the class {@link NewStubDescriptorCommand}.
 * @author Tibor_Kovacs
 *
 */
public class NewStubDescriptorCommandTest {
    private static final String GROUPNAME_FIRST = "First";

    private Map<String, StubDescriptor> normalStubDescriptors;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StubDescriptor stubDescriptor;
    @Mock
    private InputStream inputStream;
    @Mock
    private StubDescriptorFactory stubConfigurationBuilder;
    @Mock
    private StubDescriptorAttributes attributes;
    @Mock
    private SequenceDescriptorHolder sequenceDescriptorHolder;

    private NewStubDescriptorCommand underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        normalStubDescriptors = new LinkedHashMap<>();
        given(stubConfigurationBuilder.buildStubDescriptor(inputStream)).willReturn(stubDescriptor);
    }

    @Test
    public void testModifyShouldPutANewStubDescriptorIntoTheMap() throws ClassNotFoundException {
        //GIVEN
        Map<String, StubDescriptor> expected = new LinkedHashMap<>(normalStubDescriptors);
        expected.put(GROUPNAME_FIRST, stubDescriptor);
        given(stubDescriptor.getAttributes()).willReturn(attributes);
        given(attributes.getGroupName()).willReturn(GROUPNAME_FIRST);
        //WHEN
        underTest = new NewStubDescriptorCommand(inputStream, stubConfigurationBuilder, sequenceDescriptorHolder);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testModifyShouldReplaceOneStubDescriptor() throws ClassNotFoundException {
        //GIVEN
        normalStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
        Map<String, StubDescriptor> expected = new LinkedHashMap<>(normalStubDescriptors);
        given(stubDescriptor.getAttributes()).willReturn(attributes);
        given(attributes.getGroupName()).willReturn(GROUPNAME_FIRST);
        //WHEN
        underTest = new NewStubDescriptorCommand(inputStream, stubConfigurationBuilder, sequenceDescriptorHolder);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testModifyShouldAddSequenceDescriptorsToSequenceDescriptorHolder() throws ClassNotFoundException {
        //GIVEN
        //WHEN
        underTest = new NewStubDescriptorCommand(inputStream, stubConfigurationBuilder, sequenceDescriptorHolder);
        underTest.modify(normalStubDescriptors);
        //THEN
        verify(sequenceDescriptorHolder).addAllSequenceDescriptors(stubDescriptor);
    }

}
