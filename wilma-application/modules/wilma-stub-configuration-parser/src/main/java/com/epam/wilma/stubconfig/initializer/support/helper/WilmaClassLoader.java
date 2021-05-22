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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * CLass used to load external classes in a dynamic way.
 */
public class WilmaClassLoader extends ClassLoader {
    private final Logger logger = LoggerFactory.getLogger(WilmaClassLoader.class);
    private final String path;

    public WilmaClassLoader(final String path) {
        this.path = path;
    }

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        String resourceName = path + "/" + name.replace('.', '/') + ".class";
        logger.info("Loading class: {}", name);
        byte[] b = loadClassFromFile(resourceName);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) throws ClassNotFoundException {
        byte[] buffer;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream != null) {
            buffer = transferToByteArray(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ClassNotFoundException("Cannot load class as resource.", e);
            }
            return buffer;
        }
        //was null, so retry to load it as file
        File file = new File(fileName);
        try (InputStream fileInputStream = new FileInputStream(file)) {
            buffer = transferToByteArray(fileInputStream);
        } catch (IOException e) {
            throw new ClassNotFoundException("Cannot load class as file.", e);
        }
        return buffer;
    }

    private byte[] transferToByteArray(InputStream inputStream) throws ClassNotFoundException {
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue;
        try {
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException("Cannot load class as resource.", e);
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}
