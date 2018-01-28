package com.epam.wilma.core.processor.entity;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.core.processor.entity.support.MockRequestInterceptor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.router.RoutingService;

/**
 * Unit tests for the class {@link RequestInterceptorProcessor}.
 * @author Tunde_Kovacs
 *
 */
public class RequestInterceptorProcessorTest {

    private static final ParameterList PARAMS = new ParameterList();

    @Mock
    private RoutingService routingService;
    @Mock
    private MockRequestInterceptor requestInterceptor;

    @InjectMocks
    private RequestInterceptorProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessShouldCallAllResponseInterceptors() throws ApplicationException {
        //GIVEN
        Map<String, StubDescriptor> stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put("test", createStubDescriptor());
        given(routingService.getStubDescriptors()).willReturn(stubDescriptors);
        WilmaHttpEntity entity = new WilmaHttpRequest();
        //WHEN
        underTest.process(entity);
        //THEN
        BDDMockito.verify(requestInterceptor).onRequestReceive((WilmaHttpRequest) entity, PARAMS);
    }

    @Test
    public void testProcessWhenThereAreNoInterceptorsShouldDoNothing() throws ApplicationException {
        //GIVEN
        Map<String, StubDescriptor> stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put("test", createStubDescriptorWithNoInterceptor());
        given(routingService.getStubDescriptors()).willReturn(stubDescriptors);
        WilmaHttpEntity entity = new WilmaHttpRequest();
        //WHEN
        underTest.process(entity);
        //THEN
        BDDMockito.verify(requestInterceptor, BDDMockito.never()).onRequestReceive((WilmaHttpRequest) entity, PARAMS);
    }

    private StubDescriptor createStubDescriptor() {
        List<InterceptorDescriptor> interceptorDescriptors = new ArrayList<>();
        InterceptorDescriptor interceptorDescriptor = new InterceptorDescriptor("requestInterceptor", requestInterceptor, null, PARAMS);
        interceptorDescriptors.add(interceptorDescriptor);
        return new StubDescriptor(null, null, interceptorDescriptors, null);
    }

    private StubDescriptor createStubDescriptorWithNoInterceptor() {
        List<InterceptorDescriptor> interceptorDescriptors = new ArrayList<>();
        InterceptorDescriptor interceptorDescriptor = new InterceptorDescriptor("responseInterceptor", null, null, PARAMS);
        interceptorDescriptors.add(interceptorDescriptor);
        return new StubDescriptor(null, null, interceptorDescriptors, null);
    }

}
