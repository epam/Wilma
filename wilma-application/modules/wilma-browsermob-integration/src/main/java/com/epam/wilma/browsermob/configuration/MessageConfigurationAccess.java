package com.epam.wilma.browsermob.configuration;

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

import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.properties.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Configures the module with different properties.
 * @author Tamas Kohegyi
 *
 */
@Component
public class MessageConfigurationAccess implements ConfigurationAccessBase {

    private MessagePropertyDTO properties;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        String instancePrefix = propertyHolder.get("wilma.instance.prefix");
        if ((instancePrefix == null) || ("".equals(instancePrefix.trim()))) {
            instancePrefix = null;
        }
        properties = new MessagePropertyDTO(instancePrefix);
    }

    /**
     * Returns a {@link MessagePropertyDTO} holding message specific properties.
     * @return the propertiesDTO object
     */
    public MessagePropertyDTO getProperties() {
        return properties;
    }
}
