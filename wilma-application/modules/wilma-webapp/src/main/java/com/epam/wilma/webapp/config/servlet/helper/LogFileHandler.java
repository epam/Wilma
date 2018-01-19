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
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends as response the list of wilma logs in the application. If a particular log is
 * requested then it will send its content.
 * @author Tunde_Kovacs
 *
 */
@Component
public class LogFileHandler {

    private static final String APPLICATION_JSON = "application/json";
    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String FILE_NOT_FOUND_MSG = "Requested file not found.";

    @Autowired
    private FileListJsonBuilder messageFileJsonBuilder;
    @Autowired
    private FileReader messageFileReader;
    @Autowired
    private InputStreamUtil inputStreamConverter;


    /**
     * Writes a list of file names from a given path to a {@link HttpServletResponse}.
     * @param resp the response to which the file names are written
     * @param path the path where the files can be found
     * @throws IOException if an input or output exception occurred
     */
    public void writeFileNamesToResponse(final HttpServletResponse resp, final Path path) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType(APPLICATION_JSON);
        out.write(messageFileJsonBuilder.buildLogFileListJson(path.toFile()));
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
        String requestedFilename = pathInfo.substring(1);
        InputStream messageFileInputStream = messageFileReader.readFile(requestedFilename, path);
        if (messageFileExists(messageFileInputStream)) {
            writeFileToResponse(req, resp, requestedFilename, messageFileInputStream);
        } else {
            writeErrorToResponse(resp);
        }
    }

    private void writeFileToResponse(final HttpServletRequest req, final HttpServletResponse resp, final String requestedFilename,
            final InputStream messageFileInputStream) throws IOException {
        String sourceParameter = req.getParameter("source");
        String userAgent = req.getHeader("User-Agent");
        if (sourceParameter != null && "true".equalsIgnoreCase(sourceParameter)) {
            sendFileContentInResponse(resp, messageFileInputStream, userAgent);
        } else {
            sendMessageFileAsAttachmentInResponse(resp, requestedFilename, messageFileInputStream, userAgent);
        }
    }

    private boolean messageFileExists(final InputStream inputStream) {
        return inputStream != null;
    }

    private void sendMessageFileAsAttachmentInResponse(final HttpServletResponse resp, final String requestedFilename,
            final InputStream messageFileInputStream, final String userAgent) throws IOException {
        setHeaders(resp, requestedFilename);
        setContent(resp, messageFileInputStream, userAgent);
    }

    private void sendFileContentInResponse(final HttpServletResponse resp, final InputStream messageFileInputStream, final String userAgent)
        throws IOException {
        resp.setContentType(TEXT_PLAIN);
        setContent(resp, messageFileInputStream, userAgent);
    }

    private void writeErrorToResponse(final HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML);
        PrintWriter out = resp.getWriter();
        out.write(FILE_NOT_FOUND_MSG);
        out.flush();
        out.close();
    }

    private void setHeaders(final HttpServletResponse resp, final String requestedFilename) {
        resp.setContentType(TEXT_PLAIN);
        resp.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + requestedFilename);
    }

    private void setContent(final HttpServletResponse resp, final InputStream messageFileInputStream, final String userAgent) throws IOException {
        PrintWriter writer = resp.getWriter();
        String fileContent = getContentFromInputStream(messageFileInputStream, userAgent);
        writer.write(fileContent);
        writer.flush();
        writer.close();
    }

    private String getContentFromInputStream(final InputStream messageFileInputStream, final String userAgent) throws IOException {
        String fileContent = inputStreamConverter.toString(messageFileInputStream);
        if (userIsOnWindows(userAgent)) {
            fileContent = fileContent.replace("\r", "").replace("\n", "\r\n");
        }
        return fileContent;
    }

    private boolean userIsOnWindows(final String userAgent) {
        return userAgent != null && userAgent.toLowerCase().contains("windows");
    }

}
