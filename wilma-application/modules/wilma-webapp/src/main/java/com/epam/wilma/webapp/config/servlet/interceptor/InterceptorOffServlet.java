package com.epam.wilma.webapp.config.servlet.interceptor;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.toggle.interceptor.InterceptorModeToggle;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * This servlet that is used to switch off message logging when called.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class InterceptorOffServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(InterceptorOffServlet.class);

    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    private final InterceptorModeToggle interceptorModeToggle;

    /**
     * Constructor using spring framework to initialize the class.
     * @param urlAccessLogMessageAssembler is the logger used during url access
     * @param interceptorModeToggle is used to toggle the interceptor mode
     */
    @Autowired
    public InterceptorOffServlet(UrlAccessLogMessageAssembler urlAccessLogMessageAssembler, InterceptorModeToggle interceptorModeToggle) {
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
        this.interceptorModeToggle = interceptorModeToggle;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.info(urlAccessLogMessageAssembler.assembleMessage(req, "Interceptor usage: OFF"));
        interceptorModeToggle.switchOffInterceptors();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
