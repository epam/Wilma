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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.FileListJsonProperties;

/**
 * Sends as response the list of messages in the application. If a particular message is
 * requested then it will send its content.
 * @author Tibor_Kovacs
 */
@Component
public class MessageFileHandler {
    private static final String APPLICATION_JSON = "application/json";
    @Autowired
    private FileListJsonBuilder messageFileJsonBuilder;
    @Autowired
    private WebAppConfigurationAccess configurationAccess;
    @Autowired
    private LogFileHandler logFileHandler;

    /**
     * Writes a list of file names from a given path to a {@link HttpServletResponse}.
     * But it uses a limit, how much files will be written.
     * @param resp the response to which the file names are written
     * @param path the path where the files can be found
     * @throws IOException if an input or output exception occurred
     */
    public void writeFileNamesToResponse(final HttpServletResponse resp, final Path path) throws IOException {
        FileListJsonProperties properties = configurationAccess.getProperties().getFileListProperties();
        PrintWriter out = resp.getWriter();
        resp.setContentType(APPLICATION_JSON);
        out.write(messageFileJsonBuilder.buildMessageFileListJson(path.toFile(), properties.getMaximumCountOfMessages()));
        out.flush();
        out.close();
    }

    /**
     * Writes the content of a file to a {@link HttpServletResponse}. If the file headers contains
     * a paramater 'source=true' then it will simply write the content of the file, otherwise it
     * sends the file as an attachment.
     * @param req the  {@link HttpServletRequest} containing the requested file name
     * @param resp the response to which the file content is written
     * @param pathInfo contains the name of the file to be written
     * @param path the path to the file
     * @throws IOException if an input or output exception occurred
     */
    public void writeFileContentToResponse(final HttpServletRequest req, final HttpServletResponse resp, final String pathInfo, final Path path)
        throws IOException {
        logFileHandler.writeFileContentToResponse(req, resp, pathInfo, path);
    }
}
