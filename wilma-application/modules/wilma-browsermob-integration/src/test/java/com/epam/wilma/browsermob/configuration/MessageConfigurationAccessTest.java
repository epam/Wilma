package com.epam.wilma.browsermob.configuration;

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

import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.properties.PropertyHolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

/**
 * Provides unit tests for the class {@link MessageConfigurationAccess}.
 * @author Tamas Kohegyi
 *
 */
public class MessageConfigurationAccessTest {

    private static final String PREFIX = "prefix";

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private MessageConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.get("wilma.instance.prefix")).willReturn(PREFIX);
    }

    @Test
    public void testLoadPropertiesShouldSetInstancePrefix() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THENs
        MessagePropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getInstancePrefix(), PREFIX);
    }

}
