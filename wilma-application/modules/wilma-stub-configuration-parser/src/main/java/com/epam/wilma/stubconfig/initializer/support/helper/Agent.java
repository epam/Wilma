package com.epam.wilma.stubconfig.initializer.support.helper;
/*==========================================================================
Copyright since 2020, EPAM Systems

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

import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * WARNING! When the jar is created, ensure that the MANIFEST has the proper info.
 * That will cause initialization of instrumentation of this class.
 * Example:
 * manifest.attributes( 'Launcher-Agent-Class': "com.epam.wilma.stubconfig.initializer.support.helper.Agent")
 * <p>
 * This code can be used with Jdk >=9.
 */
public class Agent {
    private static Instrumentation inst = null;

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        Agent.inst = inst;
    }

    public static void addClassPath(File file) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        try {
            if (!(cl instanceof URLClassLoader)) {
                inst.appendToSystemClassLoaderSearch(new JarFile(file));
            }
        } catch (Throwable e) {
            throw new DescriptorValidationFailedException("Cannot add jar '" + file.getName() + "' to classpath.");
        }
    }

}