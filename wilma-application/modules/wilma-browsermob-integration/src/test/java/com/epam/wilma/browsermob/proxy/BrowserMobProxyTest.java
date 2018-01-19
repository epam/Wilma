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

import com.epam.wilma.browsermob.configuration.BrowserMobConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.ProxyPropertyDTO;
import com.epam.wilma.browsermob.domain.exception.ProxyCannotBeStartedException;
import com.epam.wilma.browsermob.interceptor.BrowserMobRequestInterceptor;
import com.epam.wilma.browsermob.interceptor.BrowserMobResponseInterceptor;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.jetty.http.SocketListener;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

/**
 * Test class for BrowserMobProxy.
 *
 * @author Marton_Sereg
 */
public class BrowserMobProxyTest {

    private static final String EXCPEPTION_MESSAGE = "excpeption message";
    private ProxyPropertyDTO propertiesDTO;

    @InjectMocks
    private BrowserMobProxy underTest;

    @Mock
    private ProxyServer server;
    @Mock
    private BrowserMobRequestInterceptor loggingRequestInterceptor;
    @Mock
    private BrowserMobResponseInterceptor loggingResponseInterceptor;
    @Mock
    private BrowserMobConfigurationAccess configurationAccess;
    @Mock
    private SocketListener listener;
    @Mock
    private Path path;
    @Mock
    private Logger logger;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new BrowserMobProxy());
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testStartShouldStartTheProxySuccessfully() throws Exception {
        // GIVEN
        int requestTimeout = 30000;
        int proxyPort = 9092;
        propertiesDTO = new ProxyPropertyDTO(proxyPort, requestTimeout, true, false);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
        // WHEN
        underTest.start();
        // THEN
        Mockito.verify(server).setPort(proxyPort);
        Mockito.verify(server).start(requestTimeout);
        Mockito.verify(server).addRequestInterceptor(loggingRequestInterceptor);
        Mockito.verify(server).addResponseInterceptor(loggingResponseInterceptor);
        Assert.assertTrue(ProxyServer.getResponseVolatile(), "Response volatility status was not set properly.");
    }

    @Test(expectedExceptions = ProxyCannotBeStartedException.class)
    public void testStartShouldThrowExceptionWhenTheProxyCannotBeStarted() throws Exception {
        // GIVEN
        int requestTimeout = 30000;
        propertiesDTO = new ProxyPropertyDTO(0, requestTimeout, false, false);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
        Mockito.doThrow(Exception.class).when(server).start(requestTimeout);
        // WHEN
        underTest.start();
        // THEN exception thrown
    }

    @Test
    public void testStopShouldLogErrorWhenWebAppCanNotBeStopped() throws Exception {
        //GIVEN
        willThrow(new Exception(EXCPEPTION_MESSAGE)).given(underTest).stopServer();
        //WHEN
        underTest.stop();
        //THEN
        Mockito.verify(logger).error(Mockito.eq("Proxy can not be stopped: " + EXCPEPTION_MESSAGE), Matchers.any(Exception.class));
    }

    @Test
    public void testStopShouldCallStopJettyServer() throws Exception {
        //GIVEN
        Mockito.doNothing().when(underTest).stopServer();
        //WHEN
        underTest.stop();
        //THEN
        Mockito.verify(underTest).stopServer();
    }

}
