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

import com.epam.wilma.domain.stubconfig.StubConfigSchema;
import com.epam.wilma.engine.configuration.EngineConfigurationAccess;
import com.epam.wilma.engine.configuration.domain.PropertyDTO;
import com.epam.wilma.stubconfig.cache.cleaner.helper.StubConfigPathProvider;
import com.epam.wilma.stubconfig.dom.parser.xsd.StubConfigSchemaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads a stub descriptor from a file.
 *
 * @author Marton_Sereg
 */
@Component
public class FileBasedStubConfigReader {

    private String jsonDescriptorsSourceFolderPath;
    private String jsonDescriptorsPattern;
    private String jsonDescriptorsCachePath;
    @Autowired
    private StubDescriptorReader stubDescriptorReader;
    @Autowired
    private EngineConfigurationAccess configurationAccess;
    @Autowired
    private StubConfigPathProvider cachePathProvider;
    @Autowired
    private StubConfigSchemaParser stubConfigSchemaParser;
    @Autowired
    private StubConfigSchema stubConfigSchema;

    /**
     * Reads a new stub descriptor from a file and applies the new configuration in the core module.
     */
    public void readStubConfiguration() {
        getJsonDescriptorPath();
        stubConfigSchema.setSchema(stubConfigSchemaParser.parseSchema());
        List<String> stubConfigPaths = cachePathProvider.getConfigPathsFromCache(jsonDescriptorsCachePath);
        if (!stubConfigPaths.isEmpty()) {
            stubDescriptorReader.loadSpecificStubDescriptors(stubConfigPaths);
        } else {
            stubConfigPaths = cachePathProvider.getConfigPathsFromSpecificFolder(jsonDescriptorsSourceFolderPath, jsonDescriptorsPattern);
            stubDescriptorReader.loadSpecificStubDescriptors(stubConfigPaths);
        }
    }

    private void getJsonDescriptorPath() {
        PropertyDTO properties = configurationAccess.getProperties();
        jsonDescriptorsSourceFolderPath = properties.getStubConfigFolderPath();
        jsonDescriptorsPattern = properties.getStubConfigPattern();
        jsonDescriptorsCachePath = properties.getStubConfigCachePath();
    }
}
