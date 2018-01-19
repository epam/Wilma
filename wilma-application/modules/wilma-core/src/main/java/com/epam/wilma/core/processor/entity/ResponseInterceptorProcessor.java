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

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.router.RoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Calls all {@link ResponseInterceptor}s configured in the stub configuration.
 *
 * @author Tunde_Kovacs
 */
@Component
public class ResponseInterceptorProcessor extends ProcessorBase {

    private final Logger logger = LoggerFactory.getLogger(ResponseInterceptorProcessor.class);

    @Autowired
    private RoutingService routingService;

    @Override
    public void process(final WilmaHttpEntity entity) throws ApplicationException {
        Map<String, StubDescriptor> stubDescriptors = routingService.getStubDescriptors();
        for (String groupName : stubDescriptors.keySet()) {
            StubDescriptor stubDescriptor = stubDescriptors.get(groupName);
            List<InterceptorDescriptor> interceptorDescriptors = stubDescriptor.getInterceptorDescriptors();
            for (InterceptorDescriptor interceptorDescriptor : interceptorDescriptors) {
                ResponseInterceptor interceptor = interceptorDescriptor.getResponseInterceptor();
                callInterceptor(interceptor, entity, interceptorDescriptor);
            }
        }
    }

    private void callInterceptor(ResponseInterceptor interceptor, WilmaHttpEntity entity, InterceptorDescriptor interceptorDescriptor) {
        if (interceptor != null) {
            try {
                interceptor.onResponseReceive((WilmaHttpResponse) entity, interceptorDescriptor.getParams());
            } catch (Exception e) {
                logError(interceptor, entity, interceptorDescriptor.getParams(), e);
            }
        }
    }

    private void logError(final ResponseInterceptor interceptor, final WilmaHttpEntity entity, final ParameterList parameters, final Exception e) {
        logger.error("Error during call to response interceptor: " + interceptor.getClass().getSimpleName()
                + " with parameters: " + parameters.getAllParameters().toString()
                + " at message: " + entity.getWilmaMessageLoggerId()
                + "! Reason:" + e.getMessage(), e);
    }

}
