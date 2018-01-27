package com.epam.wilma.webapp.config.servlet.logging;

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
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.webapp.config.servlet.helper.LogFileHandler;

/**
 * Sends as response the list of wilma logs in the application. If a particular log is
 * requested then it will send its content.
 * @author Tunde_Kovacs
 *
 */
@Component
public class WilmaLogHandlerServlet extends HttpServlet {

    private final LogFilePathProvider filePathProvider;
    private final LogFileHandler logFileHandler;

    /**
     * Constructor using spring framework to initialize the class.
     * @param filePathProvider provides the path of Wilma application log
     * @param logFileHandler provides the handler of the aplication log
     */
    @Autowired
    public WilmaLogHandlerServlet(LogFilePathProvider filePathProvider, LogFileHandler logFileHandler) {
        this.filePathProvider = filePathProvider;
        this.logFileHandler = logFileHandler;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Path path = filePathProvider.getAppLogFilePath();
        if (pathIsEmpty(req)) {
            logFileHandler.writeFileNamesToResponse(resp, path);
        } else {
            logFileHandler.writeFileContentToResponse(req, resp, req.getPathInfo(), path);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private boolean pathIsEmpty(final HttpServletRequest req) {
        return req.getPathInfo() == null;
    }
}
