package com.epam.wilma.core.toggle.mode;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.common.helper.SequenceHandlingState;
import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.core.processor.entity.ProcessorBase;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;

/**
 * Provides unit tests for the class {@link ProxyModeToggle}.
 * @author Tunde_Kovacs
 *
 */
public class ProxyModeToggleTest {

    private PropertyDto.Builder builder;

    @Mock
    private ProcessorBase routerProcessor;
    @Mock
    private WilmaHttpRequestProcessor requestProcessor;
    @Mock
    private CoreConfigurationAccess configurationAccess;
    @Mock
    private ContextRefreshedEvent event;

    @InjectMocks
    private ProxyModeToggle underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        builder = new PropertyDto.Builder().blockLocalhostUsage(BlockLocalhostUsage.OFF).interceptorMode("off").messageLogging("off")
                .sequenceHandlingState(SequenceHandlingState.OFF);
    }

    @Test
    public void testOnApplicationEventWhenModeIsProxyShouldSwitchProxyModeOn() {
        //GIVEN
        OperationMode operationMode = OperationMode.PROXY;
        PropertyDto properties = builder.operationMode(operationMode).build();
        given(configurationAccess.getProperties()).willReturn(properties);
        given(requestProcessor.containsProcessor(routerProcessor)).willReturn(true);
        given(requestProcessor.isProcessorEnabled(routerProcessor)).willReturn(true);
        //WHEN
        underTest.onApplicationEvent(event);
        //THEN
        verify(requestProcessor).disableProcessor(routerProcessor);
    }

    @Test
    public void testInitOperationModeWhenModeIsNotProxyShouldDoNothing() {
        //GIVEN
        OperationMode operationMode = OperationMode.WILMA;
        PropertyDto properties = builder.operationMode(operationMode).build();
        given(configurationAccess.getProperties()).willReturn(properties);
        given(requestProcessor.containsProcessor(routerProcessor)).willReturn(false);
        //WHEN
        underTest.onApplicationEvent(event);
        //THEN
        verify(requestProcessor, never()).disableProcessor(routerProcessor);
    }

    @Test
    public void testSwitchProxyModeOnWhenRouterProcessorIsInListShouldDisableProcessor() {
        //GIVEN
        given(requestProcessor.containsProcessor(routerProcessor)).willReturn(true);
        //WHEN
        underTest.switchProxyModeOn();
        //THEN
        verify(requestProcessor).disableProcessor(routerProcessor);
    }

    @Test
    public void testSwitchProxyModeOnWhenRouterProcessorIsNotInListShouldDoNothing() {
        //GIVEN
        given(requestProcessor.containsProcessor(routerProcessor)).willReturn(false);
        //WHEN
        underTest.switchProxyModeOn();
        //THEN
        verify(requestProcessor, never()).disableProcessor(routerProcessor);
    }

    @Test
    public void testSwitchProxyModeOffWhenRouterProcessorIsNotInListShouldEnableProcessor() {
        //GIVEN
        given(requestProcessor.containsProcessor(routerProcessor)).willReturn(true);
        given(requestProcessor.isProcessorEnabled(routerProcessor)).willReturn(false);
        //WHEN
        underTest.switchProxyModeOff();
        //THEN
        verify(requestProcessor).enableProcessor(routerProcessor);
    }

}
