package com.epam.wilma.webapp.config.servlet.stub.resource;
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
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.webapp.config.servlet.helper.FileListJsonBuilder;
import com.epam.wilma.webapp.config.servlet.stub.helper.ResourceFileNameHandler;

/**
 * This servlet reads all the names of the stub resources and returns them in json.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StubResourceHandlerServlet extends HttpServlet {

    private static final String TEMPLATE = "template";
    private static final String INTERCEPTOR = "interceptor";
    private static final String CONDITION_CHECKER = "condition-checker";
    private static final String RESPONSE_FORMATTER = "response-formatter";
    private static final String SEQUENCE_HANDLER = "sequence-handler";
    private static final String NO_TYPE_ERR_MSG = "Please provide a type parameter in the url!";
    private static final String APPLICATION_JSON = "application/json";
    private static final String JAR = "jar";
    private static final String HTML = "text/html";
    private static final String ERROR_MESSAGE_TEMPLATE = "Invalid type %s ! Valid types are: %s, %s, %s, %s, %s and %s!";

    private final FileListJsonBuilder fileListJsonBuilder;
    private final StubResourcePathProvider stubResourcePathProvider;
    private final ResourceFileNameHandler resourceFileNameHandler;

    /**
     * Constructor using spring framework to initialize the class.
     * @param fileListJsonBuilder generates the file list in json format
     * @param stubResourcePathProvider provides the path to the stub resource files
     * @param resourceFileNameHandler provides object that handles the resource file names
     */
    @Autowired
    public StubResourceHandlerServlet(FileListJsonBuilder fileListJsonBuilder, StubResourcePathProvider stubResourcePathProvider, ResourceFileNameHandler resourceFileNameHandler) {
        this.fileListJsonBuilder = fileListJsonBuilder;
        this.stubResourcePathProvider = stubResourcePathProvider;
        this.resourceFileNameHandler = resourceFileNameHandler;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        if (type != null) {
            Path path = getResourcePath(type);
            if (path != null) {
                writeResponse(req, resp, path);
            } else {
                writeError(resp, String.format(ERROR_MESSAGE_TEMPLATE, type, INTERCEPTOR, CONDITION_CHECKER, RESPONSE_FORMATTER, TEMPLATE, JAR,
                        SEQUENCE_HANDLER));
            }
        } else {
            writeError(resp, NO_TYPE_ERR_MSG);
        }
    }

    private void writeResponse(final HttpServletRequest req, final HttpServletResponse resp, final Path path) throws IOException {
        if (pathIsEmpty(req)) {
            writeFileNamesToResponse(resp, path);
        } else {
            resourceFileNameHandler.writeResourceFile(resp, req.getPathInfo(), path);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private Path getResourcePath(final String type) {
        Path path = null;
        if (CONDITION_CHECKER.equals(type)) {
            path = stubResourcePathProvider.getConditionCheckerPath();
        } else if (RESPONSE_FORMATTER.equals(type)) {
            path = stubResourcePathProvider.getResponseFormatterPath();
        } else if (TEMPLATE.equals(type)) {
            path = stubResourcePathProvider.getTemplatesPath();
        } else if (INTERCEPTOR.equals(type)) {
            path = stubResourcePathProvider.getInterceptorPath();
        } else if (JAR.equals(type)) {
            path = stubResourcePathProvider.getJarPath();
        } else if (SEQUENCE_HANDLER.equals(type)) {
            path = stubResourcePathProvider.getSequenceHandlerPath();
        }
        return path;
    }

    private boolean pathIsEmpty(final HttpServletRequest req) {
        return req.getPathInfo() == null;
    }

    private void writeFileNamesToResponse(final HttpServletResponse resp, final Path path) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType(APPLICATION_JSON);
        out.write(fileListJsonBuilder.buildFileListJson(path.toFile()));
        out.flush();
        out.close();
    }

    private void writeError(final HttpServletResponse resp, final String message) throws IOException {
        resp.setContentType(HTML);
        PrintWriter out = resp.getWriter();
        out.write(message);
        out.flush();
        out.close();
    }
}
