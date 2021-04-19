package com.epam.gepard.datadriven.feeders;
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

import com.epam.gepard.datadriven.DataFeederLoader;

/**
 * This exception class is used when the test is Not Applicable (N/A).
 */
public class DataFeederException extends Exception {

    private final int errorCode;

    /**
     * Default constructor set, this calls ReportError at DataFeederLoader.
     *
     * @param errorMessage that should be used as info about the exception.
     */
    public DataFeederException(String errorMessage) {
        super(errorMessage);
        DataFeederLoader.reportError(errorMessage);
        this.errorCode = 0;
    }

    /**
     * Default constructor set, this calls ReportError at DataFeederLoader, and holds the error code too.
     *
     * @param errorMessage that should be used as info about the exception.
     * @param errorCode is the return value that gepard can use.
     */
    public DataFeederException(String errorMessage, int errorCode) {
        super(errorMessage);
        DataFeederLoader.reportError(errorMessage);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }

}
