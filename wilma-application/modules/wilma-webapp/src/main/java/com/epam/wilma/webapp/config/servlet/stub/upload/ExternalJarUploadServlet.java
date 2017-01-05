package com.epam.wilma.webapp.config.servlet.stub.upload;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Servlet for uploading an external jar file.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class ExternalJarUploadServlet extends CommonExternalUploadServlet {

    private static final String EXCEPTION_MESSAGE = "Could not upload external jar: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalJarUploadServlet.class);

    @Autowired
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private FileWriter fileWriter;

    @Override
    protected String returnHintMessage() {
        return "Please give a name to the jar file! e.g.:.../jar?fileName=external.jar";
    }

    @Override
    protected void writeFile(InputStream inputStream, String fileName, HttpServletRequest request) {
        fileWriter.write(inputStream, stubResourcePathProvider.getJarPathAsString() + "/" + fileName, EXCEPTION_MESSAGE);
        LOGGER.info(urlAccessLogMessageAssembler.assembleMessage(request, "External jar '" + fileName + "' was uploaded to Wilma."));
    }

}
