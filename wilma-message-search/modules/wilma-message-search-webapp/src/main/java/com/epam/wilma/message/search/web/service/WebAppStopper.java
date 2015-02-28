package com.epam.wilma.message.search.web.service;

/*==========================================================================
 Copyright 2015 EPAM Systems

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

import java.util.concurrent.ExecutorService;

import com.epam.wilma.message.search.web.WebAppServer;

/**
 * Service used for stopping the server.
 * @author Adam_Csaba_Kiraly
 */
public class WebAppStopper {
    private final ShutdownTask task = new ShutdownTask();
    private final ExecutorService executorService;
    private final WebAppServer webAppServer;

    /**
     * Constructs a new instance of {@link WebAppStopper}.
     * @param webAppServer the server to stop
     * @param executorService the executorService to use for running the task
     */
    public WebAppStopper(final WebAppServer webAppServer, final ExecutorService executorService) {
        this.webAppServer = webAppServer;
        this.executorService = executorService;
    }

    /**
     * Stops the server asynchronously.
     */
    public void stopAsync() {
        executorService.execute(task);
        executorService.shutdown();
    }

    private class ShutdownTask implements Runnable {
        @Override
        public void run() {
            webAppServer.stop();
        }
    }
}
