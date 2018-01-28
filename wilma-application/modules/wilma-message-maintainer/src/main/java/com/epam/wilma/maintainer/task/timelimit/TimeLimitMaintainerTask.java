package com.epam.wilma.maintainer.task.timelimit;

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
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.indexing.jms.delete.JmsIndexDeletionProcessor;
import com.epam.wilma.maintainer.configuration.MaintainerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.DeletedFileProvider;
import com.epam.wilma.maintainer.task.MaintainerTask;

/**
 * This task runs scheduled as defined in wilma.conf.properties.
 * Used to maintain log files by deleting old ones based on a time limit.
 * @author Marton_Sereg
 *
 */
@Component
public class TimeLimitMaintainerTask implements MaintainerTask {

    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_DAY = 86400;
    private static final int TIMESTAMP_SUBSTRING = 14;
    private final Logger logger = LoggerFactory.getLogger(TimeLimitMaintainerTask.class);
    private final Map<Character, Integer> multipliers;
    private String timeLimit;
    private Integer timeLimitInSeconds;

    @Autowired
    private LogFilePathProvider logFilePath;
    @Autowired
    @Qualifier("message")
    private FileFilter messageFileFilter;
    @Autowired
    @Qualifier("simpleDateFormatterForFiles")
    private SimpleDateFormat simpleDateFormat;
    @Autowired
    private CurrentDateProvider currentDateProvider;
    @Autowired
    private MaintainerConfigurationAccess configurationAccess;
    @Autowired
    private JmsIndexDeletionProcessor indexDeletionProcessor;
    @Autowired
    private DeletedFileProvider deletedFileProvider;

    /**
     * Constructor that initialize the possible multiplier for the time limit.
     */
    public TimeLimitMaintainerTask() {
        multipliers = new HashMap<>();
        multipliers.put('H', SECONDS_IN_HOUR);
        multipliers.put('D', SECONDS_IN_DAY);
        multipliers.put('S', 1);
    }

    @Override
    public void run() {
        File messagesFolder = logFilePath.getLogFilePath().toFile();
        int noDeletedFiles = deleteFilesAboveFileLimit(messagesFolder);
        deletedFileProvider.setDeletedFilesCount(noDeletedFiles);
        logMaintenanceResult(noDeletedFiles);
    }

    @Override
    public void logParameters() {
        getTimeLimit();
        logger.info("Timelimit method is used to maintain log files with parameters: timelimit: " + timeLimit);
    }

    private int deleteFilesAboveFileLimit(final File messagesFolder) {
        int noDeletedFiles = 0;
        File[] messageFiles = messagesFolder.listFiles(messageFileFilter);
        if (messageFiles != null) {
            noDeletedFiles = deleteOldFiles(messageFiles);
        }
        return noDeletedFiles;
    }

    private int deleteOldFiles(final File[] messageFiles) {
        int noDeletedFiles = 0;
        for (File file : messageFiles) {
            try {
                noDeletedFiles += deleteFileIfTooOld(file);
            } catch (ParseException e) {
                logger.warn("File cannot be read because its name (" + file.getName() + ") doesn't match the message filename pattern.", e);
            }
        }
        return noDeletedFiles;
    }

    private int deleteFileIfTooOld(final File file) throws ParseException {
        String fileTimestamp = readTimestampFromFilename(file);
        int deleted = 0;
        Date messageDate = simpleDateFormat.parse(fileTimestamp);
        Date limitDate = retrieveLimitDate();
        if (messageDate.before(limitDate)) {
            deleteAndSendToIndex(file);
            deleted = 1;
        }
        return deleted;
    }

    private void deleteAndSendToIndex(final File file) {
        boolean success = file.delete();
        if (success) {
            indexDeletionProcessor.process(file.getAbsolutePath());
        }
    }

    private String readTimestampFromFilename(final File file) {
        return file.getName().substring(0, TIMESTAMP_SUBSTRING);
    }

    private Date retrieveLimitDate() {
        getTimeLimit();
        Date now = currentDateProvider.getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.SECOND, timeLimitInSeconds * -1);
        return cal.getTime();
    }

    private void getTimeLimit() {
        if (timeLimit == null) {
            MaintainerProperties properties = configurationAccess.getProperties();
            timeLimit = properties.getTimeLimit();
            timeLimitInSeconds = valueOfProperty() * multipliers.get(typeOfProperty());
        }
    }

    private int valueOfProperty() {
        return Integer.parseInt(timeLimit.substring(0, timeLimit.length() - 1));
    }

    private Character typeOfProperty() {
        return timeLimit.charAt(timeLimit.length() - 1);
    }

    private void logMaintenanceResult(final int noDeletedFiles) {
        if (noDeletedFiles > 0) {
            logger.info("Message file maintenance ended, " + noDeletedFiles + " files were deleted.");
        }
    }

}
