package com.epam.wilma.stubconfig.initializer.support.helper;

/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Used for finding classes of a certain type and package.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class PackageBasedClassFinder {

    private static final String MULTIPLE_CLASSES_FOUND_TEMPLATE = "Warning! Multiple classes found '%s' in package '%s' as subtype of '%s'.";
    private static final String CLASS_NOT_FOUND_TEMPLATE = "Could not find '%s' in package: '%s'";
    private final Logger logger = LoggerFactory.getLogger(PackageBasedClassFinder.class);

    @Autowired
    private PackageBasedClassFinderCore packageBasedClassFinderCore;

    /**
     * Finds the first subclass of the given type and package.
     * Logs a message if multiple were found.
     * @param interfaceOrClass the given type
     * @param packageName the given package
     * @param <T> the type of the {@link Class}
     * @return the first subtype of the given type that was found
     * @throws ClassNotFoundException  if it doesn't find any classes
     */
    public <T> Class<? extends T> findFirstOf(final Class<T> interfaceOrClass, final String packageName) throws ClassNotFoundException {
        Class<? extends T> result = null;
        Set<Class<? extends T>> classesThatImplementTheInterface = packageBasedClassFinderCore.find(packageName, interfaceOrClass);
        Iterator<Class<? extends T>> iterator = classesThatImplementTheInterface.iterator();
        if (iterator.hasNext()) {
            result = getResult(iterator);
        } else {
            throw new ClassNotFoundException(String.format(CLASS_NOT_FOUND_TEMPLATE, interfaceOrClass.getName(), packageName));
        }
        warnOfMultipleClasses(classesThatImplementTheInterface, packageName, interfaceOrClass);
        return result;

    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> getResult(final Iterator<Class<? extends T>> iterator) {
        return iterator.next();
    }

    private <T> void warnOfMultipleClasses(final Set<Class<? extends T>> setOfClasses, final String packageName, final Class<T> interfaceOrClass) {
        if (setOfClasses.size() > 1) {
            logger.info(String.format(MULTIPLE_CLASSES_FOUND_TEMPLATE, setOfClasses, packageName, interfaceOrClass));
        }
    }
}
