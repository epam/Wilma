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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Creates JMS messages that can be sent to the queue from HTTP requests.
 * It writes the {@link WilmaHttpRequest} to the JMS message.
 * @author Tunde_Kovacs
 *
 */
public class JmsRequestMessageCreator implements MessageCreator {

    private final WilmaHttpRequest request;

    /**
     * Constructs a {@link JmsRequestMessageCreator} based on {@link WilmaHttpRequest}.
     * @param request is the {@link WilmaHttpRequest}
     */
    public JmsRequestMessageCreator(final WilmaHttpRequest request) {
        this.request = request;
    }

    @Override
    public Message createMessage(final Session session) throws JMSException {
        request.setInputStream(null);
        request.clearEvaluationResults();
        ObjectMessage message = session.createObjectMessage();
        message.setObject(request);
        message.setBooleanProperty("bodyDecompressed", true);
        return message;
    }

}
