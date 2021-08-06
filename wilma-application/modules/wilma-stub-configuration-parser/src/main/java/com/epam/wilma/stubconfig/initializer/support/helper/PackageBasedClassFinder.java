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

import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Used for finding classes of a certain type and package.
 *
 * @author Adam_Csaba_Kiraly
 */
@Component
public class PackageBasedClassFinder {

    private static final String MULTIPLE_CLASSES_FOUND_TEMPLATE = "Warning! Multiple classes found '%s' in package '%s' as subtype of '%s'.";
    private static final String CLASS_NOT_FOUND_TEMPLATE = "Could not find '%s' in package: '%s'";
    private final Logger logger = LoggerFactory.getLogger(PackageBasedClassFinder.class);

    private <T> void warnOfMultipleClasses(final Set<Class<? extends T>> setOfClasses, final String packageName, final Class<T> interfaceOrClass) {
        if (setOfClasses.size() > 1) {
            logger.info(String.format(MULTIPLE_CLASSES_FOUND_TEMPLATE, setOfClasses, packageName, interfaceOrClass));
        }
    }

    private List<File> collectJarFiles(String jarFolderPath) {
        File folder = new File(jarFolderPath);
        File[] listOfFiles = folder.listFiles();
        List<File> jarFileList = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            File actualFile = listOfFiles[i];
            if (actualFile.isFile() && actualFile.getName().toLowerCase().endsWith(".jar")) {
                jarFileList.add(actualFile);
            }
        }
        return jarFileList;
    }

    /**
     * Finds the first subclass of the given type and package.
     * Logs a message if multiple were found.
     *
     * @param jarFolderPath   the resource path of the jar files
     * @param interfaceToCast the given type
     * @param packageName     the given package
     * @param <T>             the type of the {@link Class}
     * @return the first subtype of the given type that was found
     * @throws ClassNotFoundException if it doesn't find any classes
     */
    public <T> Class findClassInJar(String jarFolderPath, Class<T> interfaceToCast, String packageName) throws MalformedURLException {
        List<File> potentialJars = collectJarFiles(jarFolderPath);
        for (File file : potentialJars) {
            List<Class> classes = getClassesFromJarFile(file);
            for (Class clazz : classes) {
                if (clazz.getPackage().getName().startsWith(packageName)
                        && interfaceToCast.isAssignableFrom(clazz)) {
                    return clazz;
                }
            }
        }
        throw new DescriptorValidationFailedException("Cannot find any suitable class.");
    }

    private List<Class> getClassesFromJarFile(File path) {
        List<Class> classes = new ArrayList<>();
        if (path.canRead()) {
            try (JarFile jar = new JarFile(path)) {
                Enumeration<JarEntry> en = jar.entries();
                while (en.hasMoreElements()) {
                    JarEntry entry = en.nextElement();
                    if (!entry.getName().contains("..") && entry.getName().endsWith("class")) {
                        String className = fromFileToClassName(entry.getName());
                        Class clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new SystemException("Failed to read classes from jar file: " + path, e);
            }
        }
        return classes;
    }

    private String fromFileToClassName(final String fileName) {
        return fileName.substring(0, fileName.length() - 6).replaceAll("/|\\\\", "\\.");
    }

}
