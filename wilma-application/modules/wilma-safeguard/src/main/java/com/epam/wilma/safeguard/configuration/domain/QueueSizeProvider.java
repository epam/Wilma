package com.epam.wilma.safeguard.configuration.domain;
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

import org.springframework.stereotype.Component;

/**
 * Holds the sum of the sizes of queues.
 * @author Tibor_Kovacs
 *
 */
@Component
public class QueueSizeProvider {

    private long loggerQueueSize;
    private long responseQueueSize;
    private long jmsQueueSize;

    public long getLoggerQueueSize() {
        return loggerQueueSize;
    }

    public void setLoggerQueueSize(final long loggerQueueSize) {
        this.loggerQueueSize = loggerQueueSize;
    }

    public long getResponseQueueSize() {
        return responseQueueSize;
    }

    public void setResponseQueueSize(final long responseQueueSize) {
        this.responseQueueSize = responseQueueSize;
    }

    public long getJmsQueueSize() {
        return jmsQueueSize;
    }

    public void setJmsQueueSize(final long jmsQueueSize) {
        this.jmsQueueSize = jmsQueueSize;
    }

}
