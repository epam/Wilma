package com.epam.wilma.browsermob.proxy;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import net.lightbody.bmp.proxy.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.browsermob.Proxy;
import com.epam.wilma.browsermob.configuration.BrowserMobConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.ProxyPropertyDTO;
import com.epam.wilma.browsermob.domain.exception.ProxyCannotBeStartedException;
import com.epam.wilma.browsermob.interceptor.BrowserMobRequestInterceptor;
import com.epam.wilma.browsermob.interceptor.BrowserMobResponseInterceptor;

/**
 * Class that creates and starts a new BrowserMob proxy server.
 * @author Marton_Sereg, Tunde_Kovacs, Tamas_Bihari
 *
 */
@Component
public class BrowserMobProxy implements Proxy {

    private final Logger logger = LoggerFactory.getLogger(BrowserMobProxy.class);
    private Integer proxyPort;
    private Integer requestTimeout;
    private Boolean responseVolatile;

    @Autowired
    private ProxyServer server;
    @Autowired
    private BrowserMobRequestInterceptor requestInterceptor;
    @Autowired
    private BrowserMobResponseInterceptor responseInterceptor;
    @Autowired
    private BrowserMobConfigurationAccess configurationAccess;

    @Override
    public void start() {
        try {
            getProperties();
            server.setPort(proxyPort);
            server.start(requestTimeout);
            server.setResponseVolatile(responseVolatile);
            server.setCaptureContent(true);
            server.setCaptureBinaryContent(true);
            server.addRequestInterceptor(requestInterceptor);
            server.addResponseInterceptor(responseInterceptor);
        } catch (Exception e) {
            throw new ProxyCannotBeStartedException("Starting the proxy server failed.", e);
        }
    }

    private void getProperties() {
        ProxyPropertyDTO properties = configurationAccess.getProperties();
        proxyPort = properties.getProxyPort();
        requestTimeout = properties.getRequestTimeout();
        responseVolatile = properties.getAllowResponseUpdate();
    }

    @Override
    public void stop() {
        try {
            stopServer();
        } catch (Exception e) {
            logger.error("Proxy can not be stopped: " + e.getMessage(), e);
        }
    }

    void stopServer() throws Exception {
        server.stop();
    }
}
