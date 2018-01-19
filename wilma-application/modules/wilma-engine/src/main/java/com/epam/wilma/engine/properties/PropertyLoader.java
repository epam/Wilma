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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.stream.helper.FileInputStreamFactory;
import com.epam.wilma.engine.properties.helper.PropertiesFactory;
import com.epam.wilma.engine.properties.helper.PropertiesNotAvailableException;
import com.epam.wilma.engine.properties.validation.PropertyValidator;

/**
 * Loads properties from configuration file.
 * @author Tunde_Kovacs
 *
 */
@Component
public class PropertyLoader {

    private final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    @Value("#{programArgs}")
    private String configFile;
    @Value("#{configValidationFile}")
    private String validationFile;
    private Properties properties;
    private Properties validationProperties;
    private FileInputStream inputStream;
    @Autowired
    private FileInputStreamFactory inputStreamFactory;
    @Autowired
    private PropertiesFactory propertiesFactory;
    @Autowired
    private PropertyReader propertyReader;
    @Autowired
    private PropertyValidator propertyValidator;

    /**
     * Loads properties from the specified property file. Also validates
     * the property file.
     */
    public void loadProperties() {
        properties = propertiesFactory.createProperties();
        try {
            checkPropertyFileArgument(configFile);
            loadExternalProperties(configFile, properties);
            propertyReader.setProperties(properties);
            logger.debug("Properties loaded from external configuration.");
        } catch (IOException e) {
            throw new PropertiesNotAvailableException("Configuration file " + configFile + " cannot be loaded", e);
        }
    }

    /**
     * Loads validation properties. Also validates the property file.
     */
    public void loadValidationProperties() {
        try {
            checkPropertyFileArgument(validationFile);
            validationProperties = createValidationProperties();
            propertyValidator.setProperties(validationProperties);
        } catch (IOException e) {
            throw new PropertiesNotAvailableException("Configuration file " + validationFile + " cannot be loaded", e);
        }
    }

    Properties createValidationProperties() throws IOException {
        Resource resource = new ClassPathResource(validationFile);
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    private void checkPropertyFileArgument(final String args) {
        if ("".equals(args)) {
            throw new PropertiesNotAvailableException("Configuration file was not specified as input argument!");
        } else if (!args.endsWith(".properties")) {
            throw new PropertiesNotAvailableException("Configuration file must be a properties file!");
        }
    }

    private void loadExternalProperties(final String location, final Properties properties) throws IOException {
        inputStream = inputStreamFactory.createFileInputStream(location);
        properties.load(inputStream);
    }

}
