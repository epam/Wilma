package com.epam.wilma.message.search.engine.properties;
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

import com.epam.wilma.message.search.properties.PropertyHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the class {@link PropertyReader}.
 *
 * @author Tunde_Kovacs
 */
public class PropertyReaderTest {

    private Properties properties;

    @InjectMocks
    private PropertyReader underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        properties = new Properties();
        PropertyHolder propertyHolder = new PropertyHolder();
        ReflectionTestUtils.setField(underTest, "propertyHolder", propertyHolder);
    }

    @Test
    public void testSetPropertiesShouldPutPropertiesToPropertyHolder() {
        //GIVEN in setUp
        properties.put("webapp.port", "1234");
        //WHEN
        underTest.setProperties(properties);
        //THEN
        PropertyHolder actual = (PropertyHolder) ReflectionTestUtils.getField(underTest, "propertyHolder");
        assert actual != null;
        assertEquals(actual.getInt("webapp.port"), Integer.valueOf(1234));
    }
}
