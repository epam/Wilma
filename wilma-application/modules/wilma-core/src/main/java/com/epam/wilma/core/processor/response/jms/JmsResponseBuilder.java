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

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.processor.ResponseBuilder;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Builds a JMS message from {@link WilmaHttpResponse} before sending to the JMS queue.
 *
 */
@Component("jmsResponseBuilder")
public class JmsResponseBuilder implements ResponseBuilder {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Queue responseQueue;
    @Autowired
    private JmsResponseMessageCreatorFactory messageCreatorFactory;

    @Override
    public void buildResponse(final WilmaHttpEntity entity) {
        WilmaHttpResponse response = (WilmaHttpResponse) entity;
        jmsTemplate.send(responseQueue, messageCreatorFactory.create(response, true));
    }

}
