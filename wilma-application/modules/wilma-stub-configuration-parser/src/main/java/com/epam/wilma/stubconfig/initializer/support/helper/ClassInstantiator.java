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

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

/**
 * Used to create instances from Class objects.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ClassInstantiator {

    /**
     * Creates an instance of the given class.
     * @param theClass the given class
     * @param <T> the type of the instance
     * @return a new instance of the given class
     * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class
     * @throws IllegalAccessException if the underlying constructor is inaccessible
     * @throws InvocationTargetException if the underlying constructor throws an exception
     */
    @SuppressWarnings("unchecked")
    public <T> T createClassInstanceOf(final Class<T> theClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return (T) theClass.getConstructors()[0].newInstance();
    }
}
