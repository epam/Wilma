package com.epam.wilma.message.search.domain.exception;

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

/**
 * Thrown when a lucene search query cannot be parsed.
 * @author Tunde_Kovacs
 *
 */
public class QueryCannotBeParsedException extends SystemException {

    /**
     * Constructor that takes the exception message as input.
     * @param message of the exception
     */
    public QueryCannotBeParsedException(final String message) {
        super(message);
    }

    /**
     * Constructor that takes a message and wrapped or parent exception as input.
     * @param message of the exception
     * @param throwable is the parent or wrapped exception.
     */
    public QueryCannotBeParsedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
