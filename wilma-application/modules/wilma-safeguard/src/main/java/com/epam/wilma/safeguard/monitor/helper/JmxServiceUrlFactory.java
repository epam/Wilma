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

import java.net.MalformedURLException;

import javax.management.remote.JMXServiceURL;

import org.springframework.stereotype.Component;

/**
 * Factory for creating new instance of {@link JMXServiceURL}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmxServiceUrlFactory {

    /**
     * Creates a new instance of {@link JMXServiceURL}.
     * @param url the url string to be parsed
     * @return the new instance
     * @throws MalformedURLException if serviceURL
     * does not conform to the syntax for an Abstract Service URL
     * or if it is not a valid name for a JMX Remote API service.
     * A JMXServiceURL must begin with the string
     * "service:jmx:" (case-insensitive).
     * It must not contain any characters that are not printable ASCII characters.
     */
    public JMXServiceURL createJmxServiceUrl(final String url) throws MalformedURLException {
        return new JMXServiceURL(url);
    }
}
