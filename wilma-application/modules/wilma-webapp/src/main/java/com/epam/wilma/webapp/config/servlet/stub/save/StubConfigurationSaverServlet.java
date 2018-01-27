package com.epam.wilma.webapp.config.servlet.stub.save;
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

import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.service.StubConfigurationSaverService;

/**
 * This servlet starts to save stub configurations into the cache folder.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationSaverServlet extends HttpServlet {
    private static final String SUCCESSFUL_ANSWER = "Stub configurations are saved.";

    private static final String HTML = "text/html";

    private final Logger logger = LoggerFactory.getLogger(StubConfigurationSaverServlet.class);

    private final StubConfigurationSaverService stubConfigurationSaverService;
    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    /**
     * Constructor using spring framework to initialize the class.
     * @param stubConfigurationSaverService provides access to the service that saves the actual stub configuration
     * @param urlAccessLogMessageAssembler is used to log the url access event
     */
    @Autowired
    public StubConfigurationSaverServlet(StubConfigurationSaverService stubConfigurationSaverService, UrlAccessLogMessageAssembler urlAccessLogMessageAssembler) {
        this.stubConfigurationSaverService = stubConfigurationSaverService;
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            stubConfigurationSaverService.saveStubConfigurations();
            logger.info(urlAccessLogMessageAssembler.assembleMessage(req, SUCCESSFUL_ANSWER));
            writeResponse(resp, SUCCESSFUL_ANSWER, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.info(urlAccessLogMessageAssembler.assembleMessage(req, e.getMessage()), e);
            writeResponse(resp, "Saving FAILED! " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void writeResponse(final HttpServletResponse resp, final String result, final int responseCode) throws IOException {
        resp.setContentType(HTML);
        resp.setStatus(responseCode);
        PrintWriter writer = resp.getWriter();
        writer.append(result);
        writer.flush();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
