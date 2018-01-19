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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for {@link TestServerBootstrap}.
 * @author Marton_Sereg
 *
 */
public class TestServerBootstrapTest {

    @InjectMocks
    private TestServerBootstrap underTest;

    @Mock
    private Logger logger;

    @Mock
    private Properties properties;

    @Mock
    private SystemException systemException;

    @Mock
    private JettyServer jettyServer;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest.setLogger(logger);
    }

    @Test
    public final void testBootstrapShouldLogErrorWhenArgsDoesntHaveExactlyOneElement() {
        //GIVEN
        String[] args = new String[0];
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(logger).error(anyString());
    }

    @Test
    public final void testBootstrapShouldConfigureAndStartJettyServer() {
        //GIVEN
        String[] args = {"wilma.testserver.properties"};
        given(properties.getProperty("server.port.http")).willReturn("9090");
        given(properties.getProperty("server.port.https")).willReturn("8443");
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(jettyServer).configureAndStart(any(Server.class), eq(9090), eq(8443), eq(false));
    }

    @Test
    public final void testBootstrapShouldLogErrorWhenPropertyCannotBeFound() {
        //GIVEN
        String[] args = {"wilma.testservers.properties"};
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(logger).error(anyString());
    }

    @Test
    public final void testBootstrapShouldLogErrorWhenIntegerPropertyCannotBeConverted() {
        //GIVEN
        String[] args = {"wilma.testserver.properties"};
        given(properties.getProperty("server.port.http")).willReturn("9090s");
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(logger).error(anyString());
    }

    @Test
    public final void testBootstrapShouldLogErrorWhenPropertiesCannotBeLoaded() throws IOException {
        //GIVEN
        String[] args = {"wilma.testserver.properties"};
        willThrow(new IOException()).given(properties).load(any(InputStream.class));
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(logger).error(anyString());
    }

    @Test
    public final void testBootstrapShouldLogErrorWhenServerCannotBeStarted() {
        //GIVEN
        String[] args = {"wilma.testserver.properties"};
        given(properties.getProperty("server.port.http")).willReturn("9090");
        given(properties.getProperty("server.port.https")).willReturn("8443");
        willThrow(systemException).given(jettyServer).configureAndStart(any(Server.class), eq(9090), eq(8443), eq(false));
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(systemException).logStackTrace(logger);
    }

    @Test
    public final void testBootstrapShouldLogErrorWhenInvalidPropertyFile() {
        //GIVEN
        String[] args = {"wilma.testserver.txt"};
        //WHEN
        underTest.bootstrap(args, properties, jettyServer);
        //THEN
        verify(logger).error("Specified property file's extension is not \"properties\"!");
    }
}
