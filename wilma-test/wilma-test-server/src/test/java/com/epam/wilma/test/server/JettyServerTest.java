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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for {@link JettyServer}.
 * @author Marton_Sereg
 *
 */
public class JettyServerTest {

    private JettyServer underTest;

    @Mock
    private Server server;

    @Mock
    private SelectChannelConnector selectChannelConnector;

    @Mock
    private SslSelectChannelConnector sslSelectChannelConnector;

    @Mock
    private SslContextFactory sslContextFactory;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new JettyServer();
        underTest = Mockito.spy(underTest);
    }

    @Test
    public final void testConfigureAndstartShouldCreateConnectors() throws Exception {
        // GIVEN
        given(underTest.createSelectChannelConnector()).willReturn(selectChannelConnector);
        given(underTest.createSslSelectChannelConnector()).willReturn(sslSelectChannelConnector);
        given(sslSelectChannelConnector.getSslContextFactory()).willReturn(sslContextFactory);
        doNothing().when(underTest).startServer(server);
        // WHEN
        underTest.configureAndStart(server, 9090, 8443, false);
        // THEN
        verify(selectChannelConnector).setPort(9090);
        verify(sslSelectChannelConnector).setPort(8443);
        verify(sslContextFactory).setKeyStorePath("certificate/wilmaKeystore.jks");
        verify(sslContextFactory).setKeyStorePassword("secret123");
        verify(sslContextFactory).setKeyManagerPassword("secret123");
        verify(server).setHandler(Matchers.any(ExampleHandler.class));
    }

    @Test
    public final void testConfigureAndstartShouldStartServer() throws Exception {
        // GIVEN
        given(underTest.createSelectChannelConnector()).willReturn(selectChannelConnector);
        given(underTest.createSslSelectChannelConnector()).willReturn(sslSelectChannelConnector);
        given(sslSelectChannelConnector.getSslContextFactory()).willReturn(sslContextFactory);
        doNothing().when(underTest).startServer(server);
        // WHEN
        underTest.configureAndStart(server, 9090, 8443, false);
        // THEN
        verify(underTest).startServer(server);
        verify(server).join();
    }

    @Test
    public final void testConfigureAndstartShouldSetHandlerWhenPerfTestIsTrue() throws Exception {
        // GIVEN
        given(underTest.createSelectChannelConnector()).willReturn(selectChannelConnector);
        given(underTest.createSslSelectChannelConnector()).willReturn(sslSelectChannelConnector);
        given(sslSelectChannelConnector.getSslContextFactory()).willReturn(sslContextFactory);
        doNothing().when(underTest).startServer(server);
        // WHEN
        underTest.configureAndStart(server, 9090, 8443, true);
        // THEN
        verify(server).setHandler(Matchers.any(PerformanceTestHandler.class));
    }

    @Test(expectedExceptions = SystemException.class)
    public final void testConfigureAndstartShouldThrowExceptionWhenStartingTheServerFails() throws Exception {
        // GIVEN
        given(underTest.createSelectChannelConnector()).willReturn(selectChannelConnector);
        given(underTest.createSslSelectChannelConnector()).willReturn(sslSelectChannelConnector);
        given(sslSelectChannelConnector.getSslContextFactory()).willReturn(sslContextFactory);
        willThrow(new Exception()).given(underTest).startServer(server);
        // WHEN
        underTest.configureAndStart(server, 9090, 8443, false);
        // THEN exception is thrown
    }
}
