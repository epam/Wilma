package com.epam.wilma.messagemarker.configuration;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.messagemarker.configuration.domain.RequestLimits;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Unit tests for the class {@link MessageMarkerConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class MessageMarkerConfigurationAccessTest {

    private static final int LIMIT = 900;

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private MessageMarkerConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.getInt("request.error.limit")).willReturn(LIMIT);
        given(propertyHolder.getInt("request.warning.limit")).willReturn(LIMIT);
    }

    @Test
    public void testLoadPropertiesShouldSetErrorLimit() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        RequestLimits actual = underTest.getProperties();
        assertEquals(actual.getErrorLimit(), LIMIT);
    }

    @Test
    public void testLoadPropertiesShouldSetWarningLimit() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        RequestLimits actual = underTest.getProperties();
        assertEquals(actual.getWarningLimit(), LIMIT);
    }

}
