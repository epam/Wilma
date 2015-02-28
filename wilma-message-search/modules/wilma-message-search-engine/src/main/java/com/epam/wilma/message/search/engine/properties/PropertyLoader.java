package com.epam.wilma.message.search.engine.properties;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.engine.properties.helper.PropertiesFactory;
import com.epam.wilma.message.search.engine.properties.helper.PropertiesNotAvailableException;
import com.epam.wilma.message.search.lucene.index.helper.FileInputStreamFactory;

/**
 * Loads properties from a configuration file.
 * @author Tunde_Kovacs
 *
 */
@Component
public class PropertyLoader {

    private final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    @Value("#{programArgs}")
    private String configFile;
    private Properties properties;
    private FileInputStream inputStream;
    @Autowired
    private FileInputStreamFactory inputStreamFactory;
    @Autowired
    private PropertiesFactory propertiesFactory;
    @Autowired
    private PropertyReader propertyReader;

    /**
     * Loads properties from the specified property file. Also validates
     * the property file.
     * @param configFile the configuration file to be read
     * @return the loaded {@link Properties}
     */
    public Properties loadProperties(final String configFile) {
        Properties properties = new Properties();
        try {
            checkPropertyFileArgument(configFile);
            InputStream inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
            logger.debug("Properties loaded from external configuration.");
        } catch (IOException e) {
            throw new PropertiesNotAvailableException("Configuration file " + configFile + " cannot be loaded", e);
        }
        return properties;
    }

    /**
     * Loads properties from the property file specified as command line argument.
     * Also validates the property file.
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

    private void checkPropertyFileArgument(final String args) {
        if (args == null || "".equals(args)) {
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
