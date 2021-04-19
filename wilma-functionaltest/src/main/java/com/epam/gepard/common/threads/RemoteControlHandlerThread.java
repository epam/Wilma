package com.epam.gepard.common.threads;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.GepardConstants;
import com.epam.gepard.common.threads.handler.KillCommandHandler;
import com.epam.gepard.common.threads.handler.RemoteControlHandler;
import com.epam.gepard.common.threads.helper.ServerSocketFactory;
import com.epam.gepard.util.Util;

/**
 * This thread takes care about the remote control of Gepard.
 * This means meanwhile Gepard is running, it can open a port
 * where you may connect to (like a telnet connection), and Gepard can receive commands.
 * Right now load related information is available, like number of active threads,
 * which testcase is actually executed by which thread,
 * it is possible to reduce/increase the available test execution threads.
 *
 * Later it may happen that Gepard can receive new test case execution requests too,
 * and other interesting features could be implemented, but right now such things are not available.
 */
public class RemoteControlHandlerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteControlHandlerThread.class);
    private ServerSocket gepardServerSocket;
    private Socket clientSocket;
    private PrintStream os;
    private RemoteControlHandler handler;
    private boolean isInitialized;
    private ServerSocketFactory gepardServerSocketFactory;
    private KillCommandHandler killCommandHandler;
    private boolean running;
    private long sleepTime = GepardConstants.ONE_SECOND_LENGTH.getConstant();
    private final int gepardPort;

    /**
     * Constructor of the thread.
     * @param handler should be the AllTestRunner (main Gepard) class.
     * @param serverSocketFactory the factory for creating the {@link ServerSocket}
     * @param gepardPort the port the server socket will be bound to
     */
    public RemoteControlHandlerThread(final RemoteControlHandler handler, final ServerSocketFactory serverSocketFactory, final int gepardPort) {
        super();
        this.handler = handler;
        this.gepardServerSocketFactory = serverSocketFactory;
        this.killCommandHandler = new KillCommandHandler();
        this.gepardPort = gepardPort;
    }

    @Override
    public void run() {
        Util util = new Util();
        String me = this.getName();
        isInitialized = tryInitiateService();
        running = true;
        if (isInitialized) {
            //forever loop we have
            while (running) {
                receiveData(util);
                //finally, wait for a sec before the next connection
                try {
                    sleep(sleepTime); //sleep for a sec by default, then restart the loop
                } catch (InterruptedException e) {
                    //this was not expected, but if happens, then time to exit
                    LOGGER.debug("Thread: " + me + " is exiting, as got InterruptedException!");
                    return;
                }
            }
        }
    }

    public void setSleepTime(final long sleepTime) {
        this.sleepTime = sleepTime;
    }

    private boolean tryInitiateService() {
        boolean result = false;
        try {
            gepardServerSocket = gepardServerSocketFactory.create(gepardPort);
            LOGGER.debug("Gepard Remote Control Service is started on port: " + gepardPort);
            result = true;
        } catch (IOException e) {
            LOGGER.debug("Problem opening Gepard Service port: " + e.getMessage(), e);
        }
        return result;
    }

    private void receiveData(final Util util) {
        String line;
        BufferedReader is;
        try {
            clientSocket = gepardServerSocket.accept();
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
            // As long as we receive data, echo that data back to the client.
            boolean stillAlive = true;
            os.println("Hello, you are connected to " + util.getGepardVersion());
            while (stillAlive) {
                line = is.readLine();
                stillAlive = evaluateCommand(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
        }
    }

    private boolean evaluateCommand(final String command) throws IOException {
        boolean keepAlive = true;
        if ("shutdown".equals(command)) {
            //graceful shutdown is requested
            String s = handler.remoteShutdown();
            os.println(s);
        }
        if ("status".equals(command)) {
            //get full status
            String s = handler.remoteGetStatus();
            os.println(s);
        }
        if ("exit".equals(command)) {
            //close connection is requested
            clientSocket.close();
            clientSocket = null; //clean it up
            keepAlive = false;
        }
        if ("kill".equals(command)) {
            //immediate exit is requested
            running = false;
            keepAlive = false;
            os.println("Gepard is exiting (brute force)...");
            clientSocket.close();
            gepardServerSocket.close();
            killCommandHandler.handleCommand();
        }
        os.println(command + " ?");
        return keepAlive;
    }

}
