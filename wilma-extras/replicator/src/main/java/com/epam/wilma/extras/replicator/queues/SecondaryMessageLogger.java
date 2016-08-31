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

import com.epam.wilma.core.processor.entity.JmsRequestLoggerProcessor;
import com.epam.wilma.core.processor.entity.JmsResponseProcessor;
import com.epam.wilma.core.processor.entity.PrettyPrintProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Saves the secondary request and response to disk (send to Wilma's built-in message logger queue.
 *
 * @author Tamas Kohegyi
 */
public class SecondaryMessageLogger {

    private static PrettyPrintProcessor prettyPrintProcessor;
    private static JmsRequestLoggerProcessor jmsRequestLoggerProcessor;
    private static JmsResponseProcessor jmsResponseProcessor;
    private static boolean wasSet;

    private final Logger logger = LoggerFactory.getLogger(SecondaryMessageLogger.class);

    /**
     * Set the Spring beans for a non-Spring related class.
     *
     * @param prettyPrintProcessor      as its name suggests.
     * @param jmsRequestLoggerProcessor as its name suggests.
     * @param jmsResponseProcessor      as its name suggests.
     */
    public static void setBeans(PrettyPrintProcessor prettyPrintProcessor, JmsRequestLoggerProcessor jmsRequestLoggerProcessor, JmsResponseProcessor jmsResponseProcessor) {
        SecondaryMessageLogger.prettyPrintProcessor = prettyPrintProcessor;
        SecondaryMessageLogger.jmsRequestLoggerProcessor = jmsRequestLoggerProcessor;
        SecondaryMessageLogger.jmsResponseProcessor = jmsResponseProcessor;
        SecondaryMessageLogger.wasSet = true;
    }

    /**
     * Send the secondary request and response to Wilma's built-in message logger queue.
     *
     * @param secondaryRequest  is its name suggests
     * @param secondaryResponse is its name suggests
     */
    public void storeMessages(WilmaHttpRequest secondaryRequest, WilmaHttpResponse secondaryResponse) {
        if (SecondaryMessageLogger.wasSet) {
            try {
                //first handle request
                prettyPrintProcessor.process(secondaryRequest);
                jmsRequestLoggerProcessor.process(secondaryRequest);
                //then handle response (that might be missed if cannot connect to test server
                if (secondaryResponse != null) {
                    prettyPrintProcessor.process(secondaryResponse);
                    jmsResponseProcessor.process(secondaryResponse);
                }
            } catch (ApplicationException e) {
                logger.error("Problem saving replicated request/response.", e);
            }
        } else {
            logger.error("Problem saving replicated request/response - beans are unavailable.");
        }
    }

}
