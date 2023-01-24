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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Provides tests for the class {@link JmsQueueMonitorTask}.
 *
 * @author Marton_Sereg
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(configurationAccess.getProperties()).willReturn(propertyDTO);
        given(propertyDTO.getSafeguardLimits()).willReturn(new SafeguardLimits(100L, 60L, 200L, 80L, "1099"));
    }

    @Test
    public final void testRunShouldSwitchOnlyFIOffWhenFIOffLimitExceededButMWOffLimitNotExceeded() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(91L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
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
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(191L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
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
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(11L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
        // WHEN
        underTest.run();
        // THEN
        verify(logger, never()).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger, never()).info("Due to Normal load, Message Logging is restored.");
        verify(logger, never()).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldNotSwitchOnAnythingWhenQueueSizeDidNotDropBelowAnyLimit() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", false);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", false);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(111L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
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
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", false);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", false);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(58L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
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
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", false);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", false);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(2L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(3L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
        // WHEN
        underTest.run();
        // THEN
        verify(logger, never()).info("Due to High load, FI decompression is turned OFF.");
        verify(logger, never()).info("Due to High load, Message Logging is turned OFF.");
        verify(logger).info("Due to Normal load, Message Logging is restored.");
        verify(logger).info("Due to Normal load, FI decompression is restored.");
    }

    @Test
    public final void testRunShouldThrowExceptionWhenQueueSizeCannotBeRetrieved() {
        Assertions.assertThrows(SystemException.class, () -> {
            // GIVEN
            ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
            ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
            given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willThrow(new AttributeNotFoundException());
            given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(91L);
            given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
            given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
            // WHEN
            underTest.run();
            // THEN exception is thrown
        });
    }

    @Test
    public final void testRunShouldThrowExceptionWhenMemoryUsageCannotBeRetrieved() {
        Assertions.assertThrows(SystemException.class, () -> {
            // GIVEN
            ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
            ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
            given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(2L);
            given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(91L);
            given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
            given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willThrow(new AttributeNotFoundException());
            // WHEN
            underTest.run();
            // THEN exception is thrown
        });
    }

    @Test
    public final void testRunShouldInitConnectionWhenLoggerQueueIsNull() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "loggerQueue", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldInitConnectionWhenResponseQueueIsNull() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "responseQueue", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldInitConnectionWhenMBeanServerConnectionIsNull() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "mBeanServerConnection", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldInitConnectionWhenAmqObjectIsNull() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "amqObject", null);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        givenJmxConnectionBuilder();
        // WHEN
        underTest.run();
        // THEN
        verifyConnectionBuilder();
    }

    @Test
    public final void testRunShouldSetQueueSizesIntoQueueSizeProvider() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(91L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
        // WHEN
        underTest.run();
        // THEN
        verify(queueSizeProvider).setResponseQueueSize(12L);
        verify(queueSizeProvider).setLoggerQueueSize(91L);
    }

    @Test
    public final void testRunShouldSaveQueueSizesIntoQueueSizeProvider() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(12L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(91L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
        // WHEN
        underTest.run();
        // THEN
        queueSizeProvider = (QueueSizeProvider) ReflectionTestUtils.getField(underTest, "queueSizeProvider");
        assert queueSizeProvider != null;
        long loggerQueueSize = queueSizeProvider.getLoggerQueueSize();
        assertEquals(91, loggerQueueSize);
        queueSizeProvider = (QueueSizeProvider) ReflectionTestUtils.getField(underTest, "queueSizeProvider");
        assert queueSizeProvider != null;
        long responseQueueSize = queueSizeProvider.getResponseQueueSize();
        assertEquals(12, responseQueueSize);
    }

    @Test
    public final void testRunShouldPurgeDlqQueue() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(1L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
        // WHEN
        underTest.run();
        // THEN
        verify(mBeanServerConnection).invoke(dlqQueue, "purge", null, null);
    }

    @Test
    public final void testRunShouldRestartAmqWhenAmqMemoryIsFull() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(1L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(JmsQueueMonitorTask.MAX_AMQ_MEMORY_USAGE + 1);
        // WHEN
        underTest.run();
        // THEN
        verify(mBeanServerConnection).invoke(amqObject, "restart", null, null);
    }

    @Test
    public final void testRunShouldRestartAmqWhenTotalQueueSizeIsTooBig() throws Exception {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fIDecompressionEnabled", true);
        ReflectionTestUtils.setField(underTest, "messageWritingEnabled", true);
        QueueSizeProvider queueSizeProvider = new QueueSizeProvider();
        ReflectionTestUtils.setField(underTest, "queueSizeProvider", queueSizeProvider);
        Long queueSizeIsTooBig = JmsQueueMonitorTask.MAX_MULTIPLIER_OF_MESSAGE_OFF_LIMIT * propertyDTO.getSafeguardLimits().getMwOffLimit() + 1;
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(queueSizeIsTooBig);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
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
        given(mBeanServerConnection.getAttribute(responseQueue, "QueueSize")).willReturn(2L);
        given(mBeanServerConnection.getAttribute(loggerQueue, "QueueSize")).willReturn(3L);
        given(mBeanServerConnection.getAttribute(dlqQueue, "QueueSize")).willReturn(0L);
        given(mBeanServerConnection.getAttribute(amqObject, "MemoryPercentUsage")).willReturn(0);
    }

    private void verifyConnectionBuilder() {
        verify(jmxConnectionBuilder).buildMBeanServerConnection(JmsQueueMonitorTask.JMX_SERVICE_PRE_URL + "1099" + JmsQueueMonitorTask.JMX_SERVICE_POST_URL);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.RESPONSE_QUEUE_OBJECT_NAME);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.LOGGER_QUEUE_OBJECT_NAME);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.DLQ_QUEUE_OBJECT_NAME);
        verify(jmxObjectNameProvider).getObjectName(JmsQueueMonitorTask.AMQ_OBJECT_NAME);
    }

}
