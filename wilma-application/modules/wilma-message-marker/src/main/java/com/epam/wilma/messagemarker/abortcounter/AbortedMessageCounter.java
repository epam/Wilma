package com.epam.wilma.messagemarker.abortcounter;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for counting aborted messages in a synchronized way.
 * @author Tamas_Bihari
 *
 */
@Component
public class AbortedMessageCounter {
    private final Logger logger = LoggerFactory.getLogger(AbortedMessageCounter.class);
    private int abortedRequestCounter;
    private String timeStamp;

    /**
     * Log the number of aborted messages and set counter to zero.
     */
    public synchronized void logNumberOfAbortedMessages() {
        if (abortedRequestCounter > 0) {
            logger.info("ALERT: Messages overload, " + abortedRequestCounter + " messages were dropped @" + timeStamp + "!");
            abortedRequestCounter = 0;
        }
    }

    /**
     * Increment number of aborted messages.
     * @param timeStamp of the messages
     */
    public synchronized void incrementAbortedMessages(final String timeStamp) {
        this.timeStamp = timeStamp;
        abortedRequestCounter++;
    }

}
