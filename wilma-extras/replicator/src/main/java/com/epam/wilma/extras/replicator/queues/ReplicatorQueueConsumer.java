package com.epam.wilma.extras.replicator.queues;

/*==========================================================================
Copyright 2016 EPAM Systems

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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.extras.replicator.secondaryClient.SecondaryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Saves the secondary request and response to disk (send to Wilma's built-in message logger queue.
 *
 * @author Tamas Kohegyi
 */
class ReplicatorQueueConsumer implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(ReplicatorQueueConsumer.class);

    /**
     * This method handles the replicated message (translate-send-receive-save) arrived from the queue.
     *
     * @param message is the message to be sent to the secondary server
     */
    @Override
    public void onMessage(Message message) {

        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                WilmaHttpRequest secondaryRequest = (WilmaHttpRequest) objectMessage.getObject();
                //update the request body/headers as necessary
                secondaryRequest = translateRequestToSecondaryServer(secondaryRequest);

                //now send the secondary request to the secondary server, and receive response
                SecondaryClient secondaryClient = new SecondaryClient();
                WilmaHttpResponse secondaryResponse = secondaryClient.callSecondaryServer(secondaryRequest);

                //finally store the messages
                storeMessages(secondaryRequest, secondaryResponse);
            } catch (JMSException e) {
                logger.error("Error occurred when reading message from Replicator queue.", e);
            }
        }
    }

    private WilmaHttpRequest translateRequestToSecondaryServer(WilmaHttpRequest secondaryRequest) {
        // -- JUST DO ANY TRANSLATION HERE, IF AND AS NECESSARY
        // -- WORTH TO USE IT FOR EXAMPLE THE SECONDARY TARGET HAS DIFFERENT INTERFACE THAN THE ORIGINAL/PRIMARY TARGET
        return secondaryRequest;
    }

    private void storeMessages(WilmaHttpRequest secondaryRequest, WilmaHttpResponse secondaryResponse) {
        //put both request and response to message saving queue
        SecondaryMessageLogger secondaryMessageLogger = new SecondaryMessageLogger();
        secondaryMessageLogger.storeMessages(secondaryRequest, secondaryResponse);
    }


}
