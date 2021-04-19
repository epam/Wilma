package com.epam.gepard.logger.helper;

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

import com.epam.gepard.common.Environment;
import com.epam.gepard.logger.LogFileWriter;

/**
 * Factory class for {@link LogFileWriter}.
 * @author Adam_Csaba_Kiraly
 */
public class LogFileWriterFactory {

    /**
     * Creates a new instance of {@link LogFileWriter}.
     * @param currentFile the name of the file that will be used by the writer.
     * @param extension is the extension of the file.
     * @param resultPathKey is the property key of the path where the file should be stored.
     * @param environment holds the properties of the application
     * @return the new instance
     */
    public LogFileWriter createSpecificLogWriter(final String currentFile, final String extension, final String resultPathKey,
            final Environment environment) {
        LogFileWriter logWriter = new LogFileWriter(environment.getProperty(Environment.GEPARD_RESULT_TEMPLATE_PATH) + "/" + "temp_generic."
                + extension, environment.getProperty(resultPathKey) + "/" + currentFile, environment);
        return logWriter;
    }

    /**
     * Creates a new instance of {@link LogFileWriter}.
     * @param logTemplatePath The path where the template is located.
     * @param logPath The path where the output file should be put.
     * @param environment holds the properties of the application
     * @return a new instance of {@link LogFileWriter}
     */
    public LogFileWriter createCustomWriter(final String logTemplatePath, final String logPath, final Environment environment) {
        return new LogFileWriter(logTemplatePath, logPath, environment);
    }
}
