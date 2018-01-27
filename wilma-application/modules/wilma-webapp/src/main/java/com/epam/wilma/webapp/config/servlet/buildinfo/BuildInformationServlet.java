package com.epam.wilma.webapp.config.servlet.buildinfo;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.VersionTitleProvider;

/**
 * Servlet class for getting build information.
 * @author Tamas_Bihari
 *
 */
@Component
public class BuildInformationServlet extends HttpServlet {

    private final String wilmaBuildInformation;

    /**
     * Constructor using spring framework to initialize the class.
     * @param versionTitleProvider is the version title provider object
     */
    @Autowired
    public BuildInformationServlet(VersionTitleProvider versionTitleProvider) {
        this.wilmaBuildInformation = versionTitleProvider.getVersionTitle();
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write("{\"wilmaVersion\":\"" + wilmaBuildInformation + "\"}");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
