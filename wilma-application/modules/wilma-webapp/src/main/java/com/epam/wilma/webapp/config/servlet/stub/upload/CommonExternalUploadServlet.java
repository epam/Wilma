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
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;

/**
 * Abstract class implementing the common functionality of the various External...UploadServlets.
 * @author Adam_Csaba_Kiraly
 *
 */
public abstract class CommonExternalUploadServlet extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String fileName = request.getParameter("fileName");
        if (fileName == null) {
            PrintWriter writer = response.getWriter();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.write(returnHintMessage());
        } else {
            try {
                writeFile(inputStream, fileName, request);
            } catch (CannotUploadExternalResourceException e) {
                PrintWriter writer = response.getWriter();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.write(e.getMessage());
            }
        }
    }

    /**
     * Returns a message for helping the user in case of no file name given.
     * @return the message to be written out
     */
    protected abstract String returnHintMessage();

    /**
     * Handles the writing of the given inputStream, and logs it.
     * @param inputStream the file as InputStream
     * @param fileName the name of the file
     * @param request the current request, used to determine the host and requested url for the logging
     */
    protected abstract void writeFile(InputStream inputStream, String fileName, HttpServletRequest request);

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
