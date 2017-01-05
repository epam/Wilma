package com.epam.wilma.browsermob.configuration;

/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import com.epam.wilma.browsermob.configuration.domain.ProxyPropertyDTO;
import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Configures the module with different properties.
 * @author Tunde_Kovacs
 *
 */
@Component
public class BrowserMobConfigurationAccess implements ConfigurationAccessBase {

    private ProxyPropertyDTO properties;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        Integer proxyPort = propertyHolder.getInt("proxy.port");
        Integer requestTimeout = propertyHolder.getInt("proxy.request.timeout");
        String responseVolatileString = propertyHolder.get("proxy.response.volatile");
        Boolean responseVolatile = false;
        if (responseVolatileString != null && "true".equals(responseVolatileString)) {
            responseVolatile = true;
        }
        String shouldKeepSslConnectionAliveString = propertyHolder.get("proxy.connect.keepalive");
        Boolean shouldKeepSslConnectionAlive = false;
        if (shouldKeepSslConnectionAliveString != null && "true".equals(shouldKeepSslConnectionAliveString)) {
            shouldKeepSslConnectionAlive = true;
        }
        properties = new ProxyPropertyDTO(proxyPort, requestTimeout, responseVolatile, shouldKeepSslConnectionAlive);
    }

    /**
     * Returns a {@link ProxyPropertyDTO} holding all proxy specific properties.
     * @return the propertiesDTO object
     */
    public ProxyPropertyDTO getProperties() {
        return properties;
    }
}
