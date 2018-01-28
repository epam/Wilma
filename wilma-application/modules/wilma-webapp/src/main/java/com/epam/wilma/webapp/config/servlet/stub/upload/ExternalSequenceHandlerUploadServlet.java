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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;

/**
 * Servlet used for uploading SequenceHandler classes.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ExternalSequenceHandlerUploadServlet extends CommonExternalUploadServlet {

    private static final String EXCEPTION_MESSAGE = "Could not upload external sequence handler: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalSequenceHandlerUploadServlet.class);
    private static final String SUCCESS_TEMPLATE = "External sequence handler '%s' was uploaded to Wilma.";

    private final StubResourcePathProvider stubResourcePathProvider;
    private final FileWriter fileWriter;

    /**
     * Constructor using spring framework to initialize the class.
     * @param stubResourcePathProvider provides the path to Wilma resources
     * @param fileWriter saves the uploaded resource to Wilma
     */
    @Autowired
    public ExternalSequenceHandlerUploadServlet(StubResourcePathProvider stubResourcePathProvider, FileWriter fileWriter) {
        this.stubResourcePathProvider = stubResourcePathProvider;
        this.fileWriter = fileWriter;
    }

    @Override
    protected String returnHintMessage() {
        return "Please give a name to the sequence handler! e.g.:.../sequencehandler?fileName=ExternalSequenceHandler.class";
    }

    @Override
    protected void writeFile(final InputStream inputStream, final String fileName, final HttpServletRequest request) {
        String message = String.format(SUCCESS_TEMPLATE, fileName);
        fileWriter.write(inputStream, stubResourcePathProvider.getSequenceHandlerPathAsString() + "/" + fileName, EXCEPTION_MESSAGE);
        LOGGER.info(message);
    }

}
