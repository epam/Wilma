package com.epam.wilma.webapp.config.servlet.stub.helper;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.config.servlet.helper.FileReader;

/**
 * Writes a file to a {@link HttpServletResponse} based on its name and path.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ResourceFileNameHandler {

    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String FILE_NOT_FOUND_MSG = "Requested file not found.";
    private static final int BYTES_TO_READ = 1024;

    @Autowired
    private FileReader fileReader;

    /**
     * Writes a file to a {@link HttpServletResponse} as an attachment based on its name and path.
     * @param resp the response to which the file will be written
     * @param path contains the name of the file
     * @param filePath the file of the file to be written
     * @throws IOException if an input or output exception occurred
     */
    public void writeResourceFile(final HttpServletResponse resp, final String path, final Path filePath) throws IOException {
        String requestedFilename = path.substring(1);
        InputStream messageFileInputStream = fileReader.readFile(requestedFilename, filePath);
        if (messageFileExists(messageFileInputStream)) {
            writeResourceFileInResponse(resp, requestedFilename, messageFileInputStream);
        } else {
            writeErrorToResponse(resp);
        }
    }

    private boolean messageFileExists(final InputStream inputStream) {
        return inputStream != null;
    }

    private void writeResourceFileInResponse(final HttpServletResponse resp, final String requestedFilename, final InputStream messageFileInputStream)
        throws IOException {
        setHeaders(resp, requestedFilename);
        ServletOutputStream out = resp.getOutputStream();
        writeFileToOutputStream(messageFileInputStream, out);
        out.flush();
        out.close();
    }

    private void setHeaders(final HttpServletResponse resp, final String requestedFilename) {
        resp.setContentType(TEXT_PLAIN);
        resp.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + requestedFilename);
    }

    private void writeFileToOutputStream(final InputStream messageFileInputStream, final ServletOutputStream out) throws IOException {
        int read = 0;
        byte[] bytes = new byte[BYTES_TO_READ];
        while ((read = messageFileInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
    }

    private void writeErrorToResponse(final HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML);
        PrintWriter out = resp.getWriter();
        out.write(FILE_NOT_FOUND_MSG);
        out.flush();
        out.close();
    }
}
