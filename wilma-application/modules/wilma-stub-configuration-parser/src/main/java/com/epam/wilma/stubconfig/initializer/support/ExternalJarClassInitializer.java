package com.epam.wilma.stubconfig.initializer.support;

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

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.helper.Agent;
import com.epam.wilma.stubconfig.initializer.support.helper.BeanRegistryService;
import com.epam.wilma.stubconfig.initializer.support.helper.PackageBasedClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;

/**
 * Initializes classes from jars.
 *
 * @author Adam_Csaba_Kiraly
 */
@Component
public class ExternalJarClassInitializer {

    private static final String GOT_BEAN_TEMPLATE = "Got bean of name: '{}' , type: '{}' and interface: {}";
    private static final String JAR_INITIALIZATION_TEMPLATE = "JAR - Initialized class '{}' of '{}' interface, using folder path '{}'.";

    private final Logger logger = LoggerFactory.getLogger(ExternalJarClassInitializer.class);

    @Autowired
    private PackageBasedClassFinder packageBasedClassFinder;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private BeanRegistryService beanRegistryService;
    @Autowired
    private ExternalClassInitializer externalClassInitializer;

    /**
     * Imports external class from the file system and adds it to the class path.
     *
     * @param <T>             is the type of the return object
     * @param packageName     is the package name of the class to be loaded
     * @param jarFolderPath   is the path where jars are placed when uploaded
     * @param interfaceToCast class of the desirable interface
     * @return with the new instance of the class
     * @throws DescriptorValidationFailedException if the class does not exist or not valid.
     */
    public <T> T loadExternalClass(final String packageName, final String jarFolderPath, final Class<T> interfaceToCast) {
        String beanName = createBeanName(packageName, interfaceToCast);
        T result;
        try {
            result = beanRegistryService.getBean(beanName, interfaceToCast);
            logger.info(GOT_BEAN_TEMPLATE, beanName, result.getClass().getName(), interfaceToCast);
        } catch (BeansException e) {
            logger.debug(String.format("Finding bean with name '%s' of type '%s' failed", beanName, interfaceToCast), e);
            result = initializeFromJars(packageName, jarFolderPath, interfaceToCast);
            logger.info(JAR_INITIALIZATION_TEMPLATE, result.getClass().getCanonicalName(), interfaceToCast.getCanonicalName(), jarFolderPath);
            beanRegistryService.register(beanName, result);
        }
        return result;
    }

    private <T> T initializeFromJars(final String packageName, final String jarFolderPath, final Class<T> interfaceToCast) {
        T result = null;
        try {
            addJarFilesInFolderPathToClassPath(jarFolderPath);
            Class clazz = packageBasedClassFinder.findClassInJar(jarFolderPath, interfaceToCast, packageName);
            result = externalClassInitializer.loadExternalClass(clazz.getCanonicalName(), jarFolderPath, interfaceToCast);
        } catch (DescriptorValidationFailedException | MalformedURLException dvfe) {
            logger.info("Couldn't initialize external class: '{}'", dvfe.getMessage());
            throw new DescriptorValidationFailedException("Cannot load class that implements interface '"
                    + interfaceToCast.getSimpleName() + "' with package or class name '" + packageName + "'");
        }
        return result;
    }

    private void addJarFilesInFolderPathToClassPath(final String jarFolderPath) {
        Collection<File> files = fileUtils.listFiles(fileFactory.createFile(jarFolderPath), "jar");
        for (File fileElement : files) {
            Agent.addClassPath(fileElement);
        }
    }

    private <T> String createBeanName(final String name, final Class<T> interfaceToCast) {
        return name + interfaceToCast.getSimpleName();
    }

}
