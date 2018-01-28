package com.epam.wilma.webapp.config.servlet;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.WilmaService;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Servlet for shutting down Wilma.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class ShutdownServlet extends HttpServlet {

    private static final String SHUTDOWN_MESSAGE = "Shutting down Wilma.";

    private final Logger logger = LoggerFactory.getLogger(ShutdownServlet.class);

    private final WilmaService wilmaService;
    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    /**
     * Constructor using spring framework to initialize the class.
     * @param wilmaService is the Wilma service object
     * @param urlAccessLogMessageAssembler is used to log the url access event
     */
    @Autowired
    public ShutdownServlet(WilmaService wilmaService, UrlAccessLogMessageAssembler urlAccessLogMessageAssembler) {
        this.wilmaService = wilmaService;
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        writeResponse(resp);
        logger.info(urlAccessLogMessageAssembler.assembleMessage(req, SHUTDOWN_MESSAGE));
        wilmaService.stop();
    }

    private void writeResponse(final HttpServletResponse resp) throws IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(SHUTDOWN_MESSAGE);
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
