package com.epam.gepard.logger;

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

import java.util.Properties;

import com.epam.gepard.util.Util;

/**
 * Finalizes the different log files to be closed gracefully.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class LogFinalizer {

    /**
     * Inserts a footer block to every given log file with the given informations.
     * @param props is the holder of the necessary informations.
     * @param htmlLog is writer of html log file.
     * @param csvLog is writer of csv log file.
     * @param quickLog is writer of plain log file.
     * @param threadCount is the count of used threads.
     */
    public void finalizeLogs(final Properties props, final LogFileWriter htmlLog, final LogFileWriter csvLog, final LogFileWriter quickLog,
            final int threadCount) {
        Util util = new Util();
        props.setProperty("GEPARDVERSION", util.getGepardVersion() + " with " + threadCount + " thread(s).");
        //finalizing logs
        htmlLog.insertBlock("Footer", props);
        htmlLog.close();
        csvLog.insertBlock("Footer", props);
        csvLog.close();
        quickLog.insertBlock("Footer", props);
        quickLog.close();
    }

}
