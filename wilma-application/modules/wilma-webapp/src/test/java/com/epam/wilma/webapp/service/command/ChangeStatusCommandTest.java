package com.epam.wilma.webapp.service.command;
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

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provides unit tests for the class {@link ChangeStatusCommand}.
 *
 * @author Tibor_Kovacs
 */
public class ChangeStatusCommandTest {
    private static final String GROUP_NAME_FIRST = "First";
    private Map<String, StubDescriptor> normalStubDescriptors;
    private StubDescriptorAttributes attributes;

    private StubDescriptor stubDescriptor;
    @Mock
    private HttpServletRequest request;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    private ChangeStatusCommand underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        normalStubDescriptors = new LinkedHashMap<>();
        attributes = new StubDescriptorAttributes(GROUP_NAME_FIRST);
        stubDescriptor = new StubDescriptor(attributes, new ArrayList<>(), new ArrayList<>(), null);
        normalStubDescriptors.put(GROUP_NAME_FIRST, stubDescriptor);
    }

    @Test
    public void testSetStatusShouldDisableTheSelectedStubDescriptor() {
        //GIVEN in setUp
        normalStubDescriptors = new LinkedHashMap<>();
        attributes = new StubDescriptorAttributes(GROUP_NAME_FIRST, true);
        stubDescriptor = new StubDescriptor(attributes, new ArrayList<>(), new ArrayList<>(), null);
        normalStubDescriptors.put(GROUP_NAME_FIRST, stubDescriptor);
        //WHEN
        underTest = new ChangeStatusCommand(false, GROUP_NAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        StubDescriptor resultDescriptor = result.get(GROUP_NAME_FIRST);
        assertNotNull(resultDescriptor);
        boolean resultAttribute = resultDescriptor.getAttributes().isActive();
        assertFalse(resultAttribute);
    }

    @Test
    public void testSetStatusShouldEnableTheSelectedStubDescriptor() {
        //GIVEN in setUp
        normalStubDescriptors = new LinkedHashMap<>();
        attributes = new StubDescriptorAttributes(GROUP_NAME_FIRST, false);
        stubDescriptor = new StubDescriptor(attributes, new ArrayList<>(), new ArrayList<>(), null);
        normalStubDescriptors.put(GROUP_NAME_FIRST, stubDescriptor);
        //WHEN
        underTest = new ChangeStatusCommand(true, GROUP_NAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        StubDescriptor resultDescriptor = result.get(GROUP_NAME_FIRST);
        assertNotNull(resultDescriptor);
        boolean resultAttribute = resultDescriptor.getAttributes().isActive();
        assertTrue(resultAttribute);
    }
}
