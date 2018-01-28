package com.epam.wilma.logger.request.jms;
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

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.logger.request.RequestLogger;

/**
 * Provides logging with ActiveMQ. Sends all incoming messages into a queue asynchronously.
 * @author Tunde_Kovacs
 *
 */
@Component
@Qualifier("jmsRequestLogger")
public class JmsRequestLogger implements RequestLogger {

    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue loggerQueue;

    @Autowired
    private JmsRequestMessageCreatorFactory jmsMessageCreatorFactory;

    private boolean messageLoggingEnabled = true;

    @Override
    public void logRequest(final WilmaHttpRequest request) {
        if (messageLoggingEnabled && request.isLoggingEnabled()) {
            jmsTemplate.send(loggerQueue, jmsMessageCreatorFactory.createJmsRequestMessageCreator(request));
        }
    }

    public void setMessageLoggingEnabled(final boolean messageLoggingEnabled) {
        this.messageLoggingEnabled = messageLoggingEnabled;
    }

}
