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

import com.epam.wilma.webapp.configuration.domain.ServerProperties;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.springframework.stereotype.Component;

/**
 * Factory for creating instances of {@link Server}.
 *
 * @author Tunde_Kovacs, Tamas Kohegyi
 */
@Component
public class ServerFactory {

    private static final int SHUTDOWN_TIMEOUT = 5000;

    /**
     * Creates a new instance of {@link Server} with the given port,
     * request response buffer size and response buffer size.
     *
     * @param serverProperties the properties required to configure the server
     * @return the instance
     */

    public Server createServer(final ServerProperties serverProperties) {
        Server server = new Server();
        HttpConfiguration httpConfiguration = getHttpConnector(serverProperties);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfiguration));
        http.setPort(serverProperties.getProxyPort());
        http.setIdleTimeout(30000);

        server.setConnectors(new Connector[]{http});
        server.setStopTimeout(SHUTDOWN_TIMEOUT);
        server.setStopAtShutdown(true);
        return server;
    }

    private HttpConfiguration getHttpConnector(final ServerProperties serverProperties) {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(serverProperties.getProxyPort());
        httpConfig.setOutputBufferSize(serverProperties.getResponseBufferSize());
        //NOTE: serverProperties.getRequestBufferSize() is not used anymore
        return httpConfig;
    }
    
}
