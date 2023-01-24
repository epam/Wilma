package com.epam.wilma.engine.initializer;

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

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.engine.configuration.parser.WilmaAdminHostsFileParser;
import com.epam.wilma.engine.properties.PropertyLoader;
import com.epam.wilma.engine.properties.validation.PropertyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link ConfigurationInitializer}.
 *
 * @author Tunde_Kovacs
 */
public class ConfigurationInitializerTest {

    @Mock
    private PropertyLoader propertyLoader;
    @Mock
    private PropertyValidator validator;
    @Mock
    private FileBasedStubConfigReader fileBasedStubConfigReader;
    @Mock
    private WilmaAdminHostsFileParser wilmaAdminHostsFileParser;
    @Mock
    private ConfigurationAccessBase configAccess;

    @InjectMocks
    private ConfigurationInitializer underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "configurationAccesses", new ArrayList<ConfigurationAccessBase>());
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadProperties() {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(propertyLoader).loadProperties();
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadValidationProperties() {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(propertyLoader).loadValidationProperties();
        verify(validator).validateProperties();
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadReadStubConfig() {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(fileBasedStubConfigReader).readStubConfiguration();
    }

    @Test
    public final void testAfterPropertiesSetShouldParseWilmaAdminHosts() {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(wilmaAdminHostsFileParser).parseFile();
    }

    @Test
    public final void testAfterPropertiesSetShouldCallLoadPropertiesOfConfigurationAccessBase() {
        //GIVEN
        List<ConfigurationAccessBase> confAccesses = new ArrayList<>();
        confAccesses.add(configAccess);
        ReflectionTestUtils.setField(underTest, "configurationAccesses", confAccesses);
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(configAccess).loadProperties();
    }

}
