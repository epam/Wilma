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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(ServiceServlet.class);


    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        //first identify the requested service
        int requestedServicePosition = req.getRequestURI().indexOf(LEADING_TEXT) + LEADING_TEXT.length();
        String requestedService = req.getRequestURI().substring(requestedServicePosition);
        logger.info("Service call to: " + requestedService + ", method: " + req.getMethod());

        //set the default answer
        resp.setContentType("application/json");
        String response = "{\"unknownServiceCall\":\"" + requestedService + "\"}";
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        //call the built-in service, as necessary
        if ("uniqueId".equalsIgnoreCase(requestedService) && "get".equalsIgnoreCase(req.getMethod())) {
            // get a new unique id
            response = getUniqueId();
            resp.setStatus(HttpServletResponse.SC_OK);
        }

        //call further registered services
        // to be implemented, something response = calls to methods of the service

        //write the answer back
        PrintWriter out = resp.getWriter();
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
