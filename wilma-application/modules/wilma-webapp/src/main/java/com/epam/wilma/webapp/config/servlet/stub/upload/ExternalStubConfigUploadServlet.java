package com.epam.wilma.webapp.config.servlet.stub.upload;
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

import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.service.command.NewStubDescriptorCommand;
import com.epam.wilma.webapp.service.external.ServiceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for uploading an external stub configuration file.
 *
 * @author Tunde_Kovacs
 */
@Component
public class ExternalStubConfigUploadServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalStubConfigUploadServlet.class);

    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    private final StubDescriptorJsonFactory stubDescriptorJsonFactory;
    private final RoutingService routingService;
    private final SequenceDescriptorHolder sequenceDescriptorHolder;
    private final ServiceMap serviceMap;

    /**
     * Constructor using spring framework to initialize the class.
     *
     * @param urlAccessLogMessageAssembler is used to log the url access event
     * @param stubDescriptorJsonFactory    factory of stub descriptors
     * @param routingService               provides access to the routing service
     * @param sequenceDescriptorHolder     provides access to the sequence descriptors
     * @param serviceMap                   provides access to the internal service map
     */
    @Autowired
    public ExternalStubConfigUploadServlet(UrlAccessLogMessageAssembler urlAccessLogMessageAssembler, StubDescriptorJsonFactory stubDescriptorJsonFactory,
                                           RoutingService routingService, SequenceDescriptorHolder sequenceDescriptorHolder, ServiceMap serviceMap) {
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
        this.stubDescriptorJsonFactory = stubDescriptorJsonFactory;
        this.routingService = routingService;
        this.sequenceDescriptorHolder = sequenceDescriptorHolder;
        this.serviceMap = serviceMap;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        if (request.getContentLength() > 0) {
            ServletInputStream inputStream = request.getInputStream();
            try {
                routingService.performModification(new NewStubDescriptorCommand(inputStream, stubDescriptorJsonFactory, sequenceDescriptorHolder));
                serviceMap.detectServices();
                LOGGER.info(urlAccessLogMessageAssembler.assembleMessage(request, "New stub configuration was uploaded to Wilma."));
            } catch (ClassNotFoundException | NoClassDefFoundError | SystemException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Stub config uploading failed: " + e.getMessage());
                LOGGER.warn(urlAccessLogMessageAssembler.assembleMessage(request, "Stub config uploading failed: " + e.getMessage()), e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("Please provide a non-empty stub configuration!");
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
