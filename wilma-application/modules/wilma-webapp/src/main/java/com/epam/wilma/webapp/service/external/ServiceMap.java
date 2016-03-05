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
import com.epam.wilma.router.RoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tamas_Kohegyi
 */
@Component
public class ServiceMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMap.class);

    @Autowired
    RoutingService routingService;

    private Map<String, String> serviceMap = new ConcurrentHashMap<>();

    public String callExternalService(final HttpServletRequest req, final String requestedService, HttpServletResponse resp) {
        String className = serviceMap.get(requestedService);
        if (className != null) {
            //we found the class that should be called, so create it, and call the method

        }
        return null;
    }

    public void addServices(final Set<String> newServices) {

    }

    public void detectServices() {
        LOGGER.info("Detecting External Services...");
        Map<String, StubDescriptor> descriptors = routingService.getStubDescriptors();
    }
}
