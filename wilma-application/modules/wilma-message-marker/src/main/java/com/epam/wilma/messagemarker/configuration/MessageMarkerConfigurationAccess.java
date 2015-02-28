package com.epam.wilma.messagemarker.configuration;

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

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.messagemarker.configuration.domain.RequestLimits;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Provides configuration access for the module.
 * @author Tunde_Kovacs
 *
 */
@Component
public class MessageMarkerConfigurationAccess implements ConfigurationAccessBase {

    private RequestLimits requestLimit;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        Integer errorLimit = propertyHolder.getInt("request.error.limit");
        Integer warningLimit = propertyHolder.getInt("request.warning.limit");
        requestLimit = new RequestLimits(errorLimit, warningLimit);
    }

    /**
     * Returns a {@link PropertyDTO} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public RequestLimits getProperties() {
        return requestLimit;
    }

}
