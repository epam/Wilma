package com.epam.wilma.mitmProxy.proxy;
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

import com.epam.mitm.proxy.ProxyServer;
import com.epam.wilma.mitmProxy.interceptor.MitmProxyRequestInterceptor;
import com.epam.wilma.mitmProxy.interceptor.MitmProxyResponseInterceptor;
import com.epam.wilma.proxy.configuration.ProxyConfigurationAccess;
import com.epam.wilma.proxy.configuration.domain.ProxyPropertyDTO;
import com.epam.wilma.proxy.exception.ProxyCannotBeStartedException;
import net.lightbody.bmp.proxy.jetty.http.SocketListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;

import java.nio.file.Path;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

/**
 * Test class for MitmProxy.
 *
 * @author Marton_Sereg, Tamas_Kohegyi
 */
public class ProxyIntegrationTest {

    private static final String EXCPEPTION_MESSAGE = "excpeption message";
    private ProxyPropertyDTO propertiesDTO;

    @InjectMocks
    private MitmProxy underTest;

    @Mock
    private ProxyServer server;
    @Mock
    private MitmProxyRequestInterceptor loggingRequestInterceptor;
    @Mock
    private MitmProxyResponseInterceptor loggingResponseInterceptor;
    @Mock
    private ProxyConfigurationAccess configurationAccess;
    @Mock
    private SocketListener listener;
    @Mock
    private Path path;
    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        underTest = Mockito.spy(new MitmProxy());
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
        Assert.assertTrue("Response volatility status was not set properly.", ProxyServer.getResponseVolatile());
    }

    @Test(expected = ProxyCannotBeStartedException.class)
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
