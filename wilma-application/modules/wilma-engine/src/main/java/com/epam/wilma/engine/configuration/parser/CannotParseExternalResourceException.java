package com.epam.wilma.engine.configuration.parser;
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
 * Exception to throw when an external resource cannot be parsed.
 * @author Adam_Csaba_Kiraly
 *
 */
public class CannotParseExternalResourceException extends SystemException {

    /**
     * Exception constructor with custom message and original exception as parameters.
     * @param message our custom message
     * @param throwable the original exception
     */
    public CannotParseExternalResourceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
