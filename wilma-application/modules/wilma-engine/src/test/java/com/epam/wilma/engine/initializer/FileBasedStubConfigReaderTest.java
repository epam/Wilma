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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Schema;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubConfigSchema;
import com.epam.wilma.engine.configuration.EngineConfigurationAccess;
import com.epam.wilma.engine.configuration.domain.PropertyDTO;
import com.epam.wilma.stubconfig.cache.cleaner.helper.StubConfigPathProvider;
import com.epam.wilma.stubconfig.dom.parser.xsd.StubConfigSchemaParser;

/**
 * Provides unit tests for the class {@link FileBasedStubConfigReader}.
 * @author Tunde_Kovacs
 *
 */
public class FileBasedStubConfigReaderTest {

    private static final String DESCRIPTOR_PATH = "config";
    private static final String DESCRIPTOR_PATTERN = "*_stubConfig.json";
    private static final String DESCRIPTOR_CACHE_PATH = "config/cache";
    @Mock
    private StubDescriptorReader stubDescriptorReader;
    @Mock
    private EngineConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;
    @Mock
    private StubConfigSchemaParser stubConfigSchemaParser;
    @Mock
    private StubConfigSchema stubConfigSchema;
    @Mock
    private StubConfigPathProvider cachePathProvider;

    private List<String> filePaths;
    @Mock
    private Schema schema;

    @InjectMocks
    private FileBasedStubConfigReader underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filePaths = new ArrayList<>();
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getStubConfigFolderPath()).willReturn(DESCRIPTOR_PATH);
        given(properties.getStubConfigPattern()).willReturn(DESCRIPTOR_PATTERN);
        given(properties.getStubConfigCachePath()).willReturn(DESCRIPTOR_CACHE_PATH);
        given(cachePathProvider.getConfigPathsFromCache(DESCRIPTOR_CACHE_PATH)).willReturn(filePaths);
    }

    @Test
    public void testReadStubConfigurationShouldLookForStubConfigurationsWhenCacheIsEmpty() {
        //GIVEN
        List<String> paths = new ArrayList<>();
        paths.add("test");
        given(cachePathProvider.getConfigPathsFromSpecificFolder(DESCRIPTOR_PATH, DESCRIPTOR_PATTERN)).willReturn(paths);
        //WHEN
        underTest.readStubConfiguration();
        //THEN
        verify(cachePathProvider).getConfigPathsFromSpecificFolder(DESCRIPTOR_PATH, DESCRIPTOR_PATTERN);
        verify(stubDescriptorReader).loadSpecificStubDescriptors(paths);
    }

    @Test
    public void testReadStubConfigurationShouldUseTheCachedConfigurations() {
        //GIVEN
        List<String> paths = new ArrayList<>();
        paths.add("test");
        given(cachePathProvider.getConfigPathsFromCache(DESCRIPTOR_CACHE_PATH)).willReturn(paths);
        //WHEN
        underTest.readStubConfiguration();
        //THEN
        verify(stubDescriptorReader).loadSpecificStubDescriptors(paths);
    }

    @Test
    public final void testReadStubConfigurationShouldParseXSDSchema() {
        //GIVEN
        given(stubConfigSchemaParser.parseSchema()).willReturn(schema);
        //WHEN
        underTest.readStubConfiguration();
        //THEN
        verify(stubConfigSchema).setSchema(schema);
    }

}
