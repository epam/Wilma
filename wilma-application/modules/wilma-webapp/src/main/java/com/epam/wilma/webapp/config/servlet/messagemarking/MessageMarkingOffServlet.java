package com.epam.wilma.webapp.config.servlet.messagemarking;
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

import com.epam.wilma.messagemarker.configuration.MessageMarkerConfigurationAccess;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet that is used to switch off message marking when called.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class MessageMarkingOffServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(MessageMarkingOffServlet.class);

    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    private final MessageMarkerConfigurationAccess configurationAccess;

    /**
     * Constructor using spring framework to initialize the class.
     * @param urlAccessLogMessageAssembler is used to log the url access
     * @param configurationAccess provides access to Wilma configuration
     */
    @Autowired
    public MessageMarkingOffServlet(UrlAccessLogMessageAssembler urlAccessLogMessageAssembler, MessageMarkerConfigurationAccess configurationAccess) {
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
        this.configurationAccess = configurationAccess;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.info(urlAccessLogMessageAssembler.assembleMessage(req, "Message Marking: OFF"));
        configurationAccess.setMessageMarkerRequest(false);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
