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

import java.util.Properties;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.properties.InvalidPropertyException;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.properties.validation.SafeguardLimitValidator;

/**
 * Unit tests for the class {@link PropertyValidator}.
 * @author Tunde_Kovacs
 *
 */
public class PropertyValidatorTest {

    private Properties properties;
    private PropertyHolder propertyHolder;
    @Mock
    private SafeguardLimitValidator safeguardLimitValidator;

    @InjectMocks
    private PropertyValidator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        properties = new Properties();
        propertyHolder = new PropertyHolder();
        underTest.setProperties(properties);
        Whitebox.setInternalState(underTest, "propertyHolder", propertyHolder);
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

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidatePropertiesWhenInvalidPropertyShouldThrowException() {
        //GIVEN
        String validationExpression = "#this >1";
        String propertyValue = "0";
        properties.put("proxy.port", validationExpression);
        propertyHolder.addProperty("proxy.port", propertyValue);
        //WHEN
        underTest.validateProperties();
        //THEN it should throw exception
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidatePropertiesWhenNumberFormatExceptionShouldThrowException() {
        //GIVEN
        String validationExpression = "#this >1";
        String propertyValue = "bla";
        properties.put("proxy.port", validationExpression);
        propertyHolder.addProperty("proxy.port", propertyValue);
        //WHEN
        underTest.validateProperties();
        //THEN it should throw exception
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidatePropertiesWhenOgnlExceptionShouldThrowException() {
        //GIVEN
        String validationExpression = "#this >1 bla";
        String propertyValue = "0";
        properties.put("proxy.port", validationExpression);
        propertyHolder.addProperty("proxy.port", propertyValue);
        //WHEN
        underTest.validateProperties();
        //THEN it should throw exception
    }
}
