package com.epam.wilma.logger.writer;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Writes a request or a response message to a file.
 * @author Tunde_Kovacs
 *
 * @param <T> it can be either a <tt>WilmaHttpRequest</tt> or a <tt>WilmaHttpResponse</tt>
 */
public abstract class WilmaHttpEntityWriter<T> {

    private static final String FI_PREFIX = "FI";

    @Autowired
    private LogFilePathProvider logFilePath;
    @Autowired
    private DirectoryFactory directoryFactory;

    /**
     * Writes the headers and the body of a <tt>WilmaHttpEntity</tt> to a file.
     * @param wilmaHttpEntity the entity to be written
     * @param bodyDecompressed flags if the body of the HTTP message is decompressed
     * @return true if writing was successful, false otherwise
     */
    public abstract boolean write(T wilmaHttpEntity, boolean bodyDecompressed);

    /**
     * Writes the WILMA_LOGGER_ID of the message to a {@link BufferedWriter}.
     * @param writer the buffered writer
     * @param loggerId the logger id
     * @throws IOException if an I/O error occurs
     */
    protected void writeWilmaLoggerId(final BufferedWriter writer, final String loggerId) throws IOException {
        writer.append(WilmaHttpEntity.WILMA_LOGGER_ID + ":" + loggerId);
        writer.newLine();
    }

    /**
     * Writes the headers of a message to a {@link BufferedWriter}.
     * @param writer the buffered writer
     * @param headers the headers of the Http Request/Response
     * @throws IOException if an I/O error occurs
     */
    protected void writeHeaders(final BufferedWriter writer, final String headers) throws IOException {
        writer.append(headers);
        writer.newLine();
    }

    /**
     * Writes the body of a message to a {@link BufferedWriter}.
     * @param writer the buffered writer
     * @param body the body of the Http Request/Response
     * @throws IOException if an I/O error occurs
     */
    protected void writeBody(final BufferedWriter writer, final String body) throws IOException {
        if (body != null) {
            writer.newLine();
            writer.append(body);
        }
    }

    String getOutputFileName(final String messageLoggerId, final boolean bodyDecompressed) {
        StringBuilder result = new StringBuilder();
        createTargetFolder();
        result.append(getTargetFolderPath()).append("//");
        if (!bodyDecompressed) {
            result.append(FI_PREFIX);
        }
        result.append(messageLoggerId);
        result.append(".txt");
        return result.toString();
    }

    private String createTargetFolder() {
        String result = "";
        File theDir = directoryFactory.createNewDirectory(getTargetFolderPath().toString());
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        result = theDir.getName();
        return result;
    }

    private Path getTargetFolderPath() {
        return logFilePath.getLogFilePath().toAbsolutePath();
    }

}
