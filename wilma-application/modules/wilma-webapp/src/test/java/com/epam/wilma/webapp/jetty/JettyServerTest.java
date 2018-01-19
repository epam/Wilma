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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.ServerProperties;

/**
 * Provides unit tests for the {@link JettyServer} class.
 * @author Tunde_Kovacs
 *
 */
public class JettyServerTest {

    private static final int RESPONSE_BUFFER_SIZE = 204800;
    private static final int REQUEST_BUFFER_SIZE = 81920;
    private static final String EXCPEPTION_MESSAGE = "excpeption message";
    private static final Integer PORT = 1234;
    @Mock
    private Server jettyServer;
    @Mock
    private HttpServlet httpServlet;
    @Mock
    private Logger logger;
    @Mock
    private HandlerList handlerList;
    @Mock
    private ServerFactory serverFactory;
    @Mock
    private WebAppConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;

    @InjectMocks
    private JettyServer underTest;

    @Mock
    private ServerProperties serverProperties;

    @BeforeMethod
    public void setUp() {
        underTest = spy(new JettyServer());
        MockitoAnnotations.initMocks(this);
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getServerProperties()).willReturn(serverProperties);
        given(serverProperties.getProxyPort()).willReturn(PORT);
        given(serverProperties.getRequestBufferSize()).willReturn(REQUEST_BUFFER_SIZE);
        given(serverProperties.getResponseBufferSize()).willReturn(RESPONSE_BUFFER_SIZE);
        //this is necessary because the start method of the jettyServer is final
        doNothing().when(underTest).startJettyServer();
        Whitebox.setInternalState(underTest, "handlerList", handlerList);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testStartShouldStartServer() {
        //GIVEN
        given(serverFactory.createServer(serverProperties)).willReturn(jettyServer);
        //WHEN
        underTest.start();
        //THEN
        verify(underTest).startJettyServer();
    }

    @Test
    public void testStartShouldCallSetHandlerOnServerWhenCreateServerWasSuccessfully() {
        //GIVEN
        given(serverFactory.createServer(serverProperties)).willReturn(jettyServer);
        //WHEN
        underTest.start();
        //THEN
        verify(jettyServer).setHandler((Handler) Mockito.anyObject());
    }

    @Test(expectedExceptions = SystemException.class)
    public void testStartWhenStartJettyServerThrowsExceptionShouldThrowSystemException() {
        //GIVEN
        String exceptionMessage = "exception";
        SystemException e = new SystemException("Something went wrong :(");
        willThrow(e).given(underTest).startJettyServer();
        given(serverFactory.createServer(serverProperties)).willReturn(jettyServer);
        //WHEN
        underTest.start();
        //THEN
        verify(logger).error(exceptionMessage, e);
    }

    @Test
    public void testStopShouldLogErrorWhenWebAppCanNotBeStopped() throws Exception {
        //GIVEN
        Exception e = new Exception(EXCPEPTION_MESSAGE);
        willThrow(e).given(underTest).stopJettyServer();
        //WHEN
        underTest.stop();
        //THEN
        verify(logger).error("Internal web application can not be stopped: " + EXCPEPTION_MESSAGE, e);
    }

    @Test
    public void testStopShouldCallStopJettyServer() throws Exception {
        //GIVEN
        doNothing().when(underTest).stopJettyServer();
        //WHEN
        underTest.stop();
        //THEN
        verify(underTest).stopJettyServer();
    }
}
