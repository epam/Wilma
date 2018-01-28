package com.epam.wilma.properties;
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

import com.epam.wilma.domain.exception.SystemException;

/**
 * It is thrown when a property loaded from the configuration while is invalid.
 * @author Tunde_Kovacs
 *
 */
public class InvalidPropertyException extends SystemException {

    /**
     * Constructs a new exception with a given <tt>message</tt>.
     * @param message cause of the error
     */
    public InvalidPropertyException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a given <tt>message</tt>.
     * @param message cause of the error
     * @param e causing error
     */
    public InvalidPropertyException(final String message, final Throwable e) {
        super(message, e);
    }

}
