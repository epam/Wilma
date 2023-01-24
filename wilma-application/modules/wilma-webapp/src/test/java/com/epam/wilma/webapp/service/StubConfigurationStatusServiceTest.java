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

import com.epam.wilma.router.RoutingService;
import com.epam.wilma.webapp.service.command.ChangeStatusCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link StubConfigurationStatusService}.
 *
 * @author Tibor_Kovacs
 */
public class StubConfigurationStatusServiceTest {

    private static final String GROUP_NAME_FIRST = "First";

    @Mock
    private RoutingService routingService;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private StubConfigurationStatusService underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangeStatusShouldCallTheTwoMethod() throws ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.changeStatus(false, GROUP_NAME_FIRST, request);
        //THEN
        ArgumentCaptor<ChangeStatusCommand> argument = ArgumentCaptor.forClass(ChangeStatusCommand.class);
        verify(routingService, times(1)).performModification(argument.capture());
        assertFalse(argument.getValue().isNextStatus());
        assertEquals(GROUP_NAME_FIRST, argument.getValue().getGroupName());
        assertEquals(request, argument.getValue().getRequest());
    }
}
