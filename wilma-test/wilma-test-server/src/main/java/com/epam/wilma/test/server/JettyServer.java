package com.epam.wilma.test.server;

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

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Create a new server that listens on the port given in the properties file, and adds the handler to the server.
 * @author Marton_Sereg
 *
 */
public class JettyServer {

    /**
     * Configures and starts a new Jetty server instance.
     * This server is configured to answer incoming request in a dummy way.
     * @param server the server that should be started
     * @param httpPort the http server port
     * @param httpsPort the https server port
     * @param isPerfTest determines whether server is started in performance test mode or not
     * @throws Exception
     */
    public void configureAndStart(final Server server, final Integer httpPort, final Integer httpsPort, final Boolean isPerfTest) {
        try {
            SelectChannelConnector httpConnector = createHttpConnector(httpPort);
            SslSelectChannelConnector sslConnector = createSslConnector(httpsPort);
            server.setConnectors(new Connector[]{httpConnector, sslConnector});
            if (isPerfTest) {
                server.setHandler(new PerformanceTestHandler());
            } else {
                server.setHandler(new CompositeHandler(new ExampleHandler(new InputStreamConverter()), new SequenceTestHandler(new InputStreamConverter()), new PerformanceTestHandler()));
            }
            startServer(server);
            server.join();
        } catch (Exception e) {
            throw new SystemException("server cannot be started", e);
        }
    }

    void startServer(final Server server) throws Exception {
        server.start();
    }

    private SelectChannelConnector createHttpConnector(final Integer port) {
        SelectChannelConnector httpConnector = createSelectChannelConnector();
        httpConnector.setPort(port);
        return httpConnector;
    }

    private SslSelectChannelConnector createSslConnector(final Integer port) {
        SslSelectChannelConnector sslConnector = createSslSelectChannelConnector();
        sslConnector.setPort(port);
        SslContextFactory cf = sslConnector.getSslContextFactory();
        cf.setKeyStorePath("certificate/wilmaKeystore.jks");
        cf.setKeyStorePassword("secret123");
        cf.setKeyManagerPassword("secret123");
        return sslConnector;
    }

    SelectChannelConnector createSelectChannelConnector() {
        return new SelectChannelConnector();
    }

    SslSelectChannelConnector createSslSelectChannelConnector() {
        return new SslSelectChannelConnector();
    }
}
