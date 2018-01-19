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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Provides tests for the class {@link JmsQueueMonitorTask}.
 * @author Marton_Sereg
 *
 */
public class JmsQueueMonitorTaskTest {

    @InjectMocks
    private JmsQueueMonitorTask underTest;
    @Mock
    private JmxConnectionBuilder jmxConnectionBuilder;
    @Mock
    private JmxObjectNameProvider jmxObjectNameProvider;
    @Mock
    private MBeanServerConnection mBeanServerConnection;
    @Mock
    private ObjectName responseQueue;
    @Mock
    private ObjectName loggerQueue;
    @Mock
    private ObjectName dlqQueue;
    @Mock
    private ObjectName amqObject;
    @Mock
    private Logger logger;
    @Mock
    private SafeguardController safeguardController;
    @Mock
    private SafeguardConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO propertyDTO;
    @Mock
    private QueueSizeProvider queueSizeProvider;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
        given(configurationAccess.getProperties()).willReturn(propertyDTO);
        given(propertyDTO.getSafeguardLimits()).willReturn(new SafeguardLimits(new Long(100), new Long(60), new Long(200), new Long(80), "1099"));
    }

    @Test
    public final void testRunShouldSwitchOnlyFIOffWhenFIOffLimitExceededButMWOffLimitNotExceeded() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(91));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(logger).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger, never()).info("Due to Normal load, Message Logging is restored.");
        verify(logger, never()).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldSwitchFIAndMWOffWhenFIAndMwOffLimitExceeded() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(191));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(logger).info("Due to High load, FI decompression is turned OFF.");
        verify(logger).info("Due to High load, Message Logging is turned OFF.");
        verify(logger, never()).info("Due to Normal load, Message Logging is restored.");
        verify(logger, never()).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldNotSwitchOffAnythingWhenLimitsAreNotExceeded() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(11));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(logger, never()).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger, never()).info("Due to Normal load, Message Logging is restored.");
        verify(logger, never()).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldntSwitchOnAnythingWhenQueueSizeDidntDropBelowAnyLimit() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", false);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", false);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(111));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(logger, never()).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger, never()).info("Due to Normal load, Message Logging is restored.");
        verify(logger, never()).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldSwitchMWOnWhenQueueSizeDroppedBelowMWLimit() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", false);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", false);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(58));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(logger, never()).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger).info("Due to Normal load, Message Logging is restored.");
        verify(logger, never()).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldSwitchMWAndFIOnWhenQueueSizeDroppedBelowFILimit() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", false);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", false);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(2));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(3));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(logger, never()).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger).info("Due to Normal load, Message Logging is restored.");
        verify(logger).info("Due to Normal load, FI decompression is restored.");
    }

    @Test(expectedExceptions = SystemException.class)
    public final void testRunShouldThrowExceptionWhenQueueSizeCannotBeRetrieved() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willThrow(new AttributeNotFoundException());
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(91));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN exception is thrown
    }

    @Test(expectedExceptions = SystemException.class)
    public final void testRunShouldThrowExceptionWhenMemoryUsageCannotBeRetrieved() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(2));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(91));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willThrow(new AttributeNotFoundException());
        // WHEN
        underTest.run();
        // THEN exception is thrown
    }

    @Test
    public final void testRunShouldInitConnectionWhenLoggerQueueIsNull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "loggerQueue", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldInitConnectionWhenResponseQueueIsNull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "responseQueue", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldInitConnectionWhenMBeanServerConnectionIsNull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "mBeanServerConnection", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldInitConnectionWhenAmqObjectIsNull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "amqObject", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldSetQueueSizesIntoQueueSizeProvider() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(91));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(queueSizeProvider).setResponseQueueSize(new Long(12));
        verify(queueSizeProvider).setLoggerQueueSize(new Long(91));
    }

    @Test
    public final void testRunShouldSaveQueueSizesIntoQueueSizeProvider() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(12));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(91));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        long loggerQueueSize = ((QueueSizeProvider) Whitebox.getInternalState(underTest, "queueSizeProvider")).getLoggerQueueSize();
        Assert.assertEquals(loggerQueueSize, 91);
        long responseQueueSize = ((QueueSizeProvider) Whitebox.getInternalState(underTest, "queueSizeProvider")).getResponseQueueSize();
        Assert.assertEquals(responseQueueSize, 12);
    }

    @Test
    public final void testRunShouldPurgeDlqQueue() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(1));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(mBeanServerConnection).invoke(dlqQueue, "purge", null, null);
    }

    @Test
    public final void testRunShouldRestartAmqWhenAmqMemoryIsFull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(1));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(JmsQueueMonitorTask.MAX_AMQ_MEMORY_USAGE + 1));
        // WHEN
        underTest.run();
        // THEN
        verify(mBeanServerConnection).invoke(amqObject, "restart", null, null);
    }

    @Test
    public final void testRunShouldRestartAmqWhenTotalQueueSizeIsTooBig() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "fIDecompressionEnabled", true);
        Whitebox.setInternalState(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        Whitebox.setInternalState(underTest, "queueSizeProvider", queueSizeProvider);
        Long queueSizeIsTooBig = JmsQueueMonitorTask.MAX_MULTIPLIER_OF_MESSAGE_OFF_LIMIT * propertyDTO.getSafeguardLimits().getMwOffLimit() + 1;
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(queueSizeIsTooBig);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
        // WHEN
        underTest.run();
        // THEN
        verify(mBeanServerConnection).invoke(amqObject, "restart", null, null);
    }

    private void givenJmxConnectionBuilder() throws Exception {
        given(jmxConnectionBuilder.buildMBeanServerConnection(JmsQueueMonitorTask.JMX_SERVICE_PRE_URL
                + "1099" + JmsQueueMonitorTask.JMX_SERVICE_POST_URL)).willReturn(mBeanServerConnection);
        given(jmxObjectNameProvider.getObjectName(JmsQueueMonitorTask.RESPONSE_QUEUE_OBJECT_NAME)).willReturn(responseQueue);
        given(jmxObjectNameProvider.getObjectName(JmsQueueMonitorTask.LOGGER_QUEUE_OBJECT_NAME)).willReturn(loggerQueue);
        given(jmxObjectNameProvider.getObjectName(JmsQueueMonitorTask.DLQ_QUEUE_OBJECT_NAME)).willReturn(dlqQueue);
        given(jmxObjectNameProvider.getObjectName(JmsQueueMonitorTask.AMQ_OBJECT_NAME)).willReturn(amqObject);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(new Long(2));
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(new Long(3));
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(new Long(0));
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(new Integer(0));
    }

    private void verifyConnectionBuilder() {
        verify(jmxConnectionBuilder).buildMBeanServerConnection(JmsQueueMonitorTask.JMX_SERVICE_PRE_URL + "1099" + JmsQueueMonitorTask.JMX_SERVICE_POST_URL);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.RESPONSE_QUEUE_OBJECT_NAME);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.LOGGER_QUEUE_OBJECT_NAME);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.DLQ_QUEUE_OBJECT_NAME);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.AMQ_OBJECT_NAME);
    }

}
