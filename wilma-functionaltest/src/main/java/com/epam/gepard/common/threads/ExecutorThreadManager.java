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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.common.Environment;
import com.epam.gepard.util.Util;

/**
 * Manages the executor threads.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class ExecutorThreadManager {
    private static final Logger CONSOLE_LOG = LoggerFactory.getLogger("console");
    private static final int MAX_EXEC_THREADS = 50;
    private final TestClassExecutionThread[] threads = new TestClassExecutionThread[MAX_EXEC_THREADS];

    private int threadCount = 1; //number of active test threads

    /**
     * Initializes and then starts executor threads.
     * @param threadCountText number of threads to use as a {@link String}
     * @param xmlResultPath the path to use for making xml reports
     */
    public void initiateAndStartExecutorThreads(final String threadCountText, final String xmlResultPath) {
        loadThreadCount(threadCountText);

        Util util = new Util();
        CONSOLE_LOG.info(util.getGepardVersion() + " uses " + threadCount + " thread(s).");

        //MULTI THREADED EXEC
        //init threads. Note: always the max number of threads are created, just some of them is enabled for TC execution.
        //the purpose is to keep the possibility of dynamically change the number of active threads during run-time (see remote control)
        for (int ti = 0; ti < MAX_EXEC_THREADS; ti++) {
            threads[ti] = new TestClassExecutionThread(xmlResultPath);
            threads[ti].setName("Exec" + ti); //set its name
            threads[ti].setEnabled(ti < threadCount); //enable tc execution for the selected threads
            threads[ti].start(); //start
        }
    }

    private void loadThreadCount(final String t) {
        if (t != null) {
            try {
                int thNo = Integer.parseInt(t);
                if (thNo > 0) {
                    threadCount = thNo;
                }
                //limit the thread no
                if (thNo > MAX_EXEC_THREADS) {
                    threadCount = MAX_EXEC_THREADS;
                }
            } catch (Exception e) { //this is not a problem, just means we use single thread approach, i.e. threads no = 1
                // default value of threadCount is 1, so in case of problem, it stays at 1
                CONSOLE_LOG.info("INFO: Cannot parse " + Environment.GEPARD_THREADS + " property:\" " + t + "\", using single thread.");
            }
        }
    }

    /**
     * Closes all running threads in the application except the main thread.
     */
    public void closeRunningThreads() {
        if (AllTestRunner.getGepardRemote() != null) {
            AllTestRunner.getGepardRemote().interrupt();
        }
        for (TestClassExecutionThread testClassExecutionThread : threads) {
            if (testClassExecutionThread != null) {
                testClassExecutionThread.setEnabled(false);
                testClassExecutionThread.interrupt();
            }
        }
    }

    public TestClassExecutionThread[] getThreads() {
        return threads;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
