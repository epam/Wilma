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

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.configuration.domain.ServerProperties;

/**
 * Factory for creating instances of {@link Server}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ServerFactory {

    private static final int SHUTDOWN_TIMEOUT = 5000;

    /**
     * Creates a new instance of {@link Server} with the given port,
     * request response buffer size and response buffer size.
     * @param serverProperties the properties required to configure the server
     * @return the instance
     */
    public Server createServer(final ServerProperties serverProperties) {
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(serverProperties.getProxyPort());
        connector.setRequestBufferSize(serverProperties.getRequestBufferSize());
        connector.setResponseBufferSize(serverProperties.getResponseBufferSize());
        server.setConnectors(new Connector[]{connector});
        server.setGracefulShutdown(SHUTDOWN_TIMEOUT);
        server.setStopAtShutdown(true);
        return server;
    }

    /* TODO: NEXT GENERATION OF THE WILMA SERVER, with jetty 9.4.31.v20200723
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
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(serverProperties.getProxyPort());
        http_config.setOutputBufferSize(serverProperties.getResponseBufferSize());
        //NOTE: serverProperties.getRequestBufferSize() is not used anymore
        return http_config;
    }

    /*
    private Connector getHttpSConnector(final ServerProperties serverProperties) {

        // SSL Context Factory for HTTPS and SPDY
        // SSL requires a certificate so we configure a factory for ssl contents
        // with information pointing to what keystore the ssl connection needs
        // to know about. Much more configuration is available the ssl context,
        // including things like choosing the particular certificate out of a
        // keystore to be used.
        String keyStoreFile = "certificate/wilmaKeystore.jks";
        File keystoreFile = new File(keyStoreFile);
        if (!keystoreFile.exists())
        {
            throw new FileNotFoundException(keyStoreFile);
        }
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keyStoreFile);
        sslContextFactory.setKeyStorePassword("secret123");
        sslContextFactory.setKeyManagerPassword("secret123");

        // HTTPS Configuration
        // A new HttpConfiguration object is needed for the next connector and
        // you can pass the old one as an argument to effectively clone the
        // contents. On this HttpConfiguration object we add a
        // SecureRequestCustomizer which is how a new connector is able to
        // resolve the https connection before handing control over to the Jetty
        // Server.
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTPS connector
        // We create a second ServerConnector, passing in the http configuration
        // we just made along with the previously created ssl context factory.
        // Next we set the port and a longer idle timeout.
        ServerConnector https = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https_config));
        https.setPort(8443);
        https.setIdleTimeout(500000);

        // Here you see the server having multiple connectors registered with
        // it, now requests can flow into the server from both http and https
        // urls to their respective ports and be processed accordingly by jetty.
        // A simple handler is also registered with the server so the example
        // has something to pass requests off to.

    } */

}
