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

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.webapp.config.servlet.helper.InputStreamUtil;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * File writer class.
 *
 * @author Tunde_Kovacs
 */
@Component
public class FileWriter {

    @Autowired
    private FileOutputStreamFactory fileOutputStreamFactory;
    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private ClassFilePathAssembler classFilePathAssembler;
    @Autowired
    private ByteArrayInputStreamFactory byteArrayInputStreamFactory;
    @Autowired
    private InputStreamUtil inputStreamUtil;
    @Autowired
    private JarValidator jarValidator;

    /**
     * Writes the content of an inputstream to a file.
     *
     * @param inputStream      the {@link InputStream} that will be written to the file
     * @param fileName         the name of the file the <tt>inputStream</tt> will be written
     * @param exceptionMessage the message that a {@link CannotUploadExternalResourceException} should be thrown with
     */
    public void write(final InputStream inputStream, final String fileName, final String exceptionMessage) {
        String filePath = fileName;
        InputStream inputStreamToWrite = inputStream;
        try {
            boolean isClass = fileName.endsWith(".class");
            boolean isJar = fileName.endsWith(".jar");
            if (isClass) {
                InputStreamCopier inputStreamCopier = new InputStreamCopier(inputStream, fileName, exceptionMessage);
                InputStream byteArrayInputStream = inputStreamCopier.createCopy();
                filePath = classFilePathAssembler.createFilePath(byteArrayInputStream, fileName, exceptionMessage);
                inputStreamToWrite = inputStreamCopier.createCopy();
            } else if (isJar) {
                InputStreamCopier inputStreamCopier = new InputStreamCopier(inputStream, fileName, exceptionMessage);
                InputStream byteArrayInputStream = inputStreamCopier.createCopy();
                jarValidator.validateInputStream(byteArrayInputStream);
                inputStreamToWrite = inputStreamCopier.createCopy();
            }

            File newClass = fileFactory.createFile(filePath);
            //if file is in a package, create folder structure
            String directories = newClass.getParent();
            if (directories != null && !directories.isEmpty()) {
                (fileFactory.createFile(directories)).mkdirs();
            }
            // if file doesn't exist, then create it
            if (!newClass.exists()) {
                newClass.createNewFile();
            }
            FileOutputStream fos = fileOutputStreamFactory.createFileOutputStream(newClass);
            IOUtils.copy(inputStreamToWrite, fos);
            fos.close();
            if (isClass || isJar) {
                inputStreamToWrite.close();
            }
        } catch (IOException e) {
            throw new CannotUploadExternalResourceException(exceptionMessage + filePath, e);
        }
    }

    private class InputStreamCopier {
        private final byte[] inputStreamAsBytes;

        public InputStreamCopier(final InputStream toBeCopied, final String fileName, final String exceptionMessage) {
            inputStreamAsBytes = tryToTransformInputStreamToBytes(toBeCopied, fileName, exceptionMessage);
        }

        public InputStream createCopy() {
            return byteArrayInputStreamFactory.createByteArrayInputStream(inputStreamAsBytes);
        }

        private byte[] tryToTransformInputStreamToBytes(final InputStream inputStream, final String fileName, final String exceptionMessage) {
            try {
                return inputStreamUtil.transformToByteArray(inputStream);
            } catch (IOException e) {
                throw new CannotUploadExternalResourceException(exceptionMessage + fileName, e);
            }
        }
    }

}
