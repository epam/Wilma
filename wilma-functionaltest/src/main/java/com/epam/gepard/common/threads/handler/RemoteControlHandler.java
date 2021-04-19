package com.epam.gepard.common.threads.handler;

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

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.common.threads.TestClassExecutionThread;

/**
 * Handler for remote control.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class RemoteControlHandler {
    /**
     * This function is planned to be implemented only. Would offer the possibility of stopping the "Gepard test server".
     * @return with server answer - not implemented.
     */
    public String remoteShutdown() {
        return "Not yet implemented, sorry.";
    }

    /**
     * This Gepard server method offers the possibility of monitoring the status of the executor threads.
     * @return with thread statuses.
     */
    public String remoteGetStatus() {
        String s = "";
        for (TestClassExecutionThread testClassExecutionThread : AllTestRunner.getExecutorThreadManager().getThreads()) {
            s = s + "Thread: " + testClassExecutionThread.getName() + ", ";
            s = testClassExecutionThread.isEnabled() ? s + "Enabled" : s + "Disabled";
            TestClassExecutionData o = testClassExecutionThread.getActiveTest();
            if (o != null) {
                s = s + ", Executing: " + o.getID();
            }
            s = s + "\n\r";
        }
        return s;
    }
}
