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

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.engine.configuration.parser.WilmaAdminHostsFileParser;
import com.epam.wilma.engine.properties.PropertyLoader;
import com.epam.wilma.engine.properties.validation.PropertyValidator;

/**
 * After Spring properties are set in Spring managed classes, external Wilma properties are loaded and classes that need
 * external properties are initialized. The stub descriptor file is also loaded here.
 * @author Marton_Sereg
 *
 */
@Component
public class ConfigurationInitializer {

    @Autowired
    private PropertyLoader propertyLoader;
    @Autowired
    private PropertyValidator validator;
    @Autowired
    private FileBasedStubConfigReader fileBasedStubConfigReader;
    @Autowired
    private WilmaAdminHostsFileParser wilmaAdminHostsFileParser;
    @Autowired
    private List<ConfigurationAccessBase> configurationAccesses;

    /**
     * This method reads in property files, does the validation of Wilma property file,
     * reads in stub configurations and admin host file.
     * @throws Exception {@link PropertyLoader}, {@link PropertyValidator} can throw different Exceptions
     */
    @PostConstruct
    void afterPropertiesSet() throws Exception {
        propertyLoader.loadProperties();
        propertyLoader.loadValidationProperties();
        validator.validateProperties();
        loadProperties();
        fileBasedStubConfigReader.readStubConfiguration();
        wilmaAdminHostsFileParser.parseFile();
    }

    private void loadProperties() {
        for (ConfigurationAccessBase configAccess : configurationAccesses) {
            configAccess.loadProperties();
        }
    }

}
