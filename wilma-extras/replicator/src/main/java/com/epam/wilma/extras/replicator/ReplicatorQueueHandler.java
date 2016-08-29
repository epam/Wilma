package com.epam.wilma.extras.replicator;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Saves the secondary request and response to disk (send to Wilma's built-in message logger queue.
 *
 * @author Tamas Kohegyi
 */
public class ReplicatorQueueHandler {

    private final Logger logger = LoggerFactory.getLogger(ReplicatorQueueHandler.class);

    /**
     * This method handles the replicated message (translate-send-receive-save) arrived from the queue.
     *
     * @param secondaryRequest is the message to be sent to the secondary server
     */
    public void handleQueuedMessage(WilmaHttpRequest secondaryRequest) {
        //update the request body as necessary
        translateRequestToSecondaryServer(secondaryRequest);

        //now send the secondary request to the secondary server, and receive response
        SecondaryRequestSender secondaryRequestSender = new SecondaryRequestSender();
        WilmaHttpResponse secondaryResponse = secondaryRequestSender.callSecondaryServer(secondaryRequest);

        //finally store the messages
        storeMessages(secondaryRequest, secondaryResponse);
    }

    private void translateRequestToSecondaryServer(WilmaHttpRequest secondaryRequest) {
        // -- JUST DO IT HERE, IF AND AS NECESSARY
    }

    private void storeMessages(WilmaHttpRequest secondaryRequest, WilmaHttpResponse secondaryResponse) {
        //put both request and response to message saving queue
        SecondaryMessageLogger secondaryMessageLogger = new SecondaryMessageLogger();
        secondaryMessageLogger.storeMessages(secondaryRequest, secondaryResponse);
    }

}
