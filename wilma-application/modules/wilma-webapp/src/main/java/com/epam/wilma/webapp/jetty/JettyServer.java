package com.epam.wilma.webapp.jetty;

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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.ServerProperties;

/**
 * Internal jetty server on which the wilma web application is run.
 * Serves requests that are redirected inside the proxy and not
 * forwarded to the real server.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JettyServer {

    private final Logger logger = LoggerFactory.getLogger(JettyServer.class);

    @Autowired
    private ServerFactory serverFactory;
    @Autowired
    private HandlerList handlerList;
    @Autowired
    private WebAppConfigurationAccess configurationAccess;

    private Server server;

    private ServerProperties serverProperties;

    /**
     * Starts the internal web application server.
     */
    public void start() {
        initializeJettyServer();
        server.setHandler(handlerList);
        startJettyServer();
    }

    /**
     * Stops the running internal web application server.
     */
    public void stop() {
        try {
            stopJettyServer();
        } catch (Exception e) {
            logger.error("Internal web application can not be stopped: " + e.getMessage(), e);
        }
    }

    void stopJettyServer() throws Exception {
        server.stop();
    }

    void startJettyServer() {
        try {
            server.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SystemException("Internal jetty server can not be started!", e);
        }
    }

    private void initializeJettyServer() {
        getServerProperties();
        server = serverFactory.createServer(serverProperties);
    }

    private void getServerProperties() {
        PropertyDTO properties = configurationAccess.getProperties();
        serverProperties = properties.getServerProperties();

    }
}
