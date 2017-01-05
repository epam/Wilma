package com.epam.wilma.message.search.engine.properties.helper;
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

import com.epam.wilma.message.search.domain.exception.SystemException;

/**
 * Thrown when the configuration properties cannot be loaded.
 * @author Tunde_Kovacs
 *
 */
public class PropertiesNotAvailableException extends SystemException {

    /**
     * Constructor with a cause.
     * @param message the message of the exception
     * @param throwable the cause of the exception
     */
    public PropertiesNotAvailableException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructor with a message only.
     * @param message the message of the exception
     */
    public PropertiesNotAvailableException(final String message) {
        super(message);
    }

}
