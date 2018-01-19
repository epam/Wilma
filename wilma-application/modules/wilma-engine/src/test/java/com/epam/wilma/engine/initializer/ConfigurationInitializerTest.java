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

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.engine.configuration.parser.WilmaAdminHostsFileParser;
import com.epam.wilma.engine.properties.PropertyLoader;
import com.epam.wilma.engine.properties.validation.PropertyValidator;

/**
 * Unit tests for the class {@link ConfigurationInitializer}.
 * @author Tunde_Kovacs
 *
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

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "configurationAccesses", new ArrayList<ConfigurationAccessBase>());
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadProperties() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(propertyLoader).loadProperties();
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadValidationProperties() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(propertyLoader).loadValidationProperties();
        verify(validator).validateProperties();
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadReadStubConfig() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(fileBasedStubConfigReader).readStubConfiguration();
    }

    @Test
    public final void testAfterPropertiesSetShouldParseWilmaAdminHosts() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(wilmaAdminHostsFileParser).parseFile();
    }

    @Test
    public final void testAfterPropertiesSetShouldCallLoadPropertiesOfConfigurationAccessBase() throws Exception {
        //GIVEN
        List<ConfigurationAccessBase> confAccesses = new ArrayList<ConfigurationAccessBase>();
        confAccesses.add(configAccess);
        Whitebox.setInternalState(underTest, "configurationAccesses", confAccesses);
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(configAccess).loadProperties();
    }

}
