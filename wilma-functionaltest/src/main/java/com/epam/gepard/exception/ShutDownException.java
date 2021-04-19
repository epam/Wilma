package com.epam.gepard.exception;

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
/**
 * This exception class is used when the application must to shut down.
 * @author Tibor_Kovacs
 */
public class ShutDownException extends RuntimeException {

    private final int exitCode;

    /**
     * Constructs a new exception with a given exit code.
     * @param exitCode is code of exiting.
     */
    public ShutDownException(final int exitCode) {
        super();
        this.exitCode = exitCode;
    }

    /**
     * To get the exit code.
     * @return with exit code.
     */
    public int getExitCode() {
        return exitCode;
    }
}
