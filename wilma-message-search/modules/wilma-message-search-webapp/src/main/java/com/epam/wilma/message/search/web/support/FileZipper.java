package com.epam.wilma.message.search.web.support;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for compressing files into one file using ZIP.
 *
 * @author Tamas_Bihari
 */
@Component
public class FileZipper {
    private final Logger logger = LoggerFactory.getLogger(FileZipper.class);
    @Autowired
    private ZipOutputStreamFactory zipOutputStreamFactory;
    @Autowired
    private ZipEntryFactory entryFactory;

    /**
     * Compress files which is specicified in the filePaths parameter
     * and write the zipped result into the given {@link OutputStream}.
     * @param filePaths are the path of the files which will be compressed into the result
     * @param result is the compressed file which contains the files
     */
    public void createZipWithFiles(final List<List<String>> filePaths, final OutputStream result) {
        ZipOutputStream zipStream = zipOutputStreamFactory.createZipOutputStream(result);
        zipFiles(filePaths, zipStream);
        closeZipStream(zipStream);
    }

    private void zipFiles(final List<List<String>> filePaths, final ZipOutputStream zipStream) {
        for (List<String> actualPair : filePaths) {
            for (String filePath : actualPair) {
                try {
                    String fileName = getFileName(filePath);
                    InputStream file = new FileInputStream(filePath);
                    addFileToZipStream(zipStream, fileName, file);
                    file.close();
                } catch (IOException ex) {
                    logger.warn("Reading or zipping is failed with file: " + filePath, ex);
                }
            }
        }
    }

    private void addFileToZipStream(final ZipOutputStream zipStream, final String fileName, final InputStream file) throws IOException {
        putZipEntryToStream(zipStream, fileName);
        IOUtils.copy(file, zipStream);
        zipStream.closeEntry();
    }

    private void putZipEntryToStream(final ZipOutputStream zipStream, final String fileName) throws IOException {
        try {
            ZipEntry zipEntry = entryFactory.createZipEntry(fileName);
            zipStream.putNextEntry(zipEntry);
        } catch (ZipException e) {
            logger.warn("Exception occurred while creating zip stream from file: " + fileName, e);
            putZipEntryToStream(zipStream, "1_" + fileName);
        }

    }

    private String getFileName(final String filePath) {
        String result = "";
        if (filePath.contains("/")) {
            result = filePath.substring(filePath.lastIndexOf("/") + 1);
        } else if (filePath.contains("\\")) {
            result = filePath.substring(filePath.lastIndexOf("\\") + 1);
        }
        return result;
    }

    private void closeZipStream(final ZipOutputStream zipStream) {
        try {
            zipStream.close();
        } catch (IOException e) {
            logger.warn("Zipping search result failed", e);
        }
    }
}
