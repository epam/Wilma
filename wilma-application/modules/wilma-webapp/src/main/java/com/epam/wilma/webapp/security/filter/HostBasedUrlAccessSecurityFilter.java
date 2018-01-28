package com.epam.wilma.webapp.security.filter;
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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.security.HostValidatorService;

/**
 * Filter that prevents unauthorized hosts from accessing specific URLs.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class HostBasedUrlAccessSecurityFilter implements Filter {

    private static final String NO_ACCESS_MESSAGE = "You don't have the necessary rights.";
    private final Logger logger = LoggerFactory.getLogger(HostBasedUrlAccessSecurityFilter.class);

    @Autowired
    private HostValidatorService hostValidatorService;
    @Autowired
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (hostValidatorService.isRequestFromAdmin(request)) {
            chain.doFilter(request, response);
        } else {
            if (request instanceof HttpServletRequest) {
                logger.info(urlAccessLogMessageAssembler.assembleDenyMessage((HttpServletRequest) request));
            }
            sendBackNoAccessResponse(response);
        }
    }

    private void sendBackNoAccessResponse(final ServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.setContentType("text/plain");
        }
        printWriter.write(NO_ACCESS_MESSAGE);
        printWriter.flush();
        printWriter.close();
    }

    @Override
    public void destroy() {
    }

}
