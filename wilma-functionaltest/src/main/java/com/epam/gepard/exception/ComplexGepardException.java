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
 * This exception class is used when application has to shut down.
 * @author Tibor_Kovacs
 */
public class ComplexGepardException extends RuntimeException {

    private final int exitCode;
    private final boolean shouldExit;

    /**
     * Constructs a new exception with some necessary information.
     * @param introText is a information text about the failure.
     * @param cause is the cause.
     * @param shouldExit indicates whether Gepard will shut down or not.
     * @param exitCode that should be used as info about the cause of shutting down.
     */
    public ComplexGepardException(final String introText, final Throwable cause, final boolean shouldExit, final int exitCode) {
        super(introText, cause);
        this.exitCode = exitCode;
        this.shouldExit = shouldExit;
    }

    public int getExitCode() {
        return exitCode;
    }

    public boolean isShouldExit() {
        return shouldExit;
    }
}
