package com.epam.wilma.properties;
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

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Holds the application configuration properties in a key-value store.
 * @author Tunde_Kovacs
 *
 */
@Component
public class PropertyHolder {

    private final Map<String, String> properties = new HashMap<>();

    /**
     * Adds a new property to the property holding map.
     * @param key the key of the property
     * @param value the value of the property
     */
    public void addProperty(final String key, final String value) {
        properties.put(key, value);
    }

    /**
     * Returns a property with a String key.
     * @param key the key of the property
     * @return the value matching the key
     */
    public String get(final String key) {
        return properties.get(key);
    }

    /**
     * Returns a property with an Integer key.
     * @param key the key of the property
     * @return the value matching the key
     */
    public Integer getInt(final String key) {
        return Integer.valueOf(properties.get(key));
    }

    /**
     * Returns a property with a Long key.
     * @param key the key of the property
     * @return the value matching the key
     */
    public Long getLong(final String key) {
        return Long.valueOf(properties.get(key));
    }
}
