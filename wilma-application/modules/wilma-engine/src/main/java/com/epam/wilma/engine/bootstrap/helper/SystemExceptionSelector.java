package com.epam.wilma.engine.bootstrap.helper;
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

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.NestedRuntimeException;

import com.epam.wilma.domain.exception.SystemException;

/**
 * Class for selecting {@link SystemException} from exception causes to be able to get a user-readable exception message.
 * @author Tamas_Bihari
 *
 */
public class SystemExceptionSelector {

    /**
     * Selecting {@link SystemException} from {@link BeanCreationException} causes if exists to be able to return with user-readable exception message.
     * @param e is the catched exception
     * @return with {@link SystemException} if causes contains {@link SystemException} else null
     */
    public SystemException getSystemException(final Exception e) {
        SystemException result = null;
        if (e instanceof BeanCreationException) {
            if (((NestedRuntimeException) e).getMostSpecificCause() != null) {
                Throwable ex = e;
                boolean found = false;
                while (ex.getCause() != null && !found) {
                    if (ex.getCause() instanceof SystemException) {
                        found = true;
                        result = (SystemException) ex.getCause();
                    }
                    ex = ex.getCause();
                }
            }
        }
        return result;
    }
}
