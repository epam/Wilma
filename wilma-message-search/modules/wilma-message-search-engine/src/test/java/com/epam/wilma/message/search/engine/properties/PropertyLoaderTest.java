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

import com.epam.wilma.message.search.engine.properties.helper.PropertiesFactory;
import com.epam.wilma.message.search.engine.properties.helper.PropertiesNotAvailableException;
import com.epam.wilma.message.search.lucene.index.helper.FileInputStreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link PropertyLoader}.
 *
 * @author Tunde_Kovacs
 */
public class PropertyLoaderTest {

    private final String configFile = "src/test/resources/conf.properties";
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

    @InjectMocks
    private PropertyLoader underTest;

    @BeforeEach
    public void setUp() throws IOException {
        underTest = Mockito.spy(new PropertyLoader());
        MockitoAnnotations.openMocks(this);
        given(inputStreamFactory.createFileInputStream(configFile)).willReturn(inputStream);
        given(propertiesFactory.createProperties()).willReturn(properties);
        ReflectionTestUtils.setField(underTest, "configFile", configFile);
    }

    @Test
    public void testLoadPropertiesWhenProgramArgumentEmptyShouldThrowException() {
        Assertions.assertThrows(PropertiesNotAvailableException.class, () -> {
            //GIVEN
            ReflectionTestUtils.setField(underTest, "configFile", "");
            //WHEN
            underTest.loadProperties();
            //THEN exception should be thrown
        });
    }

    @Test
    public void testLoadPropertiesWhenProgramArgumentInvalidShouldThrowException() {
        Assertions.assertThrows(PropertiesNotAvailableException.class, () -> {
            //GIVEN
            ReflectionTestUtils.setField(underTest, "configFile", "wilma.conf.prop");
            //WHEN
            underTest.loadProperties();
            //THEN exception should be thrown
        });
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

    @Test
    public void testLoadPropertiesWhenFileNotFoundShouldThrowException() {
        Assertions.assertThrows(PropertiesNotAvailableException.class, () -> {
            //GIVEN
            given(inputStreamFactory.createFileInputStream(configFile)).willThrow(new FileNotFoundException());
            //WHEN
            underTest.loadProperties();
            //THEN exception should be thrown
        });
    }

    @Test
    public void testLoadPropertiesWhenIOExceptionShouldThrowException() {
        Assertions.assertThrows(PropertiesNotAvailableException.class, () -> {
            //GIVEN
            willThrow(new IOException()).given(properties).load(inputStream);
            //WHEN
            underTest.loadProperties();
            //THEN exception should be thrown
        });
    }

    @Test
    public void testLoadPropertiesShouldReturnProperties() {
        //GIVEN in setUp
        //WHEN
        Properties actual = underTest.loadProperties(configFile);
        //THEN
        assertEquals(actual.getProperty("webapp.port"), "8080");
    }

    @Test
    public void testLoadPropertiesShouldThrowExceptionWhenPropertiesNotFound() {
        Assertions.assertThrows(PropertiesNotAvailableException.class, () -> {
            //GIVEN in setUp
            //WHEN
            underTest.loadProperties("conf.properties");
            //THEN exception should be thrown
        });
    }

}
