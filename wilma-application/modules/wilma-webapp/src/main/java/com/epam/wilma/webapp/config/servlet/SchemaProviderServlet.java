package com.epam.wilma.webapp.config.servlet;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.config.servlet.helper.BufferedReaderFactory;

/**
 * Provides schema definition file for the stub configuration xml.
 * @author Tunde_Kovacs
 *
 */
@Component
public class SchemaProviderServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(SchemaProviderServlet.class);

    @Qualifier("stubConfigSchemaLocation")
    private final String stubConfigSchemaLocation;
    private final BufferedReaderFactory bufferedReaderFactory;

    /**
     * Constructor using spring framework to initialize the class.
     * @param stubConfigSchemaLocation provides the stub configuration schema location
     * @param bufferedReaderFactory is able to read the schema
     */
    @Autowired
    public SchemaProviderServlet(String stubConfigSchemaLocation, BufferedReaderFactory bufferedReaderFactory) {
        this.stubConfigSchemaLocation = stubConfigSchemaLocation;
        this.bufferedReaderFactory = bufferedReaderFactory;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/xml");
        writeSchemaFile(out);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void writeSchemaFile(final PrintWriter out) {
        try (BufferedReader bufferedReader = bufferedReaderFactory.createBufferedReader(stubConfigSchemaLocation)) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                out.write(currentLine);
            }
        } catch (IOException e) {
            logger.error(stubConfigSchemaLocation + " could not be read!", e);
        }
    }
}
