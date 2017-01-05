package com.epam.wilma.webapp.domain.exception;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import com.epam.wilma.domain.exception.SystemException;

/**
 * It is thrown when a response template cannot be formatted.
 * @author Tunde_Kovacs
 *
 */
public class TemplateFormattingFailedException extends SystemException {

    /**
     * Exception constructor with a message.
     * @param message the message of the exception
     */
    public TemplateFormattingFailedException(final String message) {
        super(message);
    }

    /**
     * Exception constructor with a message and its cause.
     * @param message the message of the exception
     * @param throwable the cause of the exception
     */
    public TemplateFormattingFailedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
