package com.epam.wilma.router;
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

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.configuration.RouteEngineConfigurationAccess;
import com.epam.wilma.router.configuration.domain.PropertyDTO;

/**
 * Redirection of incoming requests is done here to internal web application.
 * @author Tunde_Kovacs
 */
@Component
public class Router implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(Router.class);
    private String host = "http://127.0.0.1:";
    private String path = "/stub/";
    private Integer internalPort;

    @Autowired
    private RoutingService routingService;
    @Autowired
    private RouteEngineConfigurationAccess configurationAccess;

    /**
    * Redirects requests by changing their URI.
    * @param request the request that is redirected
    */
    public void reroute(final WilmaHttpRequest request) {
        if (routingService.redirectRequestToStub(request)) {
            try {
                request.setUri(getURI());
            } catch (URISyntaxException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        PropertyDTO properties = configurationAccess.getProperties();
        internalPort = properties.getProxyPort();
    }

    public Integer getInternalPort() {
        return internalPort;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    URI getURI() throws URISyntaxException {
        return new URI(host + internalPort + path);
    }

}
