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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Facade for the class initializers.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ExternalInitializer {

    @Autowired
    private ExternalClassInitializer externalClassInitializer;
    @Autowired
    private ExternalJarClassInitializer externalJarClassInitializer;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;

    private final Logger logger = LoggerFactory.getLogger(ExternalInitializer.class);

    /**
     * Generic function, which imports external class from the file system and adds it to the class path.
     * @param <T> is the type of the return object
     * @param externalClassName is fully qualified name or just the package name
     * @param classPath is the resource path
     * @param interfaceToCast class of the desirable interface
     * @return with the new instance of the class
     * @throws DescriptorValidationFailedException if the class does not exist or not valid.
     */
    public <T> T loadExternalClass(final String externalClassName, final String classPath, final Class<T> interfaceToCast) {
        T result;
        try {
            result = externalClassInitializer.loadExternalClass(externalClassName, classPath, interfaceToCast);
        } catch (DescriptorValidationFailedException dvfe) {
            logger.info("Couldn't initialize external class: '{}'", dvfe.getMessage());
            String jarFolderPath = stubResourcePathProvider.getJarPathAsString();
            result = externalJarClassInitializer.loadExternalClass(externalClassName, jarFolderPath, interfaceToCast);
        }

        return result;
    }
}
