package com.epam.wilma.core.configuration;
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

import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.common.helper.SequenceHandlingState;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Provides unit tests for the class {@link CoreConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class CoreConfigurationAccessTest {

    private final String messageLogging = "on";

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private CoreConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.get("message.logging")).willReturn(messageLogging);
        given(propertyHolder.get("switch")).willReturn("wilma");
        given(propertyHolder.get("block.localhost.usage")).willReturn("on");
        given(propertyHolder.get("sequence.handling.state")).willReturn("on");
    }

    @Test
    public void testLoadPropertiesShouldSetMessageLogging() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getMessageLogging(), messageLogging);
    }

    @Test
    public void testLoadPropertiesShouldSetOperationMode() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getOperationMode(), OperationMode.WILMA);
    }

    @Test
    public void testLoadPropertiesShouldSetBlockLocalhostUsage() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getBlockLocalhostUsage(), BlockLocalhostUsage.ON);
    }

    @Test
    public void testLoadPropertiesShouldSetSequenceHandlingUsage() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getSequenceHandlingUsage(), SequenceHandlingState.ON);
    }
}
