package com.epam.wilma.core;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link MapBasedResponseDescriptorAccess}.
 *
 * @author Tunde_Kovacs
 */
public class MapBasedResponseDescriptorAccessTest {

    @Mock
    private RoutingService routingService;

    @InjectMocks
    private MapBasedResponseDescriptorAccess underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetResponseDescriptorShouldCallRoutingService() {
        //GIVEN
        String key = "key";
        //WHEN
        underTest.getResponseDescriptor(key);
        //THEN
        verify(routingService).getResponseDescriptorDTOAndRemove(key);
    }

}
