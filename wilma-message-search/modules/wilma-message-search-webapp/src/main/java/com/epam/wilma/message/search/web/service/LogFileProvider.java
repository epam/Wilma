package com.epam.wilma.message.search.web.service;
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
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.web.support.FileUtils;

/**
 * Provides access to the log files.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class LogFileProvider {

    private static final String ERROR_MESSAGE = "Error occurred while reading file!";
    private static final String FILE_NOT_FOUND_MESSAGE = "File not found!";
    private static final String LOG_PATH = "log";

    @Autowired
    private FileUtils fileUtils;

    private final Logger logger = LoggerFactory.getLogger(LogFileProvider.class);

    /**
     * Returns the collection of log file names.
     * @return the collection of log file names
     */
    public Collection<String> getLogFileNames() {
        Collection<File> files = getLogFiles(LOG_PATH);
        return createFileNames(files);
    }

    /**
     * Returns the content of the log file.
     * @param fileName the name of the log file
     * @return the content of the log file
     */
    public String getLogContent(final String fileName) {
        Collection<File> files = getLogFiles(LOG_PATH);
        File file = findFile(fileName, files);
        return getContent(file);
    }

    private Collection<File> getLogFiles(final String logPath) {
        return fileUtils.getFilesWithExtension(logPath, "txt");
    }

    private Collection<String> createFileNames(final Collection<File> files) {
        Collection<String> fileNames = new ArrayList<>();
        for (File file : files) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    private String getContent(final File file) {
        String source = FILE_NOT_FOUND_MESSAGE;
        if (file != null) {
            try {
                source = fileUtils.readFileToString(file);
            } catch (IOException e) {
                source = ERROR_MESSAGE;
                logger.warn(ERROR_MESSAGE, e);
            }
        }
        return source;
    }

    private File findFile(final String fileName, final Collection<File> files) {
        File result = null;
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                result = file;
                break;
            }
        }
        return result;
    }
}
