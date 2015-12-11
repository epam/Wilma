package com.epam.wilma.message.search.engine.initalize;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.configuration.ConfigurationAccessBase;
import com.epam.wilma.message.search.properties.PropertyHolder;

/**
 * Configures the module with the necessary properties.
 * @author Tunde_Kovacs
 *
 */
@Component
public class EngineConfigurationAccess implements ConfigurationAccessBase {

    private PropertyDto properties;

    @Autowired
    private PropertyHolder propertyHolder;

    /**
     * Returns a {@link PropertyDto} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public PropertyDto getProperties() {
        return properties;
    }

    @Override
    public void loadProperties() {
        Integer port = propertyHolder.getInt("webapp.port");
        String messageDirectories = propertyHolder.get("message.folders");
        String indexFolder = propertyHolder.get("lucene.index.folder");
        Integer jmsBrokerPort = propertyHolder.getInt("jms.broker.port");
        String jmsBrokerHost = propertyHolder.get("jms.broker.host");
        if ((jmsBrokerHost == null) || ("".equals(jmsBrokerHost.trim()))) {
            jmsBrokerHost = "localhost";
        }
        properties = new PropertyDto(port, messageDirectories, indexFolder, jmsBrokerHost, jmsBrokerPort);
    }
}
