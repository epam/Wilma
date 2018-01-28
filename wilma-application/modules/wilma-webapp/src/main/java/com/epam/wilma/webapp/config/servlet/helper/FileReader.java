package com.epam.wilma.webapp.config.servlet.helper;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.stream.helper.FileInputStreamFactory;

/**
 * Tries to read a file from a given folder and returns an InputStream, or null if file does not exist.
 * @author Tunde_Kovacs
 *
 */
@Component
public class FileReader {

    private final Logger logger = LoggerFactory.getLogger(FileReader.class);

    @Autowired
    private FileInputStreamFactory fileInputStreamFactory;

    /**
     * Reads a message file from the messages folder.
     * @param filename name of the file to read
     * @param filePath the path of the file
     * @return InputStream of file, or null if file doesn't exist.
     */
    public InputStream readFile(final String filename, final Path filePath) {
        File requestedFile = getFile(filename, filePath);
        InputStream fileInputStream = null;
        if (requestedFile.isFile()) {
            if (requestedFile.exists()) {
                try {
                    fileInputStream = fileInputStreamFactory.createFileInputStream(requestedFile);
                } catch (FileNotFoundException e) {
                    logger.warn("Message file not found!", e);
                }
            }
        }
        return fileInputStream;
    }

    private File getFile(final String filename, final Path filePath) {
        Path messageFilePath = filePath.resolve(filename);
        return messageFilePath.toFile();
    }
}
