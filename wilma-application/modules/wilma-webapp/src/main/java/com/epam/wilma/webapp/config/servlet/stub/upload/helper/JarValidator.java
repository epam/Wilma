package com.epam.wilma.webapp.config.servlet.stub.upload.helper;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;

/**
 * Used for checking the validity of jars.
 * @author Adam_Csaba_Kiraly
 * @author Tamas_Kohegyi
 *
 */
@Component
public class JarValidator {

    /**
     * Checks if the given jar has jar entries, throws a {@link CannotUploadExternalResourceException} if it doesn't.
     * Note: a copy of the original inputStream should be passed in to the method.
     * @param inputStream a copy of the jar's {@link InputStream}
     */
    public void validateInputStream(final InputStream inputStream) {
        try {
            JarInputStream jarInputStream = new JarInputStream(inputStream);
            JarEntry jarEntry = jarInputStream.getNextJarEntry();
            jarInputStream.close();
            if (jarEntry == null) {
                throw new IOException("Empty or invalid jar.");
            }
        } catch (IOException e) {
            throw new CannotUploadExternalResourceException(
                    "Could not upload external jar, as it has invalid format or jar is empty.", e);
        }
    }
}
