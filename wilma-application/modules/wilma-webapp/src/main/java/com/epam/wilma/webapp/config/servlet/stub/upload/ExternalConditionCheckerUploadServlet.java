package com.epam.wilma.webapp.config.servlet.stub.upload;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Servlet for uploading an external condition checker class from an url and placing it into
 * the corresponding resource folder.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ExternalConditionCheckerUploadServlet extends CommonExternalUploadServlet {

    private static final String EXCEPTION_MESSAGE = "Could not upload external condition checker class: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalConditionCheckerUploadServlet.class);

    @Autowired
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private FileWriter fileWriter;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected String returnHintMessage() {
        return "Please give a name to the condition checker! e.g.:.../conditionchecker?fileName=ExternalConditionChecker.class";
    }

    @Override
    protected void writeFile(InputStream inputStream, String fileName, HttpServletRequest request) {
        fileWriter.write(inputStream, stubResourcePathProvider.getConditionCheckerPathAsString() + "/" + fileName, EXCEPTION_MESSAGE);
        LOGGER.info(urlAccessLogMessageAssembler.assembleMessage(request, "External condition checker class '" + fileName
                + "' was uploaded to Wilma."));
    }

}
