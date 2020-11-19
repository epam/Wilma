package com.epam.wilma.engine.bootstrap;

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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.proxy.Proxy;
import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.engine.bootstrap.helper.ApplicationContextCloser;
import com.epam.wilma.maintainer.LogFileMaintainer;
import com.epam.wilma.safeguard.monitor.JmsQueueMonitor;
import com.epam.wilma.sequence.maintainer.SequenceMaintainer;
import com.epam.wilma.webapp.jetty.JettyServer;

/**
 * Provides unit tests for the class {@link WilmaEngine}.
 */
public class WilmaEngineTest {

    private static final String ERR_MSG = "System error";

    @Mock
    private JettyServer webAppServer;
    @Mock
    private Proxy proxy;
    @Mock
    private LogFileMaintainer logFileMaintainer;
    @Mock
    private ApplicationContextCloser applicationCloser;
    @Mock
    private JmsQueueMonitor jmsQueueMonitor;
    @Mock
    private SequenceMaintainer sequenceMaintainer;

    @InjectMocks
    private WilmaEngine underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new WilmaEngine();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartShouldStartProxy() {
        //GIVEN in setup
        //WHEN
        underTest.start();
        //THEN
        verify(proxy).start();
    }

    @Test
    public void testStartShouldWebAppServer() {
        //GIVEN in setup
        //WHEN
        underTest.start();
        //THEN
        verify(webAppServer).start();
    }

    @Test
    public void testStartShouldStartLogFileMaintenance() {
        //GIVEN in setup
        //WHEN
        underTest.start();
        //THEN
        verify(logFileMaintainer).startScheduling();
    }

    @Test
    public void testStartShouldStartJmsQueueMonitor() {
        //GIVEN in setup
        //WHEN
        underTest.start();
        //THEN
        verify(jmsQueueMonitor).startScheduling();
    }

    @Test
    public void testStartShouldStartSequenceCleanMaintainer() {
        //GIVEN in setup
        //WHEN
        underTest.start();
        //THEN
        verify(sequenceMaintainer).startScheduling();
    }

    @Test
    public void testStartWhenSystemExceptionShouldStopServers() {
        //GIVEN
        SystemException e = new SystemException(ERR_MSG);
        doThrow(e).when(proxy).start();
        //WHEN
        underTest.start();
        //THEN
        verify(webAppServer).stop();
        verify(proxy).stop();
        verify(applicationCloser).closeTriggers();
    }

    @Test
    public void testShutdownShouldStopEverything() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.shutDown();
        //THEN
        verify(webAppServer).stop();
        verify(proxy).stop();
        verify(applicationCloser).closeTriggers();
    }

}
