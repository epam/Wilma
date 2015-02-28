package com.epam.wilma.message.search.jms;
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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.indexing.domain.IndexMessage;
import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.LuceneEngine;

/**
 * Class for processing messages coming from a JMS queue.
 *
 */
@Component("fileIndexerListener")
public class FileIndexerQueueListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(FileIndexerQueueListener.class);

    @Autowired
    private LuceneEngine luceneEngine;

    @Override
    public void onMessage(final Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                IndexMessage indexMessage = (IndexMessage) objectMessage.getObject();
                String fileName = indexMessage.getFileName();
                String type = indexMessage.getType();
                if (type.equals(IndexingType.ADD.getName())) {
                    luceneEngine.addFileToIndex(fileName);
                } else if (type.equals(IndexingType.DELETE.getName())) {
                    luceneEngine.deleteFileFromIndex(fileName);
                }
            } catch (JMSException e) {
                throw new SystemException("Message information transfer failed. Reason:" + e.getMessage());
            } catch (OutOfMemoryError e) {
                logger.error("HeapMaxMemory:" + Runtime.getRuntime().maxMemory() + ", HeapTotalMemory:" + Runtime.getRuntime().totalMemory()
                        + ", HeapFreeMemory:" + Runtime.getRuntime().freeMemory() + ", Exception: ", e);
                shutdown();
            }

        }
    }

    private void shutdown() {
        //CHECKSTYLE.OFF
        System.exit(1);
        //CHECKSTYLE.ON
    }
}
