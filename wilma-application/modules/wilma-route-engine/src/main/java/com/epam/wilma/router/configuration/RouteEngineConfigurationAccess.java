package com.epam.wilma.router.configuration;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.router.configuration.domain.PropertyDTO;

/**
 * Configures the module with different properties.
 * @author Tunde_Kovacs
 *
 */
@Component
public class RouteEngineConfigurationAccess implements ConfigurationAccessBase {

    private PropertyDTO properties;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        Integer proxyPort = propertyHolder.getInt("internal.wilma.port");
        String operationModeProperty = propertyHolder.get("switch");
        OperationMode operationMode = OperationMode.valueOf(operationModeProperty.toUpperCase());
        properties = new PropertyDTO(proxyPort, operationMode);
    }

    /**
     * Returns a {@link PropertyDTO} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public PropertyDTO getProperties() {
        return properties;
    }

}
