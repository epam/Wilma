package com.epam.wilma.common.helper;
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

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.properties.PropertyHolder;

/**
 * Used to return the folder that contains the log files.
 * @author Marton_Sereg
 *
 */
@Component
public class LogFilePathProvider {

    @Autowired
    private PropertyHolder propertyHolder;

    /**
     * Returns the path of the log folder based on the configuration in the properties file.
     * @return Path of the log folder
     */
    public Path getLogFilePath() {
        String logTargetFolder = propertyHolder.get("log.folder");
        return FileSystems.getDefault().getPath(logTargetFolder);
    }

    /**
     * Returns the path of the application log folder.
     * @return Path of the application log folder
     */
    public Path getAppLogFilePath() {
        return FileSystems.getDefault().getPath("log", "app");
    }
}
