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

import com.epam.wilma.stubconfig.json.parser.helper.JsonBasedObjectTransformer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.webapp.config.servlet.stub.download.helper.ByteArrayConverter;

/**
 * Servlet for downloading and displaying the actual stub configuration JSON.
 * @author Tamas_Bihari
 *
 */
@Component
public class StubConfigHandlerServlet extends HttpServlet {

    private static final String ENCODING = "UTF-8";
    private static final String JSON = "application/json";
    private static final String HTML = "text/html";
    private static final String TEXT = "text/plain";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ERROR_MSG = "Something went wrong! The actually used configuration can not be transformed to a JSON.";

    private final JsonBasedObjectTransformer jsonBasedObjectTransformer;

    private final StubResourceHolder stubResourceHolder;
    private final ByteArrayConverter byteArrayConverter;

    /**
     * Constructor using spring framework to initialize the class.
     * @param jsonBasedObjectTransformer transfer the stub configuration into json
     * @param stubResourceHolder provides the actual stub configuration
     * @param byteArrayConverter converts the xml into a byte array
     */
    @Autowired
    public StubConfigHandlerServlet(JsonBasedObjectTransformer jsonBasedObjectTransformer, StubResourceHolder stubResourceHolder, ByteArrayConverter byteArrayConverter) {
        this.jsonBasedObjectTransformer = jsonBasedObjectTransformer;
        this.stubResourceHolder = stubResourceHolder;
        this.byteArrayConverter = byteArrayConverter;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String sourceParameter = req.getParameter("groupname");
        if (sourceParameter != null) {
            byte[] xml = getActualUsedJson(sourceParameter);
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

    private byte[] getActualUsedJson(final String groupName) {
        //get file from stubResourceHolder and transform it
        JSONObject actualObject = stubResourceHolder.getActualStubConfigJsonObject(groupName);
        byte[] json = jsonBasedObjectTransformer.transform(actualObject);
        return json;
    }

    private void setHeader(final HttpServletRequest req, final HttpServletResponse resp, final String groupname) {
        resp.setCharacterEncoding(ENCODING);
        String sourceParamerter = req.getParameter("source");
        if (sourceParamerter != null && "true".equalsIgnoreCase(sourceParamerter)) {
            resp.setContentType(JSON);
        } else {
            resp.setContentType(TEXT);
            resp.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + groupname + "StubConfig.json");
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
