package com.epam.wilma.core.toggle.interceptor;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.core.processor.entity.ProcessorBase;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;

/**
 * Switches Interceptor usage on and off.
 * @author Tamas_Kohegyi
 *
 */

@Component
public class InterceptorModeToggle implements ApplicationListener<ContextRefreshedEvent> {

    private static final String ENABLED_FLAG = "on";

    @Autowired
    @Qualifier("requestInterceptorProcessor")
    private ProcessorBase requestInterceptorProcessor;
    @Autowired
    @Qualifier("responseInterceptorProcessor")
    private ProcessorBase responseInterceptorProcessor;
    @Autowired
    private WilmaHttpRequestProcessor requestProcessor;
    @Autowired
    private WilmaHttpResponseProcessor responseProcessor;
    @Autowired
    private CoreConfigurationAccess configurationAccess;

    /**
     * It runs after all beans have been initialized and their properties are set.
     * Turns off Interceptors if interceptor is turned off in the configuration.
     * @param contextRefreshedEvent raised when an ApplicationContext gets initialized or refreshed
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (isInterceptionEnabled(configurationAccess.getProperties())) {
            switchOnInterceptors();
        } else {
            switchOffInterceptors();
        }
    }

    private boolean isInterceptionEnabled(PropertyDto properties) {
        return ENABLED_FLAG.equalsIgnoreCase(properties.getInterceptorMode());
    }

    /**
     * Switches off Interceptors by removing the interceptors from the chain.
     */
    public void switchOffInterceptors() {
        requestProcessor.disableProcessor(requestInterceptorProcessor);
        responseProcessor.disableProcessor(responseInterceptorProcessor);
    }

    /**
     * Switches off Interceptors by removing the interceptors from the chain.
     */
    public void switchOnInterceptors() {
        requestProcessor.enableProcessor(requestInterceptorProcessor);
        responseProcessor.enableProcessor(responseInterceptorProcessor);
    }

    /**
     * Returns true if Interceptors is turned on at request path.
     * @return status of interceptor handling
     */
    public boolean isRequestInterceptorOn() {
        return requestProcessor.isProcessorEnabled(requestInterceptorProcessor);
    }

    /**
     * Returns true if Interceptors is turned on at response path.
     * @return status of interceptor handling
     */
    public boolean isResponseInterceptorOn() {
        return responseProcessor.isProcessorEnabled(responseInterceptorProcessor);
    }
}
