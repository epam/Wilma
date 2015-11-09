package com.epam.wilma.test.server;

/*==========================================================================
Copyright 2015 EPAM Systems

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the properties and handles SystemExceptions.
 * @author Marton_Sereg
 *
 */
public class TestServerBootstrap {

    private Logger logger = LoggerFactory.getLogger(TestServerBootstrap.class);

    /**
     * Bootstraps the application by loading the properties and starting the server.
     * @param args the program arguments
     * @param properties where the properties should be loaded
     * @param jettyServer that should be configured and started
     */
    public void bootstrap(final String[] args, final Properties properties, final JettyServer jettyServer) {
        if (args.length == 1) {
            try {
                String fileName = args[0];
                if (args[0].endsWith(".properties")) {
                    InputStream inputStream = new FileInputStream(fileName);
                    properties.load(inputStream);
                    Integer httpPort = Integer.parseInt(properties.getProperty("server.port.http"));
                    Integer httpsPort = Integer.parseInt(properties.getProperty("server.port.https"));
                    Boolean isPerfTest = Boolean.valueOf(properties.getProperty("perftest"));
                    jettyServer.configureAndStart(new Server(), httpPort, httpsPort, isPerfTest);
                } else {
                    logger.error("Specified property file's extension is not \"properties\"!");
                }
            } catch (FileNotFoundException e) {
                logger.error("Specified property file not found!", e);
            } catch (NumberFormatException e) {
                logger.error("One of server.port.http and server.port.https properties is not a valid port number.", e);
            } catch (IOException e) {
                logger.error("Property file cannot be read.");
            } catch (SystemException e) {
                e.logStackTrace(logger);
            }
        } else {
            logger.error("Exactly one program argument expected (properties file)!");
        }
    }

    void setLogger(final Logger logger) {
        this.logger = logger;
    }
}
