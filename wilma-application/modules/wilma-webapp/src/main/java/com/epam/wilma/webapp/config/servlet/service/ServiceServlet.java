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

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String queryString = req.getQueryString();
        String response = "{\"unknownRequest\":\""
            + ((queryString == null) ? "null" : queryString)
            + "\"}";
        if ("getUniqueId".equals(req.getQueryString())) {
            // get a new unique id
            response = getUniqueId();
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
