package com.epam.wilma.webapp.config.servlet.stub;

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

import com.epam.wilma.webapp.service.StubConfigurationOrderService;

/**
 * This servlet calls the right service what will makes the expected changes in the stub configurations collection if the changes is possible.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationOrderServlet extends HttpServlet {

    private static final String HTML = "text/html";
    private static final String ERROR_MSG = "Wrong format of direction parameter. Direction must be an integer!";
    private final Logger logger = LoggerFactory.getLogger(StubConfigurationOrderServlet.class);

    private final StubConfigurationOrderService stubConfigurationOrderService;

    /**
     * Constructor using spring framework to initialize the class.
     * @param stubConfigurationOrderService provides access to the stub configuration
     */
    @Autowired
    public StubConfigurationOrderServlet(StubConfigurationOrderService stubConfigurationOrderService) {
        this.stubConfigurationOrderService = stubConfigurationOrderService;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String groupName = request.getParameter("groupname");
        String tempDir = request.getParameter("direction");
        try {
            int direction = Integer.parseInt(tempDir);
            stubConfigurationOrderService.doChange(direction, groupName, request);
        } catch (ClassNotFoundException|NumberFormatException e) {
            logger.debug(ERROR_MSG, e);
            writeErrorToResponse(response);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void writeErrorToResponse(final HttpServletResponse resp) throws IOException {
        resp.setContentType(HTML);
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        PrintWriter out = resp.getWriter();
        out.write(ERROR_MSG);
        out.flush();
        out.close();
    }
}
