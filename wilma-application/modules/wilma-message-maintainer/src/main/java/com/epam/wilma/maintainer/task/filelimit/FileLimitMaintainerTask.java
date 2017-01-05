package com.epam.wilma.maintainer.task.filelimit;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.indexing.jms.delete.JmsIndexDeletionProcessor;
import com.epam.wilma.maintainer.configuration.MaintanerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.DeletedFileProvider;
import com.epam.wilma.maintainer.task.MaintainerTask;

/**
 * This task runs scheduled as defined in wilma.conf.properties.
 * Used to maintain log files by deleting old ones based on a file limit.
 * @author Marton_Sereg
 *
 */
@Component
public class FileLimitMaintainerTask implements MaintainerTask {

    private static final int TIMESTAMP_END_INDEX = 19;
    private final Logger logger = LoggerFactory.getLogger(FileLimitMaintainerTask.class);
    private Integer fileLimit;

    @Autowired
    @Qualifier("message")
    private FileFilter messageFileFilter;
    @Autowired
    private LogFilePathProvider logFilePath;
    @Autowired
    private MaintanerConfigurationAccess configurationAccess;
    @Autowired
    private JmsIndexDeletionProcessor indexDeletionProcessor;
    @Autowired
    private DeletedFileProvider deletedFileProvider;

    @Override
    public void run() {
        File messagesFolder = logFilePath.getLogFilePath().toFile();
        deleteFilesAboveFileLimit(messagesFolder);
    }

    @Override
    public void logParameters() {
        getFileLimit();
        logger.info("Filelimit method is used to maintain log files with parameters: filelimit: " + fileLimit);
    }

    private void getFileLimit() {
        if (fileLimit == null) {
            MaintainerProperties properties = configurationAccess.getProperties();
            fileLimit = properties.getFileLimit();
        }
    }

    private void deleteFilesAboveFileLimit(final File messagesFolder) {
        File[] messageFiles = messagesFolder.listFiles(messageFileFilter);
        if (messageFiles != null) {
            sortMessageFilesByTimestamp(messageFiles);
            deleteFilesAboveFileLimit(messageFiles);
        }
    }

    private void sortMessageFilesByTimestamp(final File[] messageFiles) {
        Arrays.sort(messageFiles, new Comparator<File>() {
            @Override
            public int compare(final File file1, final File file2) {
                String file1timestamp = retrieveIdentifier(file1);
                String file2timestamp = retrieveIdentifier(file2);
                return file1timestamp.compareTo(file2timestamp);
            }

            private String retrieveIdentifier(final File file) {
                return file.getName().substring(0, TIMESTAMP_END_INDEX);
            }
        });
    }

    private void deleteFilesAboveFileLimit(final File[] messageFiles) {
        getFileLimit();
        if (messageFiles.length > fileLimit) {
            int noFilesToDelete = messageFiles.length - fileLimit;
            deleteAndSendToDeletionFromIndex(messageFiles, noFilesToDelete);
            deletedFileProvider.setDeletedFilesCount(noFilesToDelete);
            logger.info(noFilesToDelete + " files were deleted.");
        } else {
            deletedFileProvider.setDeletedFilesCount(Integer.valueOf(0));
        }
    }

    private void deleteAndSendToDeletionFromIndex(final File[] messageFiles, final int noFilesToDelete) {
        for (int i = 0; i < noFilesToDelete; i++) {
            boolean success = messageFiles[i].delete();
            if (success) {
                indexDeletionProcessor.process(messageFiles[i].getAbsolutePath());
            }
        }
    }
}
