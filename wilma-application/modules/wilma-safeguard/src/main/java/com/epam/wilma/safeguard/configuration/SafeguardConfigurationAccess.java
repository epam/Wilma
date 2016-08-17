package com.epam.wilma.safeguard.configuration;

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
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.safeguard.configuration.domain.PropertyDTO;
import com.epam.wilma.safeguard.configuration.domain.SafeguardLimits;

/**
 * Provides configuration access for the module.
 * @author Tunde_Kovacs
 *
 */
@Component
public class SafeguardConfigurationAccess implements ConfigurationAccessBase {

    private static final String DEFAULT_JMX_PORT = "1099";
    private PropertyDTO propertyDTO;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        Long fiOffLimit = propertyHolder.getLong("safeguard.responseFIdecoder.OFFlimit");
        Long fiOnLimit = propertyHolder.getLong("safeguard.responseFIdecoder.ONlimit");
        Long mwOffLimit = propertyHolder.getLong("safeguard.responseMessageWriter.OFFlimit");
        Long mwOnLimit = propertyHolder.getLong("safeguard.responseMessageWriter.ONlimit");
        String jmxPort = propertyHolder.get("com.sun.management.jmxremote.port");
        if ((jmxPort == null) || (tryParseInt(jmxPort) == 0)) {
            jmxPort = DEFAULT_JMX_PORT;
        }
        SafeguardLimits safeguardLimits = new SafeguardLimits(fiOffLimit, fiOnLimit, mwOffLimit, mwOnLimit, jmxPort);
        String cronExpression = propertyHolder.get("safeguard.guardperiod");
        propertyDTO = new PropertyDTO(safeguardLimits, cronExpression);
    }

    /**
     * Returns a {@link SafeguardLimits} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public PropertyDTO getProperties() {
        return propertyDTO;
    }

    //CHECKSTYLE_OFF - we need 2 returns here
    int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    //CHECKSTYLE_ON

}
