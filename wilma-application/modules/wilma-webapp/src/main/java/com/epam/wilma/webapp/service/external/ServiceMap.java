package com.epam.wilma.webapp.service.external;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.router.RoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that coordinates the activities of the external REST services.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class ServiceMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMap.class);
    private final Object o = new Object();
    @Autowired
    private RoutingService routingService;
    private Map<String, ExternalWilmaService> serviceMap = new ConcurrentHashMap<>();

    /**
     * Method to call the preoper registered external service, based on the request URI.
     *
     * @param req              is the original request
     * @param requestedService is the request for the service
     * @param resp             is the response, default status of the response is SC_OK (200), and response type is application/json
     * @return with the body of the response (a JSON response)
     */
    public String callExternalService(final HttpServletRequest req, final String requestedService, HttpServletResponse resp) {
        ExternalWilmaService service;
        synchronized (o) {
            service = serviceMap.get(requestedService);
        }
        if (service != null) {
            //we found the service class that should be called, so call it
            try {
                service.handleRequest(req, requestedService, resp);
            } catch (Exception e) {
                logError(service, requestedService, e);
            }
        }
        return null;
    }

    private void logError(final ExternalWilmaService service, final String requestedService, final Exception e) {
        LOGGER.error("Error during call to external service: " + service.getClass().getSimpleName()
                + " with requested service: " + requestedService
                + "! Reason:" + e.getMessage(), e);
    }

    /**
     * Method that collects the available external service entry points. Right now only interceptors can be used for this purpose.
     */
    public void detectServices() {
        Map<String, ExternalWilmaService> newServiceMap = new ConcurrentHashMap<>();
        Map<String, StubDescriptor> descriptors = routingService.getStubDescriptors();
        for (String key : descriptors.keySet()) {
            StubDescriptor stubDescriptor = descriptors.get(key);
            if (stubDescriptor != null) {
                for (InterceptorDescriptor interceptorDescriptor : stubDescriptor.getInterceptorDescriptors()) {
                    RequestInterceptor requestInterceptor = interceptorDescriptor.getRequestInterceptor();
                    if (requestInterceptor instanceof ExternalWilmaService) {
                        ExternalWilmaService service = (ExternalWilmaService) requestInterceptor;
                        for (String handler : service.getHandlers()) {
                            newServiceMap.putIfAbsent(handler, service);
                        }
                    }
                    ResponseInterceptor responseInterceptor = interceptorDescriptor.getResponseInterceptor();
                    if (responseInterceptor instanceof ExternalWilmaService) {
                        ExternalWilmaService service = (ExternalWilmaService) responseInterceptor;
                        for (String handler : service.getHandlers()) {
                            newServiceMap.putIfAbsent(handler, service);
                        }
                    }
                }
            }
        }
        //new service map created
        synchronized (o) {
            serviceMap.clear();
            serviceMap = newServiceMap;
        }
    }
}
