package com.epam.wilma.core.processor.response.jms;

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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.sequence.SequenceManager;

/**
 * Listens for response messages put on the response queue.
 * @author Marton_Sereg
 */
@Component("responseMessageListener")
public class ResponseQueueListener implements MessageListener {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Queue loggerQueue;
    @Autowired
    private JmsResponseMessageCreatorFactory messageCreatorFactory;
    @Autowired
    private MessageExtractor messageExtractor;
    @Autowired
    private SequenceManager manager;

    private boolean fiDecompressionEnabled = true;
    private boolean messageLoggingEnabled = true;

    @Override
    public void onMessage(final Message message) {
        if (message instanceof ObjectMessage) {
            try {
                WilmaHttpResponse response = getWilmaHttpResponseFromMessage(message);
                boolean consistentFIDecompressionEnabled = fiDecompressionEnabled;
                if (consistentFIDecompressionEnabled) {
                    messageExtractor.extract(response);
                }
                response.setInputStream(null);
                if (messageLoggingEnabled  && response.isLoggingEnabled()) {
                    jmsTemplate.send(loggerQueue, messageCreatorFactory.create(response, consistentFIDecompressionEnabled));
                }
                manager.tryToSaveResponseIntoSequence(response);
            } catch (JMSException e) {
                throw new SystemException("JMS Exception occurred", e);
            }
        } else {
            throw new IllegalArgumentException("Message must be of type ObjectMessage");
        }
    }

    public void setFiDecompressionEnabled(final boolean fiDecompressionEnabled) {
        this.fiDecompressionEnabled = fiDecompressionEnabled;
    }

    public void setMessageLoggingEnabled(final boolean messageLoggingEnabled) {
        this.messageLoggingEnabled = messageLoggingEnabled;
    }

    private WilmaHttpResponse getWilmaHttpResponseFromMessage(final Message message) throws JMSException {
        ObjectMessage objectMessage = (ObjectMessage) message;
        return (WilmaHttpResponse) objectMessage.getObject();
    }

}
