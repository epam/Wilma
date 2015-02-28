package com.epam.wilma.webapp.config.servlet.loadinfo;

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
 * Servlet class for getting load informations for message page.
 * @author Tibor_Kovacs
 */
@Component
public class LoadInformationServlet extends HttpServlet {

    private long responseQueueSize;
    private long loggerQueueSize;
    private int deletedFilesCount;
    private int messagesCount;

    @Autowired
    private QueueSizeProvider queueSizeProvider;
    @Autowired
    private DeletedFileProvider deletedFileProvider;
    @Autowired
    private MessageFileCounter messageCounter;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        getLoadInformations();
        out.write("{\"deletedFilesCount\":" + deletedFilesCount + "," + "\"countOfMessages\":" + messagesCount + "," + "\"responseQueueSize\":"
                + responseQueueSize + "," + "\"loggerQueueSize\":" + loggerQueueSize + "}");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void getLoadInformations() {
        deletedFilesCount = deletedFileProvider.getDeletedFilesCount();
        messagesCount = messageCounter.getCountOfMessages();
        responseQueueSize = queueSizeProvider.getResponseQueueSize();
        loggerQueueSize = queueSizeProvider.getLoggerQueueSize();
    }

}
