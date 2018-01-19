package com.epam.wilma.test.client;
/*==========================================================================
Copyright since 2013, EPAM Systems

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

import org.slf4j.Logger;

/**
 * This exception acts as a base class for all types of system exceptions, that can occur when somthing not related to the business logic fails.
 * @author Marton_Sereg
 *
 */
public class SystemException extends RuntimeException {

    /**
     * Creates a new SystemException.
     * @param message Message of the exception.
     * @param cause Cause of the exception.
     */
    public SystemException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Logs the stack trace of the exception.
     * @param logger Logger that is used to log the exception
     */
    public void logStackTrace(final Logger logger) {
        logger.error(getMessage());
        logger.error("Caused by: " + getCause().getClass() + " " + getCause().getMessage());
        String stackTrace = "";
        for (StackTraceElement element : getStackTrace()) {
            stackTrace += "\n\t" + element.toString();
        }
        logger.debug(stackTrace);
    }
}
