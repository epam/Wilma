package com.epam.wilma.stubconfig.initializer.support.helper;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.exception.InterfaceValidationFailedException;

/**
 * Class used to validate other classes based on their interface.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ClassValidator {

    /**
     * If the imported class does not implement the required interface, throw wrapped {@link ClassCastException}.
     * @param externalClass is the imported class
     * @param interfaceToCast class of the desirable interface
     * @param classPath the path of the class
     * @param <T> the class type of the externalClass
     */
    public <T> void validateInterface(final T externalClass, final Class<T> interfaceToCast, final String classPath) {
        if (!interfaceToCast.isInstance(externalClass)) {
            throw new InterfaceValidationFailedException("Validation of stub descriptor failed - External class '" + classPath + "/"
                    + externalClass.getClass().getName() + "' has not implemented the required interface: " + interfaceToCast.getName(),
                    new ClassCastException());
        }
    }
}
