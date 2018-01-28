package com.epam.wilma.router.configuration;
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
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.router.configuration.domain.PropertyDTO;

/**
 * Unit tests for the class {@link RouteEngineConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class RouteEngineConfigurationAccessTest {

    private final Integer port = 1234;

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private RouteEngineConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.getInt("internal.wilma.port")).willReturn(port);
        given(propertyHolder.get("switch")).willReturn("wilma");
    }

    @Test
    public void testLoadPropertiesShouldSetProxyPort() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getProxyPort(), port);
    }

    @Test
    public void testLoadPropertiesShouldSetOperationMode() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getOperationMode(), OperationMode.WILMA);
    }

}
