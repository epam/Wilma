package com.epam.wilma.domain.exception;
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

/**
 * Thrown when scheduling cannot be started.
 * @author Marton_Sereg
 *
 */
public class SchedulingCannotBeStartedException extends SystemException {

    /**
     * Constructor with a message.
     * @param message of the exception
     */
    public SchedulingCannotBeStartedException(final String message) {
        super(message);
    }

    /**
     * Constructor with a message and a cause.
     * @param message of the exception
     * @param throwable cause of the exception
     */
    public SchedulingCannotBeStartedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
