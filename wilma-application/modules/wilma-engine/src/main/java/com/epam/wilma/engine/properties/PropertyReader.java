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

import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.properties.PropertyHolder;

/**
 * Reads configuration parameters from a {@link Properties} object and saves them
 * in a key-value store.
 * @author Tunde_Kovacs
 *
 */
@Component
public class PropertyReader {

    @Autowired
    private PropertyHolder propertyHolder;

    /**
     * Reads configuration parameters from a {@link Properties} object and
     * saves them in a key-value store.
     * @param properties a {@link Properties} object that will be processed
     * and the each property will be injected a key-value store
     */
    public void setProperties(final Properties properties) {
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            String value = properties.getProperty(key.toString());
            propertyHolder.addProperty(key.toString(), value);
        }
    }

}
