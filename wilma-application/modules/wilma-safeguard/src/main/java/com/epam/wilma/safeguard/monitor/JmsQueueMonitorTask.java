package com.epam.wilma.safeguard.monitor;

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

import com.epam.wilma.core.safeguard.SafeguardController;
import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.safeguard.configuration.SafeguardConfigurationAccess;
import com.epam.wilma.safeguard.configuration.domain.PropertyDTO;
import com.epam.wilma.safeguard.configuration.domain.QueueSizeProvider;
import com.epam.wilma.safeguard.configuration.domain.SafeguardLimits;
import com.epam.wilma.safeguard.monitor.helper.JmxConnectionBuilder;
import com.epam.wilma.safeguard.monitor.helper.JmxObjectNameProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * This task is scheduled to run periodically. The period is given in the safeguard.guardperiod external property.
 * It queries the sizes of the JMS queues through JMX, and disables/enables FI decompression or the complete message logging based on the limits given in the properties files.
 * Disabling/Enabling is achieved through 2 boolean flags, that are checked before every decompression/message logging.
 * @author Marton_Sereg
 *
 * It also takes care about the ActiveMQ.DLQ (so called dead letter queue), and eliminates such messages as necessary,
 * @author Tamas_Kohegyi
 */
@Component
public class JmsQueueMonitorTask implements Runnable {

    static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";
    static final String RESPONSE_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=responseQueue";
    static final String LOGGER_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=loggerQueue";
    static final String DLQ_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=ActiveMQ.DLQ";

    private final Logger logger = LoggerFactory.getLogger(JmsQueueMonitorTask.class);

    @Autowired
    private JmxConnectionBuilder jmxConnectionBuilder;
    @Autowired
    private JmxObjectNameProvider jmxObjectNameProvider;
    @Autowired
    private SafeguardController safeguardController;
    @Autowired
    private SafeguardConfigurationAccess configurationAccess;
    @Autowired
    private QueueSizeProvider queueSizeProvider;

    private SafeguardLimits safeguardLimits;

    private MBeanServerConnection mBeanServerConnection;
    private ObjectName responseQueue;
    private ObjectName loggerQueue;
    private ObjectName dlqQueue;

    private boolean fIDecompressionEnabled = true;
    private boolean messageWritingEnabled = true;

    @Override
    public void run() {
        if (mBeanServerConnection == null || responseQueue == null || loggerQueue == null || dlqQueue == null) {
            mBeanServerConnection = jmxConnectionBuilder.buildMBeanServerConnection(JMX_SERVICE_URL);
            responseQueue = jmxObjectNameProvider.getObjectName(RESPONSE_QUEUE_OBJECT_NAME);
            loggerQueue = jmxObjectNameProvider.getObjectName(LOGGER_QUEUE_OBJECT_NAME);
            dlqQueue = jmxObjectNameProvider.getObjectName(DLQ_QUEUE_OBJECT_NAME);
        }
        Long totalQueueSize = retrieveQuerySize();

        getSafeguardLimits();
        setSafeguardFlags(totalQueueSize);

        resetDlqAsNecessary();
    }

    private void resetDlqAsNecessary() {
        boolean sizeIsValid = false;
        Long dlqSize = new Long(0);
        try {
            dlqSize = (Long) mBeanServerConnection.getAttribute(dlqQueue, "QueueSize");
            sizeIsValid = true;
            if (dlqSize > 0) {
                mBeanServerConnection.invoke(dlqQueue, "purge", null, null);
                logger.info("Message found in ActiveMQ.DLQ. Queue size is: " + dlqSize + ", queue purged successfully.");
            }
        } catch (Exception e) {
            if (sizeIsValid) {
                throw new SystemException("Message found in ActiveMQ.DLQ. Queue size is: " + dlqSize + ", QUEUE PURGE FAILED.", e);
            } else {
                throw new SystemException("Message found in ActiveMQ.DLQ. Queue size cannot be detected.", e);
            }
        }
    }

    public boolean isSafeguardFIEnabled() {
        return fIDecompressionEnabled;
    }

    public boolean isSafeguardMWEnabled() {
        return messageWritingEnabled;
    }

    private void getSafeguardLimits() {
        if (safeguardLimits == null) {
            PropertyDTO properties = configurationAccess.getProperties();
            safeguardLimits = properties.getSafeguardLimits();
        }
    }

    private void setSafeguardFlags(final Long totalQueueSize) {
        if (fIDecompressionEnabled && totalQueueSize > safeguardLimits.getFiOffLimit()) {
            fIDecompressionEnabled = false;
            safeguardController.setFIDecompressionEnabled(fIDecompressionEnabled);
            logger.info("Due to High load, FI decompression is turned OFF.");
        }
        if (messageWritingEnabled && totalQueueSize > safeguardLimits.getMwOffLimit()) {
            messageWritingEnabled = false;
            safeguardController.setMessageWritingEnabled(messageWritingEnabled);
            logger.info("Due to High load, Message Logging is turned OFF.");
        }
        if (!messageWritingEnabled && totalQueueSize < safeguardLimits.getMwOnLimit()) {
            messageWritingEnabled = true;
            safeguardController.setMessageWritingEnabled(messageWritingEnabled);
            logger.info("Due to Normal load, Message Logging is restored.");
        }
        if (!fIDecompressionEnabled && totalQueueSize < safeguardLimits.getFiOnLimit()) {
            fIDecompressionEnabled = true;
            safeguardController.setFIDecompressionEnabled(fIDecompressionEnabled);
            logger.info("Due to Normal load, FI decompression is restored.");
        }
    }

    private Long retrieveQuerySize() {
        try {
            Long responseQueueSize = (Long) mBeanServerConnection.getAttribute(responseQueue, "QueueSize");
            Long loggerQueueSize = (Long) mBeanServerConnection.getAttribute(loggerQueue, "QueueSize");
            queueSizeProvider.setResponseQueueSize(responseQueueSize);
            queueSizeProvider.setLoggerQueueSize(loggerQueueSize);
            return responseQueueSize + loggerQueueSize;
        } catch (Exception e) {
            throw new SystemException("Exception while monitoring queue sizes", e);
        }
    }

}
