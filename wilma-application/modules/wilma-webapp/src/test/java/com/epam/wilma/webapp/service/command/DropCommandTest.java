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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Provides unit tests for the class {@link DropCommand}.
 * @author Tibor_Kovacs
 *
 */
public class DropCommandTest {
    private static final String GROUPNAME_FIRST = "First";
    private static final String GROUPNAME_SECOND = "Second";

    private Map<String, StubDescriptor> normalStubDescriptors;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StubDescriptor stubDescriptor;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Mock
    private HttpServletRequest request;

    private DropCommand underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        normalStubDescriptors = new LinkedHashMap<>();
        normalStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
        normalStubDescriptors.put(GROUPNAME_SECOND, stubDescriptor);
    }

    @Test
    public void testDropShouldRemoveTheSelectedStubConfiguration() {
        //GIVEN in setUp
        Map<String, StubDescriptor> expected = normalStubDescriptors;
        expected.remove(GROUPNAME_FIRST);
        //WHEN
        underTest = new DropCommand(GROUPNAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testDropShouldNotRomveAnythingBecauseWeWantRemoveAValueWithUnknownKey() {
        //GIVEN in setUp
        Map<String, StubDescriptor> expected = normalStubDescriptors;
        //WHEN
        underTest = new DropCommand("test", request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        //THEN
        Assert.assertEquals(result, expected);
    }
}
