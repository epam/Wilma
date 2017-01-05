package com.epam.wilma.message.search.engine.bootstrap;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.engine.bootstrap.helper.SystemExceptionSelector;
import com.epam.wilma.message.search.engine.properties.PropertyLoader;
import com.epam.wilma.message.search.engine.properties.helper.PropertiesNotAvailableException;
import com.epam.wilma.message.search.properties.domain.exception.InvalidPropertyException;
import com.epam.wilma.message.search.web.WebAppServer;

/**
 * Bootstrap class that starts the application engine.
 * @author Tunde_Kovacs
 *
 */
public class MessageSearchBootstrap {

    private final Logger logger = LoggerFactory.getLogger(MessageSearchBootstrap.class);
    private final SystemExceptionSelector systemExceptionSelector = new SystemExceptionSelector();
    private final PropertyLoader propertyLoader = new PropertyLoader();

    /**
     * Starts the application.
     * @param args command line arguments
     */
    public void bootstrap(final String[] args) {
        WebAppServer webAppServer = createWebAppServer();
        try {
            Integer port = getPort(args);
            webAppServer.createServer(port);
            webAppServer.start();
        } catch (BeanCreationException e) {
            handleException(webAppServer, e);
        } catch (SystemException e) {
            logError(e);
            webAppServer.stop();
        }
    }

    WebAppServer createWebAppServer() {
        return new WebAppServer();
    }

    private Integer getPort(final String[] args) {
        checkPropertyFileArgument(args);
        Properties properties = propertyLoader.loadProperties(args[0]);
        Integer port = null;
        try {
            port = Integer.valueOf(properties.getProperty("webapp.port"));
        } catch (NumberFormatException e) {
            throw new InvalidPropertyException("Invalid port value!");
        }
        return port;
    }

    private void checkPropertyFileArgument(final String[] args) {
        if (args.length == 0) {
            throw new PropertiesNotAvailableException("Configuration file was not specified as input argument!");
        } else if (!args[0].endsWith(".properties")) {
            throw new PropertiesNotAvailableException("Configuration file must be a properties file!");
        }
    }

    private void handleException(final WebAppServer webAppServer, final BeanCreationException e) {
        SystemException ex = systemExceptionSelector.getSystemException(e);
        if (ex != null) {
            logError(ex);
            webAppServer.stop();
        } else {
            throw e;
        }
    }

    private void logError(final Exception e) {
        logger.error("Application cannot be started: " + e.getMessage());
    }
}
