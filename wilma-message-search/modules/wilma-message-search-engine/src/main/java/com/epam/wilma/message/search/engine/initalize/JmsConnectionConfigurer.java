package com.epam.wilma.message.search.engine.initalize;
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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * Configures the {@link JmsConnectionConfigurer} and {@link TransportConnector} with a broker url.
 * The jms queue is used to recieve messages to be indexed by the application.
 *
 * @author Tunde_Kovacs, Tamas Kohegyi
 */
@Component
public class JmsConnectionConfigurer {

    private final Logger logger = LoggerFactory.getLogger(JmsConnectionConfigurer.class);

    @Autowired
    private PooledConnectionFactory pooledConnectionFactory;
    @Autowired
    @Qualifier("jmsConnectionFactory")
    private ActiveMQConnectionFactory connectionFactory;
    @Autowired
    @Qualifier("jmsTransportConnector")
    private TransportConnector transportConnector;
    @Autowired
    private EngineConfigurationAccess configurationAccess;

    /**
     * Configures the jms broker url.
     */
    public void setBrokerUrl() {
        String jmsBrokerHost = getJmsBrokerHost();
        Integer jmsBrokerPort = getJmsBrokerPort();
        connectionFactory.setBrokerURL("tcp://" + jmsBrokerHost + ":" + jmsBrokerPort);
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        configureTransportConnector(jmsBrokerHost, jmsBrokerPort);
    }

    private void configureTransportConnector(final String jmsBrokerHost, final Integer jmsBrokerPort) {
        String uriString = "tcp://" + jmsBrokerHost + ":" + jmsBrokerPort;
        try {
            transportConnector.setUri(new URI(uriString));
        } catch (URISyntaxException e) {
            logger.error("Wrong URI: '" + uriString + "', message: " + e.getMessage()); //NOSONAR - we don't need more info
        }
    }

    private String getJmsBrokerHost() {
        PropertyDto properties = configurationAccess.getProperties();
        String jmsBrokerHost = properties.getJmsBrokerHost();
        return jmsBrokerHost;
    }

    private Integer getJmsBrokerPort() {
        PropertyDto properties = configurationAccess.getProperties();
        Integer jmsBrokerPort = properties.getJmsBrokerPort();
        return jmsBrokerPort;
    }
}
