package com.epam.wilma.webapp.config.servlet.service;
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

import com.epam.wilma.common.helper.UniqueIdGenerator;
import com.epam.wilma.webapp.service.external.ServiceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet class for providing extra services for wilma-service-api.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class ServiceServlet extends HttpServlet {
    private static final String LEADING_TEXT = "/public/services/";
    private final Logger logger = LoggerFactory.getLogger(ServiceServlet.class);

    private final ServiceMap serviceMap;

    /**
     * Constructor using spring framework to initialize the class.
     * @param serviceMap provides access to the map of services
     */
    @Autowired
    public ServiceServlet(ServiceMap serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        //first identify the requested service
        String requestUri = req.getRequestURI();
        logger.info("Service call to: " + requestUri + ", method: " + req.getMethod());
        int positionOfLeadingText = requestUri.indexOf(LEADING_TEXT);
        int requestedServicePosition = positionOfLeadingText + LEADING_TEXT.length();
        String requestedService = positionOfLeadingText >= 0 ? requestUri.substring(requestedServicePosition) : "";

        //set the default answer
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        String response = null;

        //call the built-in service, as necessary - later should be part of the registered services !!!
        if ("UniqueIdGenerator/uniqueId".equalsIgnoreCase(requestedService) && "get".equalsIgnoreCase(req.getMethod())) {
            // get a new unique id
            response = getUniqueId();
            resp.setStatus(HttpServletResponse.SC_OK);
        }

        //call further registered services
        if (response == null) {
            response = serviceMap.callExternalService(req, requestedService, resp);
        }

        //if we still don't have the response, then either provide the service map, or send back that it is unknown request
        if (response == null) {
            if (requestedService.length() > 0) {
                response = "{ \"unknownServiceCall\": \"" + req.getMethod() + " " + requestedService + "\" }";
            } else {
                //call the built-in listing service (service-map)
                response = serviceMap.getMapAsResponse();
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String getUniqueId() {
        long nextUniqueId = UniqueIdGenerator.getNextUniqueId();
        return "{ \"uniqueId\": \"" + nextUniqueId + "\" }";
    }
}
