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
import com.epam.wilma.webapp.config.servlet.helper.MessageFileHandler;

/**
 * This servlet reads all the names of the messages logged and returns them in json.
 * @author Marton_Sereg
 *
 */
@Component
public class MessageHandlerServlet extends HttpServlet {

    private final LogFilePathProvider filePathProvider;
    private final MessageFileHandler messageFileHandler;

    /**
     * Constructor using spring framework to initialize the class.
     * @param filePathProvider provides the path where the messages are logged
     * @param messageFileHandler will write the list of messages
     */
    @Autowired
    public MessageHandlerServlet(LogFilePathProvider filePathProvider, MessageFileHandler messageFileHandler) {
        this.filePathProvider = filePathProvider;
        this.messageFileHandler = messageFileHandler;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Path path = filePathProvider.getLogFilePath();
        if (pathIsEmpty(req)) {
            messageFileHandler.writeFileNamesToResponse(resp, path);
        } else {
            messageFileHandler.writeFileContentToResponse(req, resp, req.getPathInfo(), path);
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
