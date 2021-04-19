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
 * This exception class is used when a test case implementation error was detected by Gepard.
 * @author Tamas Kohegyi
 */
public class SimpleGepardException extends RuntimeException {

    /**
     * Constructs a new exception.
     * @param message is the error text.
     */
    public SimpleGepardException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception.
     * @param message is the error text.
     * @param cause is the root cause of this exception.
     */
    public SimpleGepardException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
