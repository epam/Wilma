package com.epam.wilma.core.processor.entity;
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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.indexing.jms.add.JmsRequestIndexingProcessor;
import com.epam.wilma.indexing.jms.add.JmsResponseIndexingProcessor;
import com.epam.wilma.logger.writer.WilmaHttpRequestWriter;
import com.epam.wilma.logger.writer.WilmaHttpResponseWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Class for processing messages from JMS logger queue.
 */
@Component("fileWriterListener")
public class FileWriterQueueListener implements MessageListener {

    @Autowired
    private WilmaHttpRequestWriter wilmaHttpRequestWriter;
    @Autowired
    private WilmaHttpResponseWriter wilmaHttpResponseWriter;
    @Autowired
    private JmsRequestIndexingProcessor requestIndexerProcessor;
    @Autowired
    private JmsResponseIndexingProcessor responseIndexerProcessor;

    @Override
    public void onMessage(final Message message) {
        if (message instanceof ObjectMessage) {
            try {
                WilmaHttpEntity wilmaHttpEntity = getWilmaHttpEntityFromMessage(message);
                writeFile(message, wilmaHttpEntity);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Message must be of type ObjectMessage");
        }
    }

    private void writeFile(final Message message, final WilmaHttpEntity wilmaHttpEntity) throws JMSException {
        boolean isBodyDecompressed = getBodyDecompressedFromMessage(message);
        if (isEntityARequest(wilmaHttpEntity)) {
            writeRequestAndSendToIndexing((WilmaHttpRequest) wilmaHttpEntity, isBodyDecompressed);
        } else {
            writeResponseAndSendToIndexing((WilmaHttpResponse) wilmaHttpEntity, isBodyDecompressed);
        }
    }

    private void writeRequestAndSendToIndexing(final WilmaHttpRequest request, final boolean isBodyDecompressed) {
        boolean success = wilmaHttpRequestWriter.write(request, isBodyDecompressed);
        if (success) {
            requestIndexerProcessor.process(request);
        }
    }

    private void writeResponseAndSendToIndexing(final WilmaHttpResponse response, final boolean isBodyDecompressed) {
        boolean success = wilmaHttpResponseWriter.write(response, isBodyDecompressed);
        if (success) {
            responseIndexerProcessor.process(response);
        }
    }

    private WilmaHttpEntity getWilmaHttpEntityFromMessage(final Message message) throws JMSException {
        ObjectMessage objectMessage = (ObjectMessage) message;
        return (WilmaHttpEntity) objectMessage.getObject();
    }

    private boolean getBodyDecompressedFromMessage(final Message message) throws JMSException {
        ObjectMessage objectMessage = (ObjectMessage) message;
        return objectMessage.getBooleanProperty("bodyDecompressed");
    }

    boolean isEntityARequest(final WilmaHttpEntity wilmaHttpEntity) {
        return wilmaHttpEntity.getClass() == WilmaHttpRequest.class;
    }
}
