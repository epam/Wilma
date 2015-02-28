package com.epam.wilma.indexing.jms;
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

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.epam.wilma.indexing.domain.IndexMessage;
import com.epam.wilma.indexing.jms.helper.MessageCreatorFactory;

/**
 * Provides indexing with ActiveMQ. Sends all incoming messages into a queue asynchronously.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmsMessageIndexer {

    private final Logger logger = LoggerFactory.getLogger(JmsMessageIndexer.class);

    private boolean logConnectionError = true;

    @Autowired
    @Qualifier("jmsTemplateForIndexing")
    private JmsTemplate jmsTemplateForIndexing;

    @Autowired
    @Qualifier("jmsConnectionFactoryForIndex")
    private ActiveMQConnectionFactory connectionFactory;

    @Autowired
    private MessageCreatorFactory messageCreatorFactory;

    /**
     * Sends an {@link IndexMessage} to a jms queue.
     * @param message message to be sent
     */
    public void sendMessageToIndexer(final IndexMessage message) {
        try {
            jmsTemplateForIndexing.send(messageCreatorFactory.createJmsMessageCreator(message));
            turnOnConnectionErrorLog();
        } catch (Exception e) {
            turnOffConnectionErrorLog(e);
        }
    }

    private void turnOffConnectionErrorLog(final Exception e) {
        if (logConnectionError) {
            logger.info("Wilma could not connect to Indexer JMS Queue @" + connectionFactory.getBrokerURL(), e);
            //turn off logging connection errors to avoid duplicate errors in Wilma log
            logConnectionError = false;
        }
    }

    private void turnOnConnectionErrorLog() {
        if (!logConnectionError) {
            logger.info("Wilma successfully connected to Indexer JMS Queue @" + connectionFactory.getBrokerURL());
            logConnectionError = true;
        }
    }
}
