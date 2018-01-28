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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.stream.helper.FileInputStreamFactory;
import com.epam.wilma.engine.properties.helper.PropertiesFactory;
import com.epam.wilma.engine.properties.helper.PropertiesNotAvailableException;
import com.epam.wilma.engine.properties.validation.PropertyValidator;

/**
 * Provides unit tests for the class {@link PropertyLoader}.
 * @author Tunde_Kovacs
 *
 */
public class PropertyLoaderTest {

    private final String configFile = "wilma.conf.properties";
    private final String validationFile = "wilma.conf.validation.properties";
    @Mock
    private Properties properties;
    @Mock
    private Properties validationProperties;
    @Mock
    private FileInputStream inputStream;
    @Mock
    private FileInputStreamFactory inputStreamFactory;
    @Mock
    private PropertiesFactory propertiesFactory;
    @Mock
    private PropertyReader propertyReader;
    @Mock
    private PropertyValidator propertyValidator;

    @InjectMocks
    private PropertyLoader underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        underTest = Mockito.spy(new PropertyLoader());
        MockitoAnnotations.initMocks(this);
        given(inputStreamFactory.createFileInputStream(configFile)).willReturn(inputStream);
        given(propertiesFactory.createProperties()).willReturn(properties);
        Whitebox.setInternalState(underTest, "configFile", configFile);
        Whitebox.setInternalState(underTest, "validationFile", validationFile);
    }

    @Test(expectedExceptions = PropertiesNotAvailableException.class)
    public void testLoadPropertiesWhenProgramArgumentEmptyShouldThrowException() {
        //GIVEN
        Whitebox.setInternalState(underTest, "configFile", "");
        //WHEN
        underTest.loadProperties();
        //THEN excpetion should be thrown
    }

    @Test(expectedExceptions = PropertiesNotAvailableException.class)
    public void testLoadPropertiesWhenProgramArgumentInvalidShouldThrowException() {
        //GIVEN
        Whitebox.setInternalState(underTest, "configFile", "wilma.conf.prop");
        //WHEN
        underTest.loadProperties();
        //THEN excpetion should be thrown
    }

    @Test
    public void testLoadPropertiesWhenArgumentsAreValidShouldLoadProperties() throws IOException {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        verify(properties).load(inputStream);
    }

    @Test
    public void testLoadPropertiesWhenArgumentsAreValidShouldCallPropertyReader() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        verify(propertyReader).setProperties(properties);
    }

    @Test(expectedExceptions = PropertiesNotAvailableException.class)
    public void testLoadPropertiesWhenFileNotFoundShouldThrowException() throws Exception {
        //GIVEN
        given(inputStreamFactory.createFileInputStream(configFile)).willThrow(new FileNotFoundException());
        //WHEN
        underTest.loadProperties();
        //THEN excpetion should be thrown

    }

    @Test(expectedExceptions = PropertiesNotAvailableException.class)
    public void testLoadPropertiesWhenIOExcpetionShouldThrowException() throws Exception {
        //GIVEN
        willThrow(new IOException()).given(properties).load(inputStream);
        //WHEN
        underTest.loadProperties();
        //THEN excpetion should be thrown
    }

    @Test
    public void testLoadValidationPropertiesShouldLoadProperties() throws IOException {
        //GIVEN
        doReturn(validationProperties).when(underTest).createValidationProperties();
        //WHEN
        underTest.loadValidationProperties();
        //THEN
        verify(propertyValidator).setProperties(validationProperties);
    }

    @Test(expectedExceptions = PropertiesNotAvailableException.class)
    public void testLoadValidationPropertiesWhenIOExcpetionShouldThrowException() throws Exception {
        //GIVEN
        Mockito.doThrow(new IOException()).when(underTest).createValidationProperties();
        //WHEN
        underTest.loadValidationProperties();
        //THEN excpetion should be thrown
    }
}
