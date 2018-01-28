package com.epam.wilma.webapp.config.servlet.stub.download;

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
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.stubconfig.dom.transformer.DomBasedDocumentTransformer;
import com.epam.wilma.domain.stubconfig.exception.DocumentTransformationException;
import com.epam.wilma.webapp.config.servlet.stub.download.helper.ByteArrayConverter;

/**
 * Servlet for downloading and displaying the actual stub configuration XML.
 * @author Tamas_Bihari
 *
 */
@Component
public class StubConfigHandlerServlet extends HttpServlet {

    private static final String ENCODING = "UTF-8";
    private static final String XML = "application/xml";
    private static final String HTML = "text/html";
    private static final String TEXT = "text/plain";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ERROR_MSG = "Something went wrong! The actually used configuration can not be transformed to an XML.";

    private final Logger logger = LoggerFactory.getLogger(StubConfigHandlerServlet.class);

    private final DomBasedDocumentTransformer domBasedDocumentTransformer;
    private final StubResourceHolder stubResourceHolder;
    private final ByteArrayConverter byteArrayConverter;

    /**
     * Constructor using spring framework to initialize the class.
     * @param domBasedDocumentTransformer transfer the stub configuration into xml
     * @param stubResourceHolder provides the actual stub configuration
     * @param byteArrayConverter converts the xml into a byte array
     */
    @Autowired
    public StubConfigHandlerServlet(DomBasedDocumentTransformer domBasedDocumentTransformer, StubResourceHolder stubResourceHolder, ByteArrayConverter byteArrayConverter) {
        this.domBasedDocumentTransformer = domBasedDocumentTransformer;
        this.stubResourceHolder = stubResourceHolder;
        this.byteArrayConverter = byteArrayConverter;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String sourceParameter = req.getParameter("groupname");
        if (sourceParameter != null) {
            byte[] xml = getActualUsedXMLDocument(sourceParameter);
            if (xml != null) {
                setHeader(req, resp, sourceParameter);
                writeStubConfigToResponse(req, resp, xml);
            } else {
                writeErrorToResponse(resp);
            }
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private byte[] getActualUsedXMLDocument(final String groupName) {
        //get file from stubResourceHOlder and transform it
        Document actualDocument = stubResourceHolder.getActualStubConfigDocument(groupName);
        byte[] xml;
        try {
            xml = domBasedDocumentTransformer.transform(actualDocument);
        } catch (DocumentTransformationException e) {
            logger.debug(ERROR_MSG, e);
            xml = null;
        }
        return xml;
    }

    private void setHeader(final HttpServletRequest req, final HttpServletResponse resp, final String groupname) {
        resp.setCharacterEncoding(ENCODING);
        String sourceParamerter = req.getParameter("source");
        if (sourceParamerter != null && "true".equalsIgnoreCase(sourceParamerter)) {
            resp.setContentType(XML);
        } else {
            resp.setContentType(TEXT);
            resp.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + groupname + "StubConfig.xml");
        }
    }

    private void writeStubConfigToResponse(final HttpServletRequest req, final HttpServletResponse resp, final byte[] xml) throws IOException {
        PrintWriter out = resp.getWriter();
        String fileContent = byteArrayConverter.toString(xml);
        String userAgent = req.getHeader("User-Agent");
        if (userIsOnWindows(userAgent)) {
            fileContent = fileContent.replace("\r", "").replace("\n", "\r\n");
        }
        out.write(fileContent);
        out.flush();
        out.close();

    }

    private void writeErrorToResponse(final HttpServletResponse resp) throws IOException {
        resp.setContentType(HTML);
        PrintWriter out = resp.getWriter();
        out.write(ERROR_MSG);
        out.flush();
        out.close();
    }

    private boolean userIsOnWindows(final String userAgent) {
        return userAgent != null && userAgent.toLowerCase().contains("windows");
    }
}
