package com.epam.wilma.core.toggle.message;
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

import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.common.helper.SequenceHandlingState;
import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.core.processor.entity.JmsRequestLoggerProcessor;
import com.epam.wilma.core.processor.entity.JmsResponseProcessor;
import com.epam.wilma.core.processor.entity.ProcessorBase;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link MessageLoggingToggle}.
 *
 * @author Marton_Sereg
 */
public class MessageLoggingToggleTest {

    private PropertyDto.Builder builder;

    @InjectMocks
    private MessageLoggingToggle underTest;
    @Mock
    private JmsRequestLoggerProcessor jmsRequestLoggerProcessor;
    @Mock
    private JmsResponseProcessor jmsResponseProcessor;
    @Mock
    private WilmaHttpRequestProcessor requestHandler;
    @Mock
    private WilmaHttpResponseProcessor responseHandler;
    @Mock
    private CoreConfigurationAccess configurationAccess;
    @Mock
    private ContextRefreshedEvent event;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        builder = new PropertyDto.Builder().blockLocalhostUsage(BlockLocalhostUsage.OFF).interceptorMode("off").operationMode(OperationMode.WILMA)
                .sequenceHandlingState(SequenceHandlingState.OFF);
    }

    @Test
    public final void testOnApplicationEventShouldSwitchOffMessageLoggingWhenPropertyIsOff() {
        // GIVEN
        String messageLogging = "off";
        PropertyDto properties = builder.messageLogging(messageLogging).build();
        given(configurationAccess.getProperties()).willReturn(properties);
        // WHEN
        underTest.onApplicationEvent(event);
        // THEN
        verify(requestHandler).disableProcessor(jmsRequestLoggerProcessor);
        verify(responseHandler).disableProcessor(jmsResponseProcessor);
    }

    @Test
    public final void testOnApplicationEventShouldNotSwitchOffMessageLoggingWhenPropertyIsOn() {
        // GIVEN
        String messageLogging = "on";
        PropertyDto properties = builder.messageLogging(messageLogging).build();
        given(configurationAccess.getProperties()).willReturn(properties);
        // WHEN
        underTest.onApplicationEvent(event);
        // THEN
        verify(requestHandler, never()).disableProcessor(jmsRequestLoggerProcessor);
        verify(responseHandler, never()).disableProcessor(jmsResponseProcessor);
    }

    @Test
    public final void testOnApplicationEventShouldNotSwitchOffMessageLoggingWhenPropertyIsNull() {
        // GIVEN
        PropertyDto properties = builder.messageLogging(null).build();
        given(configurationAccess.getProperties()).willReturn(properties);
        // WHEN
        underTest.onApplicationEvent(event);
        // THEN
        verify(requestHandler, never()).disableProcessor(jmsRequestLoggerProcessor);
        verify(responseHandler, never()).disableProcessor(jmsResponseProcessor);
    }

    @Test
    public final void testSwitchOffMessageLoggingRemovesBothProcessors() {
        // GIVEN in setup
        // WHEN
        underTest.switchOffMessageLogging();
        // THEN
        verify(requestHandler).disableProcessor(jmsRequestLoggerProcessor);
        verify(responseHandler).disableProcessor(jmsResponseProcessor);
    }

    @Test
    public final void testSwitchOnMessageLoggingAddsBothProcessors() {
        // GIVEN in setup
        // WHEN
        underTest.switchOnMessageLogging();
        // THEN
        verify(requestHandler).enableProcessor(jmsRequestLoggerProcessor);
        verify(responseHandler).enableProcessor(jmsResponseProcessor);
    }

    @Test
    public final void testIsRequestLoggingOnReturnsTrueIfHandlerContainsProcessor() {
        // GIVEN in setup
        given(requestHandler.containsProcessor(jmsRequestLoggerProcessor)).willReturn(true);
        given(requestHandler.isProcessorEnabled(jmsRequestLoggerProcessor)).willReturn(true);
        // WHEN
        boolean actual = underTest.isRequestLoggingOn();
        // THEN
        verify(requestHandler).isProcessorEnabled(jmsRequestLoggerProcessor);
        assertTrue(actual);
    }

    @Test
    public final void testIsRequestLoggingOnReturnsFalseIfHandlerDoesNotContainProcessor() {
        // GIVEN in setup
        given(requestHandler.isProcessorEnabled(jmsRequestLoggerProcessor)).willReturn(false);
        // WHEN
        boolean actual = underTest.isRequestLoggingOn();
        // THEN
        verify(requestHandler).isProcessorEnabled(jmsRequestLoggerProcessor);
        assertFalse(actual);
    }

    @Test
    public final void testIsResponseLoggingOnReturnsTrueIfHandlerContainsProcessor() {
        // GIVEN in setup
        given(responseHandler.containsProcessor(jmsResponseProcessor)).willReturn(true);
        given(responseHandler.isProcessorEnabled(jmsResponseProcessor)).willReturn(true);
        // WHEN
        boolean actual = underTest.isResponseLoggingOn();
        // THEN
        verify(responseHandler).isProcessorEnabled(jmsResponseProcessor);
        assertTrue(actual);
    }

    @Test
    public final void testIsResponseLoggingOnReturnsFalseIfHandlerDoesNotContainProcessor() {
        // GIVEN in setup
        given(responseHandler.containsProcessor(jmsResponseProcessor)).willReturn(false);
        // WHEN
        boolean actual = underTest.isResponseLoggingOn();
        // THEN
        verify(responseHandler).isProcessorEnabled(jmsResponseProcessor);
        assertFalse(actual);
    }

}
