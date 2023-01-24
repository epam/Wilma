package com.epam.wilma.engine.properties.validation;
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

import com.epam.wilma.properties.InvalidPropertyException;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.properties.validation.SafeguardLimitValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

/**
 * Unit tests for the class {@link PropertyValidator}.
 *
 * @author Tunde_Kovacs
 */
public class PropertyValidatorTest {

    private Properties properties;
    private PropertyHolder propertyHolder;
    @Mock
    private SafeguardLimitValidator safeguardLimitValidator;

    @InjectMocks
    private PropertyValidator underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        properties = new Properties();
        propertyHolder = new PropertyHolder();
        underTest.setProperties(properties);
        ReflectionTestUtils.setField(underTest, "propertyHolder", propertyHolder);
    }

    @Test
    public void testValidatePropertiesShouldPass() {
        //GIVEN
        String validationExpression = "#this >1";
        String propertyValue = "1234";
        properties.put("proxy.port", validationExpression);
        propertyHolder.addProperty("proxy.port", propertyValue);
        //WHEN
        underTest.validateProperties();
        //THEN it should not throw exception
    }

    @Test
    public void testValidatePropertiesShouldCallSafeguardValidator() {
        //GIVEN
        String validationExpression = "#this >1";
        String propertyValue = "1234";
        properties.put("proxy.port", validationExpression);
        propertyHolder.addProperty("proxy.port", propertyValue);
        //WHEN
        underTest.validateProperties();
        //THEN
        Mockito.verify(safeguardLimitValidator).validate();
    }

    @Test
    public void testValidatePropertiesWhenInvalidPropertyShouldThrowException() {
        Assertions.assertThrows(InvalidPropertyException.class, () -> {
            //GIVEN
            String validationExpression = "#this >1";
            String propertyValue = "0";
            properties.put("proxy.port", validationExpression);
            propertyHolder.addProperty("proxy.port", propertyValue);
            //WHEN
            underTest.validateProperties();
            //THEN it should throw exception
        });
    }

    @Test
    public void testValidatePropertiesWhenNumberFormatExceptionShouldThrowException() {
        Assertions.assertThrows(InvalidPropertyException.class, () -> {
            //GIVEN
            String validationExpression = "#this >1";
            String propertyValue = "bla";
            properties.put("proxy.port", validationExpression);
            propertyHolder.addProperty("proxy.port", propertyValue);
            //WHEN
            underTest.validateProperties();
            //THEN it should throw exception
        });
    }

    @Test
    public void testValidatePropertiesWhenOgnlExceptionShouldThrowException() {
        Assertions.assertThrows(InvalidPropertyException.class, () -> {
            //GIVEN
            String validationExpression = "#this >1 bla";
            String propertyValue = "0";
            properties.put("proxy.port", validationExpression);
            propertyHolder.addProperty("proxy.port", propertyValue);
            //WHEN
            underTest.validateProperties();
            //THEN it should throw exception
        });
    }
}
