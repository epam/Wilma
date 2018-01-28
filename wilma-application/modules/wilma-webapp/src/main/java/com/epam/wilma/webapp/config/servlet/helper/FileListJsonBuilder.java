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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Builds a JSON response message from the filenames in a given directory.
 * @author Marton_Sereg
 * @author Tunde_Kovacs
 *
 */
@Component
public class FileListJsonBuilder {

    private static final String FILES = "files";
    private static Logger logger = LoggerFactory.getLogger(FileListJsonBuilder.class);
    @Autowired
    private FileUtils fileUtilsWrapper;

    /**
     * Builds a JSON response based on the filenames in a given directory.
     * @param directory the directory where the filenames should be listed in the JSON
     * @return JSON response as String
     */
    public String buildLogFileListJson(final File directory) {
        String result = "{\"files\":[]}";
        String[] messageFiles = directory.list();
        if (messageFiles != null) {
            Arrays.sort(messageFiles);
            result = getJson(messageFiles);
        } else {
            logger.debug("The directory '" + directory + "' has not been created yet, or an I/O error occured.");
        }
        return result;
    }

    /**
     * Builds a JSON response based on the filenames in a given directory.
     * @param directory the directory where the filenames should be listed in the JSON
     * @param maxValue is the limit, how much filenames will be listed.
     * @return JSON response as String
     */
    public String buildMessageFileListJson(final File directory, final int maxValue) {
        String result = "{\"files\":[]}";
        String[] messageFiles = directory.list();
        if (messageFiles != null) {
            String[] resultFiles = messageFiles;
            Arrays.sort(resultFiles);
            if (messageFiles.length > maxValue) {
                resultFiles = Arrays.copyOfRange(messageFiles, resultFiles.length - maxValue, resultFiles.length);
            }
            result = getJson(resultFiles);
        } else {
            logger.debug("The directory '" + directory + "' has not been created yet, or an I/O error occured.");
        }
        return result;
    }

    /**
     * Builds a JSON response based on the filenames in a given directory. If the directory
     * contains subdirectories, their files will be listed as well.
     * @param directory the directory that's filenames should be listed in the JSON
     * @return JSON response as a string
     */
    public String buildFileListJson(final File directory) {
        String result = "{\"files\":[]}";
        Collection<File> messageFiles = fileUtilsWrapper.listFiles(directory);
        if (messageFiles != null) {
            List<String> fileNames = getFileNames(messageFiles);
            Collections.sort(fileNames);
            result = getJson(fileNames);
        } else {
            logger.debug("The directory '" + directory + "' has not been created yet, or an I/O error occurred.");
        }
        return result;
    }

    private List<String> getFileNames(final Collection<File> messageFiles) {
        List<String> result = new ArrayList<>();
        for (File file : messageFiles) {
            String fileName = file.getName();
            result.add(fileName);
        }
        return result;
    }

    private String getJson(final Object object) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FILES, gson.toJsonTree(object));
        String json = gson.toJson(jsonObject);
        return json;
    }
}
