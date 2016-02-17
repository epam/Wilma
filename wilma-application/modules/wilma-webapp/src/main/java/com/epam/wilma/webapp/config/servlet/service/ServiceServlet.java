package com.epam.wilma.webapp.config.servlet.service;

/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.common.helper.UniqueIdGenerator;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet class for providing extra services for wilma-service-api.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class ServiceServlet extends HttpServlet {
    private static final String LEADING_TEXT = "/public/service/";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        int requestedServicePosition = req.getRequestURI().indexOf(LEADING_TEXT) + LEADING_TEXT.length();
        String requestedService = req.getRequestURI().substring(requestedServicePosition);
        String response = "{\"unknownServiceCall\":\"" + requestedService + "\"}";
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        if ("uniqueId".equalsIgnoreCase(requestedService) && "get".equalsIgnoreCase(req.getMethod())) {
            // get a new unique id
            response = getUniqueId();
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        out.write(response);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String getUniqueId() {
        long nextUniqueId = UniqueIdGenerator.getNextUniqueId();
        return "{\"uniqueId\":\"" + nextUniqueId + "\"}";
    }
}
