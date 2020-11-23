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
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.File;
import java.io.FileNotFoundException;

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
     */
    public void configureAndStart(final Server server, final Integer httpPort, final Integer httpsPort, final Boolean isPerfTest) {
        try {
            // HTTP Configuration
            // HttpConfiguration is a collection of configuration information
            // appropriate for http and https. The default scheme for http is
            // <code>http</code> of course, as the default for secured http is
            // <code>https</code> but we show setting the scheme to show it can be
            // done. The port for secured communication is also set here.
            HttpConfiguration http_config = new HttpConfiguration();
            http_config.setSecureScheme("https");
            http_config.setSecurePort(httpsPort);
            http_config.setOutputBufferSize(32768);

            // HTTP connector
            // The first server connector we create is the one for http, passing in
            // the http configuration we configured above so it can get things like
            // the output buffer size, etc. We also set the port (8080) and
            // configure an idle timeout.
            ServerConnector http = new ServerConnector(server,
                    new HttpConnectionFactory(http_config));
            http.setPort(httpPort);
            http.setIdleTimeout(30000);

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
            server.setConnectors(new Connector[]{http, https});
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

}
