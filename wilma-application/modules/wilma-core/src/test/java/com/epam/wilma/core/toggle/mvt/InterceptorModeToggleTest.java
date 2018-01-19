package com.epam.wilma.core.toggle.mvt;
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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertTrue;

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
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import com.epam.wilma.core.toggle.interceptor.InterceptorModeToggle;

/**
 * Provides unit tests for the class {@link com.epam.wilma.core.toggle.interceptor.InterceptorModeToggle}.
 * @author Tamas_Kohegyi
 *
 */
public class InterceptorModeToggleTest {

    @Mock
    private ProcessorBase requestInterceptorProcessor;
    @Mock
    private ProcessorBase responseInterceptorProcessor;
    @Mock
    private WilmaHttpRequestProcessor requestProcessor;
    @Mock
    private WilmaHttpResponseProcessor responseProcessor;
    @Mock
    private ContextRefreshedEvent event;
    @Mock
    private CoreConfigurationAccess configurationAccess;

    @InjectMocks
    private InterceptorModeToggle underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnApplicationEventWhenModeIsInterceptorOn() {
        //GIVEN
        setupInterceptorConfiguration("on");
        // WHEN
        underTest.onApplicationEvent(event);
        // THEN
        verify(requestProcessor).enableProcessor(requestInterceptorProcessor);
        verify(responseProcessor).enableProcessor(responseInterceptorProcessor);
    }

    @Test
    public void testOnApplicationEventWhenModeIsInterceptorOff() {
        //GIVEN
        setupInterceptorConfiguration("off");
        // WHEN
        underTest.onApplicationEvent(event);
        // THEN
        verify(requestProcessor).disableProcessor(requestInterceptorProcessor);
        verify(responseProcessor).disableProcessor(responseInterceptorProcessor);
    }

    @Test
    public void testOnApplicationEventWhenModeHasNoPropertyShouldTurnInterceptionOff() {
        //GIVEN
        setupInterceptorConfiguration(null);
        // WHEN
        underTest.onApplicationEvent(event);
        // THEN
        verify(requestProcessor).disableProcessor(requestInterceptorProcessor);
        verify(responseProcessor).disableProcessor(responseInterceptorProcessor);
    }

    @Test
    public void testSwitchOnInterceptors() {
        //GIVEN initial state
        // WHEN
        underTest.switchOnInterceptors();
        // THEN
        verify(requestProcessor).enableProcessor(requestInterceptorProcessor);
        verify(responseProcessor).enableProcessor(responseInterceptorProcessor);
    }

    @Test
    public void testSwitchOffInterceptors() {
        //GIVEN initial state
        // WHEN
        underTest.switchOffInterceptors();
        // THEN
        verify(requestProcessor).disableProcessor(requestInterceptorProcessor);
        verify(responseProcessor).disableProcessor(responseInterceptorProcessor);
    }

    @Test
    public void testIsRequestInterceptorOn() {
        //GIVEN
        given(requestProcessor.isProcessorEnabled(requestInterceptorProcessor)).willReturn(true);
        // WHEN
        boolean requestInterceptorOn = underTest.isRequestInterceptorOn();
        // THEN
        assertTrue(requestInterceptorOn);
    }

    @Test
    public void testIsResponseInterceptorOn() {
        //GIVEN
        given(responseProcessor.isProcessorEnabled(responseInterceptorProcessor)).willReturn(true);
        // WHEN
        boolean responseInterceptorOn = underTest.isResponseInterceptorOn();
        // THEN
        assertTrue(responseInterceptorOn);
    }

    private void setupInterceptorConfiguration(final String interceptorMode) {
        PropertyDto properties = new PropertyDto.Builder().interceptorMode(interceptorMode).operationMode(OperationMode.WILMA)
                .blockLocalhostUsage(BlockLocalhostUsage.OFF).sequenceHandlingState(SequenceHandlingState.OFF).build();
        given(configurationAccess.getProperties()).willReturn(properties);
    }
}
