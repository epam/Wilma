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
 * Routes the "blocked" requests to the servlet that will answer back instead.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class LocalhostRequestRouter implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RouteEngineConfigurationAccess routeEngineConfigurationAccess;

    private final Logger logger = LoggerFactory.getLogger(LocalhostRequestRouter.class);
    private final String host = "http://127.0.0.1:%s/local/";
    private int internalPort;

    /**
     * Changes the requests destination URI to the servlet that will answer back that the request was blocked.
     * @param request the request to reroute
     */
    public void reroute(final WilmaHttpRequest request) {
        try {
            URI uri;
            uri = createUri();
            request.setUri(uri);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        PropertyDTO propertyDTO = routeEngineConfigurationAccess.getProperties();
        internalPort = propertyDTO.getProxyPort();
    }

    private URI createUri() throws URISyntaxException {
        return new URI(String.format(host, internalPort));
    }

}
