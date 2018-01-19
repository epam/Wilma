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

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.safeguard.domain.exception.JmxConnectionException;

/**
 * Builds a JMX connection.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmxConnectionBuilder {

    @Autowired
    private JmxServiceUrlFactory urlFactory;

    /**
     * Builds a JMX connection to an MBeanServer.
     * @param url URL to connect.
     * @return connection
     */
    public MBeanServerConnection buildMBeanServerConnection(final String url) {
        try {
            JMXServiceURL jmxServiceURL = urlFactory.createJmxServiceUrl(url);
            JMXConnector c = getJmxConnector(jmxServiceURL);
            return c.getMBeanServerConnection();
        } catch (IOException e) {
            throw new JmxConnectionException("JMX connection to " + url + " cannot be established", e);
        }
    }

    JMXConnector getJmxConnector(final JMXServiceURL jmxServiceURL) throws IOException {
        return JMXConnectorFactory.connect(jmxServiceURL);
    }
}
