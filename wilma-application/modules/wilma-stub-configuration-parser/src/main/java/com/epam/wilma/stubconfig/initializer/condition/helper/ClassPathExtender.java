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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.stereotype.Component;

/**
 * Adds a file to the system classpath.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ClassPathExtender {

    @SuppressWarnings("rawtypes")
    private Class[] parameters;

    /**
     * Adds a file to the system classpath.
     * @param folder the folder that contains the class file
     */
    public void addFile(final String folder) {
        parameters = new Class[]{URL.class};
        File f = new File(folder);
        try {
            addFile(f);
        } catch (IOException e) {
            throw new com.epam.wilma.domain.exception.SystemException("could not put class file to classpath", e);
        }
    }

    private void addFile(final File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    private void addURL(final URL u) throws IOException {
        URLClassLoader systemLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(systemLoader, new Object[]{u});
        } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException t) {
            throw new IOException("Error, could not add URL to system classloader", t);
        }

    }

}
