package com.epam.wilma.engine.properties;
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

import static org.testng.Assert.assertEquals;

import java.util.Properties;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.properties.PropertyHolder;

/**
 * Provides unit tests for the class {@link PropertyReader}.
 * @author Tunde_Kovacs
 *
 */
public class PropertyReaderTest {

    private Properties properties;
    private PropertyHolder propertyHolder;

    @InjectMocks
    private PropertyReader underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        properties = new Properties();
        propertyHolder = new PropertyHolder();
        Whitebox.setInternalState(underTest, "propertyHolder", propertyHolder);
    }

    @Test
    public void testSetPropertiesShouldPutPropertiesToPropertyHolder() {
        //GIVEN in setUp
        properties.put("switch", "stub");
        properties.put("proxy.port", "1234");
        //WHEN
        underTest.setProperties(properties);
        //THEN
        PropertyHolder actual = (PropertyHolder) Whitebox.getInternalState(underTest, "propertyHolder");
        assertEquals(actual.get("switch"), "stub");
        assertEquals(actual.getInt("proxy.port"), Integer.valueOf(1234));
    }

}
