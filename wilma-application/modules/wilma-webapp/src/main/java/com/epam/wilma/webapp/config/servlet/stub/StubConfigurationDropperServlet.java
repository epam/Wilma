package com.epam.wilma.webapp.config.servlet.stub;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.service.StubConfigurationDropperService;

/**
 * This servlet calls the right service what will drop the selected stub configuration.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationDropperServlet extends HttpServlet {

    private final StubConfigurationDropperService stubConfigurationDropperService;

    /**
     * Constructor using spring framework to initialize the class.
     * @param stubConfigurationDropperService provides access to service that can drop a stub configuration
     */
    @Autowired
    public StubConfigurationDropperServlet(StubConfigurationDropperService stubConfigurationDropperService) {
        this.stubConfigurationDropperService = stubConfigurationDropperService;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String groupName = request.getParameter("groupname");
        try {
            stubConfigurationDropperService.dropSelectedStubConfiguration(groupName, request);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
