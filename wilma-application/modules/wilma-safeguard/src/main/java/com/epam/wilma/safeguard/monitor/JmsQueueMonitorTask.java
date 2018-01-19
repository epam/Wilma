package com.epam.wilma.safeguard.monitor;

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

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * This task is scheduled to run periodically. The period is given in the safeguard.guardperiod external property.
 * It queries the sizes of the JMS queues through JMX, and disables/enables FI decompression or the complete message logging based on the limits given in the properties files.
 * Disabling/Enabling is achieved through 2 boolean flags, that are checked before every decompression/message logging.
 * It also takes care about the ActiveMQ.DLQ (so called dead letter queue), and eliminates such messages as necessary.
 *
 * @author Marton_Sereg
 * @author Tamas_Kohegyi
 */
@Component
public class JmsQueueMonitorTask implements Runnable {

    static final String JMX_SERVICE_PRE_URL = "service:jmx:rmi:///jndi/rmi://localhost:";
    static final String JMX_SERVICE_POST_URL = "/jmxrmi";
    static final String RESPONSE_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=responseQueue";
    static final String LOGGER_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=loggerQueue";
    static final String DLQ_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=ActiveMQ.DLQ";
    static final String AMQ_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost";
    static final String QUEUE_SIZE_TEXT = "QueueSize";
    static final Integer MAX_AMQ_MEMORY_USAGE = Integer.valueOf(95); //over this memory usage (in percent) we have to reset the AMQ
    static final Long MAX_MULTIPLIER_OF_MESSAGE_OFF_LIMIT = Long.valueOf(4); // in case total message queue size is > Msg queue off limit * this value, we have to reset the AMQ

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
    private ObjectName amqObject;

    private boolean fIDecompressionEnabled = true;
    private boolean messageWritingEnabled = true;

    @Override
    public void run() {
        getSafeguardLimits();

        String jmxServiceUrl = JMX_SERVICE_PRE_URL + safeguardLimits.getJmxPort() + JMX_SERVICE_POST_URL;

        if (mBeanServerConnection == null || responseQueue == null || loggerQueue == null || dlqQueue == null || amqObject == null) {
            mBeanServerConnection = jmxConnectionBuilder.buildMBeanServerConnection(jmxServiceUrl);
            responseQueue = jmxObjectNameProvider.getObjectName(RESPONSE_QUEUE_OBJECT_NAME);
            loggerQueue = jmxObjectNameProvider.getObjectName(LOGGER_QUEUE_OBJECT_NAME);
            dlqQueue = jmxObjectNameProvider.getObjectName(DLQ_QUEUE_OBJECT_NAME);
            amqObject = jmxObjectNameProvider.getObjectName(AMQ_OBJECT_NAME);
        }

        Long totalQueueSize = retrieveQuerySize();

        setSafeguardFlags(totalQueueSize);

        resetDlqAsNecessary();
        resetAMQueueAsNecessary(totalQueueSize);
    }

    private void resetDlqAsNecessary() {
        boolean sizeIsValid = false;
        Long dlqSize = Long.valueOf(0);
        try {
            dlqSize = (Long) mBeanServerConnection.getAttribute(dlqQueue, QUEUE_SIZE_TEXT);
            sizeIsValid = true;
            if (dlqSize > 0) {
                mBeanServerConnection.invoke(dlqQueue, "purge", null, null);
                logger.info("Message found in ActiveMQ.DLQ. Queue size is: " + dlqSize + ", queue purged successfully.");
            }
        } catch (Exception e) {
            if (!(e instanceof InstanceNotFoundException)) {
                if (sizeIsValid) {
                    throw new SystemException("Message found in ActiveMQ.DLQ. Queue size is: " + dlqSize + ", QUEUE PURGE FAILED.", e);
                } else {
                    throw new SystemException("Message found in ActiveMQ.DLQ. Queue size cannot be detected.", e);
                } //otherwise no DLQ exists, and that is fine
            }
        }
    }

    /**
     * Need to restart whole AMQ in case
     * - the used memory is over MAX_AMQ_MEMORY_USAGE percent OR
     * - the totalQueueSize is higher than MAX_MULTIPLIER_OF_MESSAGE_OFF_LIMIT * Message OFF Limit
     * In extreme cases it may happen (very large messages), and the only possibility to survive is to reset the AMQ.
     *
     * @param totalQueueSize actual total size of the queues
     */
    private void resetAMQueueAsNecessary(final Long totalQueueSize) {
        boolean valueIsValid = false;
        Integer memoryPercentUsage = Integer.valueOf(0);
        Long totalQueueSizeLimit = MAX_MULTIPLIER_OF_MESSAGE_OFF_LIMIT * safeguardLimits.getMwOffLimit();
        if (totalQueueSize > totalQueueSizeLimit) {
            try {
                mBeanServerConnection.invoke(amqObject, "restart", null, null);
                logger.info("ActiveMQ Total Queue Size is too high (" + totalQueueSize + ">" + totalQueueSizeLimit + "), AMQ restart initiated.");
            } catch (Exception e) {
                throw new SystemException("ActiveMQ Total Queue Size is too high (\" + totalQueueSize + \">\" + totalQueueSizeLimit + \"), AMQ RESTART FAILED.", e);
            }
        }
        try {
            memoryPercentUsage = (Integer) mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage");
            valueIsValid = true;
            if (memoryPercentUsage > MAX_AMQ_MEMORY_USAGE) {
                mBeanServerConnection.invoke(amqObject, "restart", null, null);
                logger.info("ActiveMQ Memory usage is too high:" + memoryPercentUsage + "%, AMQ restart initiated.");
            }
        } catch (Exception e) {
            if (!(e instanceof InstanceNotFoundException)) {
                if (valueIsValid) {
                    throw new SystemException("ActiveMQ Memory usage is too high:" + memoryPercentUsage + "%, AMQ RESTART FAILED.", e);
                } else {
                    throw new SystemException("ActiveMQ Memory usage cannot be detected.", e);
                }
            }
        }

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
            Long responseQueueSize = (Long) mBeanServerConnection.getAttribute(responseQueue, QUEUE_SIZE_TEXT);
            Long loggerQueueSize = (Long) mBeanServerConnection.getAttribute(loggerQueue, QUEUE_SIZE_TEXT);
            queueSizeProvider.setResponseQueueSize(responseQueueSize);
            queueSizeProvider.setLoggerQueueSize(loggerQueueSize);
            return responseQueueSize + loggerQueueSize;
        } catch (Exception e) {
            throw new SystemException("Exception while monitoring queue sizes", e);
        }
    }

}
