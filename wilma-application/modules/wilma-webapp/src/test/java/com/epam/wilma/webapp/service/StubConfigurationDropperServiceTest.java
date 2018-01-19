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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;

import com.epam.wilma.webapp.service.external.ServiceMap;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.router.RoutingService;
import com.epam.wilma.sequence.SequenceManager;
import com.epam.wilma.webapp.service.command.DropCommand;

/**
 * Provides unit tests for the class {@link StubConfigurationDropperService}.
 * @author Tibor_Kovacs
 *
 */
public class StubConfigurationDropperServiceTest {
    private static final String GROUPNAME_FIRST = "First";

    @Mock
    private SequenceManager sequenceManager;
    @Mock
    private RoutingService routingService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private ServiceMap serviceMap;

    @InjectMocks
    private StubConfigurationDropperService underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDropSelectedStubConfigurationShouldCallTheTwoMethod() throws ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.dropSelectedStubConfiguration(GROUPNAME_FIRST, request);
        //THEN
        ArgumentCaptor<DropCommand> argument = ArgumentCaptor.forClass(DropCommand.class);
        verify(sequenceManager).removeSequenceDescriptors(GROUPNAME_FIRST);
        verify(routingService, times(1)).performModification(argument.capture());
        Assert.assertEquals(argument.getValue().getGroupName(), GROUPNAME_FIRST);
        Assert.assertEquals(argument.getValue().getRequest(), request);
    }
}
