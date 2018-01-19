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
 * Provides unit tests for the class {@link ChangeOrderCommand}.
 * @author Tibor_Kovacs
 *
 */
public class ChangeOrderCommandTest {
    private static final String GROUPNAME_FIRST = "First";
    private static final String GROUPNAME_SECOND = "Second";

    private Map<String, StubDescriptor> normalStubDescriptors;
    private Map<String, StubDescriptor> reverseStubDescriptors;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StubDescriptor stubDescriptor;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Mock
    private HttpServletRequest request;

    private ChangeOrderCommand underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        normalStubDescriptors = new LinkedHashMap<>();
        normalStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
        normalStubDescriptors.put(GROUPNAME_SECOND, stubDescriptor);
        reverseStubDescriptors = new LinkedHashMap<>();
        reverseStubDescriptors.put(GROUPNAME_SECOND, stubDescriptor);
        reverseStubDescriptors.put(GROUPNAME_FIRST, stubDescriptor);
    }

    @Test
    public void testDoChangeShouldMoveTheFirstStubConfigurationToDownByOne() {
        //GIVEN in setUp
        String[] expectedGroupnames = reverseStubDescriptors.keySet().toArray(new String[reverseStubDescriptors.size()]);
        //WHEN
        underTest = new ChangeOrderCommand(-1, GROUPNAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        String[] resultGroupnames = result.keySet().toArray(new String[result.size()]);
        //THEN
        Assert.assertEquals(resultGroupnames, expectedGroupnames);
    }

    @Test
    public void testDoChangeShouldMoveTheSecondStubConfigurationToUpByOne() {
        //GIVEN in setUp
        String[] expectedGroupnames = normalStubDescriptors.keySet().toArray(new String[normalStubDescriptors.size()]);
        //WHEN
        underTest = new ChangeOrderCommand(1, GROUPNAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(reverseStubDescriptors);
        String[] resultGroupnames = result.keySet().toArray(new String[result.size()]);
        //THEN
        Assert.assertEquals(resultGroupnames, expectedGroupnames);
    }

    @Test
    public void testDoChangeShouldNotMoveTheFirstStubConfigurationToUpByOne() {
        //GIVEN in setUp
        String[] expectedGroupnames = normalStubDescriptors.keySet().toArray(new String[normalStubDescriptors.size()]);
        //WHEN
        underTest = new ChangeOrderCommand(1, GROUPNAME_FIRST, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        String[] resultGroupnames = result.keySet().toArray(new String[result.size()]);
        //THEN
        Assert.assertEquals(resultGroupnames, expectedGroupnames);
    }

    @Test
    public void testDoChangeShouldNotMoveTheSecondStubConfigurationToDownByOne() {
        //GIVEN in setUp
        String[] expectedGroupnames = normalStubDescriptors.keySet().toArray(new String[normalStubDescriptors.size()]);
        //WHEN
        underTest = new ChangeOrderCommand(-1, GROUPNAME_SECOND, request, urlAccessLogMessageAssembler);
        Map<String, StubDescriptor> result = underTest.modify(normalStubDescriptors);
        String[] resultGroupnames = result.keySet().toArray(new String[result.size()]);
        //THEN
        Assert.assertEquals(resultGroupnames, expectedGroupnames);
    }
}
