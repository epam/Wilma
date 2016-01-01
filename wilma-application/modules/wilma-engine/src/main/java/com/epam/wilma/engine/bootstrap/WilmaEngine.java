package com.epam.wilma.engine.bootstrap;

/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.browsermob.Proxy;
import com.epam.wilma.common.helper.WilmaService;
import com.epam.wilma.engine.bootstrap.helper.ApplicationContextCloser;
import com.epam.wilma.maintainer.LogFileMaintainer;
import com.epam.wilma.safeguard.monitor.JmsQueueMonitor;
import com.epam.wilma.sequence.maintainer.SequenceMaintainer;
import com.epam.wilma.webapp.jetty.JettyServer;
import com.google.common.util.concurrent.AbstractIdleService;

/**
 * Starts the proxy, and catches every Application specific exception.
 * @author Marton_Sereg
 */
@Component
public class WilmaEngine extends AbstractIdleService implements WilmaService {

    @Autowired
    private JettyServer webAppServer;
    @Autowired
    private Proxy proxy;
    @Autowired
    private LogFileMaintainer logFileMaintainer;
    @Autowired
    private JmsQueueMonitor jmsQueueMonitor;
    @Autowired
    private ApplicationContextCloser applicationCloser;
    @Autowired
    private SequenceMaintainer sequenceMaintainer;

    @Override
    protected void startUp() throws Exception {
        webAppServer.start();
        proxy.start();
        logFileMaintainer.startScheduling();
        jmsQueueMonitor.startScheduling();
        sequenceMaintainer.startScheduling();
    }

    @Override
    protected void shutDown() throws Exception {
        stopEverything();
    }

    @Override
    public void stop() {
        stopAsync();
    }

    @Override
    public void start() {
        try {
            startAsync().awaitRunning();
        } catch (IllegalStateException e) {  //NOSONAR - we don't need to log this exception
            stopEverything();
        }
    }

    private void stopEverything() {
        webAppServer.stop();
        proxy.stop();
        applicationCloser.closeTriggers();
    }
}
