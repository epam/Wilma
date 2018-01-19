package com.epam.wilma.stubconfig.initializer;

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

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Abstract class serving as the base for the various external class initializers.
 * @param <T> the class to be initialized
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public abstract class CommonClassInitializer<T> implements ApplicationContextAware {

    private static final String BEAN_NOT_FOUND_MESSAGE = "Bean not found, class name: ";
    private final Logger logger = LoggerFactory.getLogger(CommonClassInitializer.class);
    private ApplicationContext applicationContext;
    @Autowired
    private ExternalInitializer externalInitializer;

    /**
     * Returns the path to the folder that contains the external classes.
     * @return the path as a String
     */
    protected abstract String getPathOfExternalClasses();

    /**
     * Returns the list of already initialized external class objects.
     * @return the list
     */
    protected abstract List<T> getExternalClassObjects();

    /**
     * Adds the external class object to list of already initialized ones.
     * @param externalClassObject the class instance that got initialized
     */
    protected abstract void addExternalClassObject(T externalClassObject);

    /**
     * Returns the Class of the to be initialized external class.
     * @return the Class
     */
    protected abstract Class<T> getExternalClassType();

    /**
     * Tries to get the class with the given name, if not found throws a DescriptorValidationFailedException.
     * @param className the name of the external class
     * @return an instance of the class
     */
    public T getExternalClassObject(final String className) {
        T result = getClassFromContext(className);
        if (result == null) {
            result = getClassFromPreviouslyImportedClasses(className);
        }
        if (result == null) {
            result = getClassFromExternalResources(className);
        }
        return result;
    }

    private T getClassFromPreviouslyImportedClasses(final String className) {
        T result = null;
        List<T> previouslyImportedClassObjects = getExternalClassObjects();
        if (previouslyImportedClassObjects != null) {
            Iterator<T> iterator = previouslyImportedClassObjects.iterator();
            while (iterator.hasNext() && result == null) {
                T next = iterator.next();
                if (next.getClass().getSimpleName().equalsIgnoreCase(className)) {
                    result = next;
                }
            }
        }
        return result;
    }

    private T getClassFromExternalResources(final String className) {
        T result = null;
        String pathToClassFolder = getPathOfExternalClasses();
        if (pathToClassFolder != null) {
            result = externalInitializer.loadExternalClass(className, pathToClassFolder, getExternalClassType());
            addExternalClassObject(result);
        }
        return result;
    }

    private T getClassFromContext(final String className) {
        T result;
        try {
            result = applicationContext.getBean(className, getExternalClassType());
        } catch (BeansException e) {
            logger.debug(BEAN_NOT_FOUND_MESSAGE + className, e);
            result = null;
        }
        return result;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
