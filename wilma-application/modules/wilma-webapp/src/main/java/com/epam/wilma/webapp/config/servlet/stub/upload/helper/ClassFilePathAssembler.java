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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.JavaClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.JavaClassFactory;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;

/**
 * Creates the path where a class file should be moved based on its package name.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ClassFilePathAssembler {

    private static final String FORWARDSLASH = "/";
    private static final String CLASS_FORMAT_EXCEPTION_MESSAGE = "'%s' has invalid class format.";
    @Autowired
    private JavaClassFactory javaClassFactory;
    @Autowired
    private FileFactory fileFactory;

    /**
     * Creates a path based on the full class name of the provided class file and the provided file name.
     * @param classFile the provided class file
     * @param fileName the name of the file, can be a path to the file
     * @param exceptionMessage the error message in case an exception happens
     * @return the path to where the class should be placed
     */
    public String createFilePath(final InputStream classFile, final String fileName, final String exceptionMessage) {
        String filePath = null;
        File file = fileFactory.createFile(fileName);
        String folderPart = file.getParent();
        String filePart = file.getName();
        String packageName;
        try {
            JavaClass javaClass = javaClassFactory.createJavaClass(classFile, fileName);
            packageName = javaClass.getPackageName();
            String packageAsFolderName = packageName.replace('.', '/');
            String folderStructure = folderPart + FORWARDSLASH + packageAsFolderName;
            filePath = folderStructure + FORWARDSLASH + filePart;
        } catch (IOException e) {
            throw new CannotUploadExternalResourceException(exceptionMessage + fileName, e);
        } catch (ClassFormatException e) {
            throw new CannotUploadExternalResourceException(exceptionMessage + String.format(CLASS_FORMAT_EXCEPTION_MESSAGE, fileName), e);
        }
        return filePath;
    }

}
