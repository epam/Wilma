package com.epam.wilma.domain.stubconfig.exception;
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
 * Runtime exception for stub configuration errors.
 * @author Tamas_Bihari
 *
 */
public class StubConfigurationException extends SystemException {

    /**
     * Create {@link StubConfigurationException} instance with message and throwable cause.
     * @param message is the exception message
     * @param throwable is the cause
     */
    public StubConfigurationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
