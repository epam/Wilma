package com.epam.wilma.webapp.config.servlet.loadinfo;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.maintainer.domain.DeletedFileProvider;
import com.epam.wilma.maintainer.task.helper.MessageFileCounter;
import com.epam.wilma.safeguard.configuration.domain.QueueSizeProvider;

/**
 * Servlet class for getting load information for message page.
 * @author Tibor_Kovacs
 */
@Component
public class LoadInformationServlet extends HttpServlet {

    private final QueueSizeProvider queueSizeProvider;
    private final DeletedFileProvider deletedFileProvider;
    private final MessageFileCounter messageCounter;

    /**
     * Constructor using spring framework to initialize the class.
     * @param queueSizeProvider provides size information of the message queue
     * @param deletedFileProvider provides information about the number of files deleted during the last maintenance period
     * @param messageCounter provides information about the existing messages logged and available in filesystem
     */
    @Autowired
    public LoadInformationServlet(QueueSizeProvider queueSizeProvider, DeletedFileProvider deletedFileProvider, MessageFileCounter messageCounter) {
        this.queueSizeProvider = queueSizeProvider;
        this.deletedFileProvider = deletedFileProvider;
        this.messageCounter = messageCounter;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int deletedFilesCount = deletedFileProvider.getDeletedFilesCount();
        int messagesCount = messageCounter.getCountOfMessages();
        long responseQueueSize = queueSizeProvider.getResponseQueueSize();
        long loggerQueueSize = queueSizeProvider.getLoggerQueueSize();

        out.write("{\"deletedFilesCount\":" + deletedFilesCount + "," + "\"countOfMessages\":" + messagesCount + "," + "\"responseQueueSize\":"
                + responseQueueSize + "," + "\"loggerQueueSize\":" + loggerQueueSize + "}");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
