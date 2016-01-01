package com.epam.wilma.indexing.jms.helper;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.indexing.domain.IndexMessage;

/**
 * Creates JMS {@link IndexMessage}es that can be sent to a queue.
 * @author Tunde_Kovacs
 *
 */
public class JmsIndexMessageCreator implements MessageCreator {

    private final IndexMessage indexMessage;

    /**
     * Constructs a new message creator from an {@link IndexMessage}.
     * @param message the index message to be sent on a jms queue
     */
    public JmsIndexMessageCreator(final IndexMessage message) {
        indexMessage = message;
    }

    @Override
    public Message createMessage(final Session session) throws JMSException {
        ObjectMessage message = session.createObjectMessage();
        message.setObject(indexMessage);
        return message;
    }

}
