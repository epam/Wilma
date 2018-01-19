package com.epam.wilma.stubconfig.initializer.condition.helper;
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

/**
 * Factory for creating new instances of {@link Class}.
 *
 * @author Tunde_Kovacs
 */
@Component
public class ClassFactory {

    /**
     * Constructs new instances of {@link Class}.
     *
     * @param className the name of the class that should be created
     * @param <T> is the type of the class
     * @return the new class
     * @throws ClassNotFoundException if the class cannot be located
     */
    public <T> Class<T> getClassToLoad(final String className) throws ClassNotFoundException {
        Class<T> classToLoad;
        String qualifiedClassName = className.replace("/", ".").split(".class")[0];
        classToLoad = getClass(qualifiedClassName);
        return classToLoad;
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getClass(final String className) throws ClassNotFoundException {
        Class<T> classToLoad;
        classToLoad = (Class<T>) Class.forName(className);
        return classToLoad;
    }
}
