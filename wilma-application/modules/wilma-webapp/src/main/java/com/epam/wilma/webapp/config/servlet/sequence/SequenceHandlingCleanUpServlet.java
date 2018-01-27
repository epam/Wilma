package com.epam.wilma.webapp.config.servlet.sequence;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.sequence.maintainer.SequenceMaintainer;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Servlet for running the sequence maintenance task manually.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class SequenceHandlingCleanUpServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(SequenceHandlingCleanUpServlet.class);

    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    private final SequenceMaintainer sequenceMaintainer;

    /**
     * Constructor using spring framework to initialize the class.
     * @param urlAccessLogMessageAssembler is used to log the url access
     * @param sequenceMaintainer provides access to the sequence handler
     */
    @Autowired
    public SequenceHandlingCleanUpServlet(UrlAccessLogMessageAssembler urlAccessLogMessageAssembler, SequenceMaintainer sequenceMaintainer) {
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
        this.sequenceMaintainer = sequenceMaintainer;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        sequenceMaintainer.runOnDemand();
        logger.info(urlAccessLogMessageAssembler.assembleMessage(req, "Sequence maintenance task invoked manually"));
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
