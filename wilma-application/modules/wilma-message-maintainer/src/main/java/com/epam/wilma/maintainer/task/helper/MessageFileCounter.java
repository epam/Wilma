package com.epam.wilma.maintainer.task.helper;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.properties.PropertyHolder;

/**
 * This class counts the log files in the folder of logged messages.
 * @author Tibor_Kovacs
 *
 */
@Component
public class MessageFileCounter {
    private static final String FOLDER_ERROR_MESSAGE = "Folder does not exist.";

    private final Logger logger = LoggerFactory.getLogger(MessageFileCounter.class);

    private String messagesFolderPath;
    @Autowired
    private PropertyHolder propertyHolder;

    /**
     * This method counts the files in that folder which is given with the log.folder key in wilma.conf.propeties.
     * @return count of the files
     */
    public int getCountOfMessages() {
        int result;
        getMessagesFolderPath();
        try {
            File folder = new File(messagesFolderPath);
            folder = folder.getAbsoluteFile();
            result = folder.listFiles().length;
        } catch (NullPointerException e) {
            logger.debug(FOLDER_ERROR_MESSAGE, e);
            //this may happen only at the start of a brand new Wilma instance, and this solution is still better than executing the if statement forever and ever
            result = 0;
        }
        return result;
    }

    private void getMessagesFolderPath() {
        messagesFolderPath = propertyHolder.get("log.folder");
    }
}
