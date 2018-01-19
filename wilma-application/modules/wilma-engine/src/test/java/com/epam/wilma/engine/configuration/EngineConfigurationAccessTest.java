package com.epam.wilma.engine.configuration;

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

import com.epam.wilma.engine.configuration.domain.PropertyDTO;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Unit tests for the class {@link EngineConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class EngineConfigurationAccessTest {

    private static final String STUB_CONFIG_SOURCE_FOLDER_PATH = "src/main/resources/stubconfigs";
    private static final String STUB_CONFIG_PATTERN = "xml";
    private final Integer port = 1234;

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private EngineConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.getInt("proxy.port")).willReturn(port);
        given(propertyHolder.get("stub.descriptors.path")).willReturn(STUB_CONFIG_SOURCE_FOLDER_PATH);
        given(propertyHolder.get("stub.descriptors.pattern")).willReturn(STUB_CONFIG_PATTERN);
    }

    @Test
    public void testLoadPropertiesShouldSetProxyPort() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getProxyPort(), port);
    }

    @Test
    public void testLoadPropertiesShouldSetStubDescriptorSourceFolderPath() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getStubConfigFolderPath(), STUB_CONFIG_SOURCE_FOLDER_PATH);
    }

    @Test
    public void testLoadPropertiesShouldSetStubDescriptorPattern() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getStubConfigPattern(), STUB_CONFIG_PATTERN);
    }
}
