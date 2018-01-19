package com.epam.wilma.safeguard.monitor.helper;
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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.stereotype.Component;

import com.epam.wilma.safeguard.domain.exception.JmxConnectionException;

/**
 * Provides JMX ObjectNames.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmxObjectNameProvider {

    /**
     * Retrieves a JMX ObjectName from a string name.
     * @param name name of JMX object
     * @return ObjectName object
     */
    public ObjectName getObjectName(final String name) {
        try {
            return new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            throw new JmxConnectionException("JMX connection to " + name + " cannot be established", e);
        }
    }
}
