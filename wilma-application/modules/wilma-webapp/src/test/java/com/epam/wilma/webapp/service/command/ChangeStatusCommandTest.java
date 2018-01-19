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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Provides unit tests for the class {@link ChangeStatusCommand}.
 * @author Tibor_Kovacs
 *
 */
public class ChangeStatusCommandTest {
    private static final String GROUPNAME_FIRST = "First";
    private Map<String, StubDescriptor> normalStubDescriptors;
    private StubDescriptorAttributes attributes;

    private StubDescriptor stubDescriptor;
    @Mock
    private HttpServletRequest request;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    private ChangeStatusCommand underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        normalStubDescriptors = new LinkedHashMap<>();
        attributes = new StubDescriptorAttributes(GROUPNAME_FIRST);
        stubDescriptor = new StubDescriptor(attributes, new ArrayList<DialogDescriptor>(), new ArrayList<InterceptorDescriptor>(), null);
        normalStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
    }

    @Test
    public void testSetStatusShouldDisableTheSelectedStubDescriptor() {
        //GIVEN in setUp
        normalStubDescriptors = new LinkedHashMap<>();
        attributes = new StubDescriptorAttributes(GROUPNAME_FIRST, true);
        stubDescriptor = new StubDescriptor(attributes, new ArrayList<DialogDescriptor>(), new ArrayList<InterceptorDescriptor>(), null);
        normalStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
        //WHEN
        underTest = new ChangeStatusCommand(false, GROUPNAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        StubDescriptor resultDescriptor = result.get(GROUPNAME_FIRST);
        Assert.assertNotNull(resultDescriptor);
        boolean resultAttribute = resultDescriptor.getAttributes().isActive();
        Assert.assertFalse(resultAttribute);
    }

    @Test
    public void testSetStatusShouldEnableTheSelectedStubDescriptor() {
        //GIVEN in setUp
        normalStubDescriptors = new LinkedHashMap<>();
        attributes = new StubDescriptorAttributes(GROUPNAME_FIRST, false);
        stubDescriptor = new StubDescriptor(attributes, new ArrayList<DialogDescriptor>(), new ArrayList<InterceptorDescriptor>(), null);
        normalStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
        //WHEN
        underTest = new ChangeStatusCommand(true, GROUPNAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        StubDescriptor resultDescriptor = result.get(GROUPNAME_FIRST);
        Assert.assertNotNull(resultDescriptor);
        boolean resultAttribute = resultDescriptor.getAttributes().isActive();
        Assert.assertTrue(resultAttribute);
    }
}
