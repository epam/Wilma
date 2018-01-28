package com.epam.wilma.messagemarker.configuration;
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

import com.epam.wilma.messagemarker.configuration.domain.MessageMarkerRequest;
import com.epam.wilma.properties.PropertyHolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

/**
 * Unit tests for the class {@link MessageMarkerConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class MessageMarkerConfigurationAccessTest {

    private static final String MESSAGE_MARKER_ON = "on";
    private static final String MESSAGE_MARKER_OFF = "off";

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private MessageMarkerConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadPropertiesShouldMessageMarkerOn() {
        //GIVEN
        given(propertyHolder.get("message.marking")).willReturn(MESSAGE_MARKER_ON);
        //WHEN
        underTest.loadProperties();
        //THEN
        MessageMarkerRequest actual = underTest.getProperties();
        assertEquals(actual.getNeedMessageMarker(), true);
    }

    @Test
    public void testLoadPropertiesShouldMessageMarkerOff() {
        //GIVEN
        given(propertyHolder.get("message.marking")).willReturn(MESSAGE_MARKER_OFF);
        //WHEN
        underTest.loadProperties();
        //THEN
        MessageMarkerRequest actual = underTest.getProperties();
        assertEquals(actual.getNeedMessageMarker(), false);
    }

}
