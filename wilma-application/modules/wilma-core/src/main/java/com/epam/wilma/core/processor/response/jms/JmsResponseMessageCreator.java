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
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Class for creating JMS message from objects.
 *
 */
public class JmsResponseMessageCreator implements MessageCreator {

    private final WilmaHttpResponse wilmaHttpResponse;
    private final boolean bodyDecompressed;

    /**
     * Constructor to set required {@link WilmaHttpResponse} as field.
     * @param wilmaHttpResponse the response
     * @param bodyDecompressed true if body of HTTP response is decompressed
     */
    public JmsResponseMessageCreator(final WilmaHttpResponse wilmaHttpResponse, final boolean bodyDecompressed) {
        this.wilmaHttpResponse = wilmaHttpResponse;
        this.bodyDecompressed = bodyDecompressed;
    }

    @Override
    public Message createMessage(final Session session) throws JMSException {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(wilmaHttpResponse);
        message.setBooleanProperty("bodyDecompressed", bodyDecompressed);
        return message;
    }
}
