package com.epam.gepard.logger;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.Environment;
import com.epam.gepard.exception.ShutDownException;
import com.epam.gepard.util.ExitCode;
import com.epam.gepard.util.FileUtil;

/**
 * Cleans the folders of log files.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class LogFolderCreator {
    private static final Logger CONSOLE_LOG = LoggerFactory.getLogger("console");
    private Environment environment;

    /**
     * Constructs a new instance of {@link LogFolderCreator}.
     * @param environment holds the properties of the application
     */
    public LogFolderCreator(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Deletes the old result folder and then creates a folder to every log files.
     */
    public void prepareOutputFolders() {
        //Clean-up the result folders
        deleteFolder(environment.getProperty(Environment.GEPARD_RESULT_PATH));

        //Create result folders
        createFolder(environment.getProperty(Environment.GEPARD_RESULT_PATH), "Cannot create general result dir: ");
        createFolder(environment.getProperty(Environment.GEPARD_XML_RESULT_PATH), "Cannot create XML result dir: ");
        createFolder(environment.getProperty(Environment.GEPARD_CSV_RESULT_PATH), "Cannot create CSV result dir: ");
        createFolder(environment.getProperty(Environment.GEPARD_HTML_RESULT_PATH), "Cannot create HTML result dir: ");
    }

    private void deleteFolder(final String filePath) {
        File logDir = new File(filePath);
        FileUtil fileUtil = new FileUtil();
        fileUtil.deleteDir(logDir); //it may happen that delete don't work
    }

    private void createFolder(final String filePath, final String errorMessage) {
        File logDir = new File(filePath);
        //noinspection ResultOfMethodCallIgnored
        logDir.mkdirs();
        if (!logDir.exists()) {
            CONSOLE_LOG.info(errorMessage + logDir);
            throw new ShutDownException(ExitCode.EXIT_CODE_OUTPUT_FOLDER_HANDLING_ERROR);
        }
    }
}
