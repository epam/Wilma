package com.epam.wilma.indexing.jms.configuration;
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

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.indexing.IndexingConfigurationAccess;
import com.epam.wilma.indexing.domain.PropertyDTO;

/**
 * Configures the broker URL of the {@link ActiveMQConnectionFactory} used
 * to communicate with the message searching application.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmsConnectionFactoryConfigurer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("jmsConnectionFactoryForIndex")
    private ActiveMQConnectionFactory connectionFactory;

    @Autowired
    private IndexingConfigurationAccess configurationAccess;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        PropertyDTO properties = configurationAccess.getProperties();
        String brokerHost = properties.getBrokerHost();
        Integer brokerPort = properties.getBrokerPort();
        connectionFactory.setBrokerURL("tcp://" + brokerHost + ":" + brokerPort);
    }
}
