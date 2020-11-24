package com.epam.wilma.browserup.proxy;
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

import com.epam.wilma.proxy.configuration.ProxyConfigurationAccess;
import com.epam.wilma.proxy.configuration.domain.ProxyPropertyDTO;
import com.epam.wilma.browsermob.domain.exception.ProxyCannotBeStartedException;
import com.epam.wilma.browserup.interceptor.BrowserUpRequestInterceptor;
import com.epam.wilma.browserup.interceptor.BrowserUpResponseInterceptor;
import com.epam.wilma.browserup.proxy.helper.BrowserUpProxyServer;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

/**
 * Test class for BrowserUpProxy.
 *
 * @author Tamas_Kohegyi
 */
public class ProxyBrowserUpTest {

    private static final String EXCEPTION_MESSAGE = "exception message";
    private ProxyPropertyDTO propertiesDTO;

    @InjectMocks
    private ProxyBrowserUp underTest;

    @Mock
    private BrowserUpProxyServer server;
    @Mock
    private BrowserUpRequestInterceptor loggingRequestInterceptor;
    @Mock
    private BrowserUpResponseInterceptor loggingResponseInterceptor;
    @Mock
    private ProxyConfigurationAccess configurationAccess;
    @Mock
    private Logger logger;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new ProxyBrowserUp());
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
        Mockito.verify(server).start(proxyPort, requestTimeout);
        Mockito.verify(server).addRequestFilter(loggingRequestInterceptor);
        Mockito.verify(server).addResponseFilter(loggingResponseInterceptor);
    }

    @Test(expectedExceptions = ProxyCannotBeStartedException.class)
    public void testStartShouldThrowExceptionWhenTheProxyCannotBeStarted() throws Exception {
        // GIVEN
        int requestTimeout = 30000;
        int proxyPort = 9092;
        propertiesDTO = new ProxyPropertyDTO(proxyPort, requestTimeout, false, false);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
        Whitebox.setInternalState(underTest, "server", server);
        Mockito.doThrow(ProxyCannotBeStartedException.class).when(server).start(proxyPort, requestTimeout);
        // WHEN
        underTest.start();
        // THEN exception thrown
    }

    @Test
    public void testStopShouldLogErrorWhenWebAppCanNotBeStopped() throws Exception {
        //GIVEN
        willThrow(new Exception(EXCEPTION_MESSAGE)).given(underTest).stopServer();
        //WHEN
        underTest.stop();
        //THEN
        Mockito.verify(logger).error(Mockito.eq("Proxy can not be stopped: " + EXCEPTION_MESSAGE), Matchers.any(Exception.class));
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
