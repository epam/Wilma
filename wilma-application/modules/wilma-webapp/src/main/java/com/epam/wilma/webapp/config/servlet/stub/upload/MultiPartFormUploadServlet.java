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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.config.servlet.stub.upload.helper.ServletFileUploadFactory;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Servlet for uploading external configuration file via UI.
 * @author Tamas_Bihari
 *
 */
@Component
public class MultiPartFormUploadServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiPartFormUploadServlet.class);

    private final ServletFileUpload servletFileUpload;
    private final MultiPartFileParser multiPartFileParser;
    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    /**
     * Creates a new {@link MultiPartFormUploadServlet} instance using parameters.
     * @param servletFileUploadFactory is necessary to create a new {@link ServletFileUpload} instance
     * @param multiPartFileParser is the list of uploaded multipart files
     * @param urlAccessLogMessageAssembler is used to log url access event
     */
    @Autowired
    public MultiPartFormUploadServlet(final ServletFileUploadFactory servletFileUploadFactory, final MultiPartFileParser multiPartFileParser,
                                      final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler) {
        this.servletFileUpload = servletFileUploadFactory.createInstance();
        this.multiPartFileParser = multiPartFileParser;
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            out.println("You are not trying to upload");
        } else {
            try {
                List<FileItem> fields = servletFileUpload.parseRequest(request);
                String msg = multiPartFileParser.parseMultiPartFiles(fields);
                LOGGER.info(urlAccessLogMessageAssembler.assembleMessage(request, msg));
                out.write(msg);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("File uploading failed! cause: " + e.getMessage());
                LOGGER.info(urlAccessLogMessageAssembler.assembleMessage(request, e.getMessage()), e);
            }
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
