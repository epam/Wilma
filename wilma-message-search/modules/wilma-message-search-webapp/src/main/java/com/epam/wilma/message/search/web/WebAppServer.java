package com.epam.wilma.message.search.web;

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

import java.net.URL;
import java.util.concurrent.Executors;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.epam.wilma.message.search.web.domain.exception.ServerException;
import com.epam.wilma.message.search.web.service.WebAppStopper;

/**
 * Responsible for configuring, starting and
 * stopping the jetty server.
 * @author Tamas_Bihari
 *
 */

public class WebAppServer {

    private static final String SHUTDOWN_URL = "/shutdown";
    private static final int STOP_TIMEOUT = 5000;
    private static final String WEB_XML_LOCATION = "/WEB-INF/web.xml";
    private static final String WEBAPP_ROOT = "webapp";
    private Server server;

    /**
     * Creates and configures the webapp server.
     * @param port the port the server listens on
     */
    public void createServer(final Integer port) {
        WebAppContext context = configureWebAppContext();
        createServer(context, port);
    }

    /**
     * Starts the jetty server.
     */
    public void start() {
        try {
            startJettyServer();
        } catch (Exception e) {
            throw new ServerException("Jetty server cannot be started. Reason: " + e.getMessage(), e);
        }
    }

    /**
     * Stops the server.
     */
    public void stop() {
        try {
            if (server != null && server.isStarted()) {
                stopJettyServer();
            }
        } catch (Exception e) {
            throw new ServerException("Internal web application can not be stopped: " + e.getMessage(), e);
        }
    }

    private WebAppContext configureWebAppContext() {
        final WebAppContext context = new WebAppContext();
        String baseUrl = getBaseUrl();
        context.setDescriptor(baseUrl + WEB_XML_LOCATION);
        context.setResourceBase(baseUrl + "");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        WebAppStopper webAppStopper = new WebAppStopper(this, Executors.newSingleThreadExecutor());
        Servlet shutdownServlet = new ShutdownServlet(webAppStopper);
        context.addServlet(new ServletHolder(shutdownServlet), SHUTDOWN_URL);
        return context;
    }

    void createServer(final WebAppContext context, final Integer port) {
        server = new Server(port);
        server.setStopAtShutdown(true);
        server.setStopTimeout(STOP_TIMEOUT);
        server.setHandler(context);
    }

    void startJettyServer() throws Exception {
        server.start();
        server.join();
    }

    void stopJettyServer() throws Exception {
        server.stop();
        server.join();
    }

    private String getBaseUrl() {
        URL webInfUrl = WebAppServer.class.getClassLoader().getResource(WEBAPP_ROOT);
        return webInfUrl.toExternalForm();
    }

}
