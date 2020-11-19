package com.epam.wilma.browsermob.proxy;
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

import net.lightbody.bmp.proxy.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.proxy.Proxy;
import com.epam.wilma.browsermob.configuration.ProxyConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.ProxyPropertyDTO;
import com.epam.wilma.browsermob.domain.exception.ProxyCannotBeStartedException;
import com.epam.wilma.browsermob.interceptor.BrowserMobRequestInterceptor;
import com.epam.wilma.browsermob.interceptor.BrowserMobResponseInterceptor;

/**
 * Class that creates and starts a new BrowserMob proxy server.
 * @author Marton_Sereg, Tunde_Kovacs, Tamas_Bihari, Tamas Kohegyi
 *
 */
@Component
public class BrowserMobProxy implements Proxy {

    private final Logger logger = LoggerFactory.getLogger(BrowserMobProxy.class);
    private Integer proxyPort;
    private Integer requestTimeout;
    private Boolean responseVolatile;
    private Boolean shouldKeepSslConnectionAlive;

    @Autowired
    private ProxyServer server;
    @Autowired
    private BrowserMobRequestInterceptor requestInterceptor;
    @Autowired
    private BrowserMobResponseInterceptor responseInterceptor;
    @Autowired
    private ProxyConfigurationAccess configurationAccess;

    public static boolean getResponseVolatile() {
        return ProxyServer.getResponseVolatile();
    }

    /**
     * Global setter of the proxy behaviour. When it is volatile=true,
     * the response of the messages will be volatile (can be changed), otherwise not.
     * Note1: Requests are always volatile.
     * Note2: This global setting can be overwritten per message base, using the request interceptors.
     *
     * @param responseVolatility must be either true (volatile) or false (not volatile).
     */
    public static void setResponseVolatile(boolean responseVolatility) {
        ProxyServer.setResponseVolatile(responseVolatility);
    }

    public static boolean getShouldKeepSslConnectionAlive() {
        return ProxyServer.getShouldKeepSslConnectionAlive();
    }

    /**
     * Global setter of the proxy behaviour. When it is shouldKeepSslConnectionAlive=true,
     * after the CONNECT the connection keeps opened (will be closed only by socket connection timeouts),
     * otherwise the CONNECT request will be closed (normal behaviour).
     * Default is false, but some clients (.NET Web Application Clients for example) may need to set it to true.
     *
     * @param shouldKeepSslConnectionAlive must be either true (keep CONNECT request alive) or false (close CONNECT request).
     */
    public static void setShouldKeepSslConnectionAlive(boolean shouldKeepSslConnectionAlive) {
        ProxyServer.setShouldKeepSslConnectionAlive(shouldKeepSslConnectionAlive);
    }

    @Override
    public void start() {
        try {
            getProperties();
            server.setPort(proxyPort);
            server.start(requestTimeout);
            ProxyServer.setResponseVolatile(responseVolatile);
            ProxyServer.setShouldKeepSslConnectionAlive(shouldKeepSslConnectionAlive);
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
        shouldKeepSslConnectionAlive = properties.getShouldKeepSslConnectionAlive();
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
