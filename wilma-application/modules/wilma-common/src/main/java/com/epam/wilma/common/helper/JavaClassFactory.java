package com.epam.wilma.common.helper;
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
import java.io.InputStream;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.stream.helper.FileInputStreamFactory;

/**
 * Used for instantiating JavaClass objects.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class JavaClassFactory {

    @Autowired
    private FileInputStreamFactory fileInputStreamFactory;

    /**
     * Creates a new instance of JavaClass based on the given resource.
     * The resFileName is only used for logging purposes.
     * @param resource the class file
     * @param resFileName the name of the class file
     * @return a JavaClass instance
     * @throws IOException if an IO error occurs
     */
    public JavaClass createJavaClass(InputStream resource, String resFileName) throws IOException {
        ClassParser classParser = new ClassParser(resource, resFileName);
        return classParser.parse();
    }

    /**
     * Creates a new instance of JavaClass based on the given file.
     * @param file the class file
     * @return a JavaClass instance
     * @throws IOException if an IO error occurs
     */
    public JavaClass createJavaClass(File file) throws IOException {
        InputStream resource = fileInputStreamFactory.createFileInputStream(file);
        String resFileName = file.getName();
        return createJavaClass(resource, resFileName);
    }
}
