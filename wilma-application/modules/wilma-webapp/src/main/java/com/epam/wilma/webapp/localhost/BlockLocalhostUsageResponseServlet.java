package com.epam.wilma.webapp.localhost;
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

import org.springframework.stereotype.Component;

/**
 * Servlet used for writing the response to the requests that got "blocked" by LocalhostRequestProcessor.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class BlockLocalhostUsageResponseServlet extends HttpServlet {

    private static final String TEXT_PLAIN = "text/plain";
    private static final String BLOCKED_REQUEST_MESSAGE = "Wilma is configured to ignore request targeting to localhost/127.0.0.1";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        writeAnswer(resp);
    }

    private void writeAnswer(final HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_PLAIN);
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(BLOCKED_REQUEST_MESSAGE);
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
