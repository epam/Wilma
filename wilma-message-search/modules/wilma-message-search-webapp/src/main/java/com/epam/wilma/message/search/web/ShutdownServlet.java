package com.epam.wilma.message.search.web;

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

import com.epam.wilma.message.search.web.service.WebAppStopper;

/**
 * Servlet for shutting down Wilma Message Search.
 *
 * @author Adam_Csaba_Kiraly
 */
public class ShutdownServlet extends HttpServlet {

    private static final String SHUTDOWN_MESSAGE = "Shutting down Wilma Message Search.";
    private final WebAppStopper webAppStopper;
    private final Logger logger = LoggerFactory.getLogger(ShutdownServlet.class);

    /**
     * Constructs a new instance of {@link ShutdownServlet}.
     * @param webAppStopper the webAppStopper service to use for shutting down
     */
    public ShutdownServlet(final WebAppStopper webAppStopper) {
        this.webAppStopper = webAppStopper;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.info(SHUTDOWN_MESSAGE);
        writeResponse(resp);
        webAppStopper.stopAsync();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void writeResponse(final HttpServletResponse resp) throws IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(SHUTDOWN_MESSAGE);
        printWriter.flush();
        printWriter.close();
    }

}
