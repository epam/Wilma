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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.stubconfig.dom.parser.node.helper.ClassNameMapper;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.condition.helper.ClassFactory;
import com.epam.wilma.stubconfig.initializer.condition.helper.ClassPathExtender;
import com.epam.wilma.stubconfig.initializer.support.helper.BeanRegistryService;
import com.epam.wilma.stubconfig.initializer.support.helper.ClassInstantiator;
import com.epam.wilma.stubconfig.initializer.support.helper.ClassValidator;

/**
 * Class for initializing external classes and adding them to class path.
 * @author Tamas_Bihari, Tamas Kohegyi
 *
 */
@Component
public class ExternalClassInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalClassInitializer.class);

    @Autowired
    private ClassPathExtender classPathExtender;
    @Autowired
    private ClassFactory classFactory;
    @Autowired
    private BeanRegistryService beanRegistryService;
    @Autowired
    private ClassNameMapper classNameMapper;
    @Autowired
    private ClassValidator externalClassValidator;
    @Autowired
    private ClassInstantiator classInstantiator;

    /**
     * Generic function, which imports external class from the file system and adds it to the class path.
     * @param <T> is the type of the return object
     * @param externalClassName is fully qualified name
     * @param classPath is the resource path
     * @param interfaceToCast class of the desirable interface
     * @return with the new instance of the class
     * @throws DescriptorValidationFailedException if the class does not exist or not valid.
     */
    public <T> T loadExternalClass(final String externalClassName, final String classPath, final Class<T> interfaceToCast) {
        String simpleName = getSimpleName(externalClassName);
        T result;
        try {
            result = findBean(interfaceToCast, simpleName);
        } catch (BeansException ex) {
            LOGGER.debug(String.format("Finding class with name '%s' of type '%s' as a bean failed", simpleName, interfaceToCast), ex);
            String fullClassName = classNameMapper.get(externalClassName);
            result = initializeBean(fullClassName, classPath, interfaceToCast, simpleName);
        }
        return result;
    }

    private <T> T initializeBean(final String externalClassName, final String classPath, final Class<T> interfaceToCast, final String className) {
        LOGGER.info("Initializing class {} of type {}, using classpath {}.", externalClassName + "/" + className, interfaceToCast, classPath);
        T result = instantiateExternalClass(externalClassName, classPath, interfaceToCast);
        beanRegistryService.register(className, result);
        return result;
    }

    private <T> T instantiateExternalClass(final String externalClassName, final String classPath, final Class<T> interfaceToCast) {
        T result;
        classPathExtender.addFile(classPath);
        try {
            Class<T> classToLoad = null;
            classToLoad = classFactory.getClassToLoad(externalClassName);
            result = classInstantiator.createClassInstanceOf(classToLoad);
            externalClassValidator.validateInterface(result, interfaceToCast, classPath);
        } catch (SecurityException  | IllegalArgumentException | ReflectiveOperationException e) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - External class '" + classPath + "/"
                    + externalClassName + "' not found.", e);
        } catch (NoClassDefFoundError e) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - External class '" + classPath + "/"
                    + externalClassName + "' cannot be loaded. Issue: " + e.getMessage(), e);
        } catch (ClassFormatError e) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - External class '" + classPath + "/"
                    + externalClassName + "' has invalid class format.", e);
        }
        return result;
    }

    private <T> T findBean(final Class<T> interfaceToCast, final String className) {
        LOGGER.debug("Searching for class with name {} of type {}", className, interfaceToCast);
        return beanRegistryService.getBean(className, interfaceToCast);
    }

    private String getSimpleName(final String externalClassName) {
        String[] split = externalClassName.split("\\.");
        return split[split.length - 1];
    }

}
