package com.epam.wilma.domain.exception;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
 * Thrown if there are too many incoming requests in one second.
 * @author Marton_Sereg
 *
 */
public class TooManyRequestsException extends ApplicationException {

    private final String messageTimeStamp;

    /**
     * Constructor with exception message and message time stamp.
     * @param message of the exception
     * @param timeStamp of the message
     */
    public TooManyRequestsException(final String message, final String timeStamp) {
        super(message);
        messageTimeStamp = timeStamp;
    }

    public String getMessageTimeStamp() {
        return messageTimeStamp;
    }
}
