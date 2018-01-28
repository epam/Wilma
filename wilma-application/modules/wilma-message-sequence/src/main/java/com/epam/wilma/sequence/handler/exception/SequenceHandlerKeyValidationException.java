package com.epam.wilma.sequence.handler.exception;
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
 * This exception is a specific exception and it's raised when the generated new key of SequenceHandler contains at least one forbidden character.
 * @author Tibor_Kovacs
 *
 */
public class SequenceHandlerKeyValidationException extends SystemException {

    /**
     * Constructor that takes the exception message as input.
     * @param message of the exception
     */
    public SequenceHandlerKeyValidationException(final String message) {
        super(message);
    }

}
