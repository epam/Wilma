package com.epam.wilma.indexing;

/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.indexing.domain.PropertyDTO;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Configures the module with the necessary properties.
 * @author Tunde_Kovacs
 *
 */
@Component
public class IndexingConfigurationAccess implements ConfigurationAccessBase {

    private PropertyDTO properties;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        String brokerHost = propertyHolder.get("jms.queue.host");
        if ((brokerHost == null) || ("".equals(brokerHost.trim()))) {
            brokerHost = "localhost"; //defaults to "localhost" in order to preserve backward compatibility with Wilma <1.4
        }
        Integer brokerPort = propertyHolder.getInt("jms.queue.port");
        properties = new PropertyDTO(brokerHost, brokerPort);
    }

    /**
     * Returns a {@link PropertyDTO} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public PropertyDTO getProperties() {
        return properties;
    }

}
