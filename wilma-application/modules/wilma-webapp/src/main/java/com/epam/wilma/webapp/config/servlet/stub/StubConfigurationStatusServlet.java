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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.service.StubConfigurationStatusService;

/**
 * This servlet calls the right service what will enabled or disabled the selected stub configuration.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationStatusServlet extends HttpServlet {

    private static final String HTML = "text/html";
    private static final String ERROR_MSG = "Wrong format of direction parameter. \"nextstatus\" must be a boolean!";

    private final StubConfigurationStatusService stubConfigurationStatusService;

    /**
     * Constructor using spring framework to initialize the class.
     * @param stubConfigurationStatusService provides access to the stub configuration
     */
    @Autowired
    public StubConfigurationStatusServlet(StubConfigurationStatusService stubConfigurationStatusService) {
        this.stubConfigurationStatusService = stubConfigurationStatusService;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String groupName = request.getParameter("groupname");
        String enabledDisabled = request.getParameter("nextstatus");
        if ("TRUE".equalsIgnoreCase(enabledDisabled) || "FALSE".equalsIgnoreCase(enabledDisabled)) {
            boolean nextStatus = Boolean.parseBoolean(enabledDisabled);
            try {
                stubConfigurationStatusService.changeStatus(nextStatus, groupName, request);
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
        } else {
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
