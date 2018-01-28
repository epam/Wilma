package com.epam.wilma.webapp.helper;
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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class which adds request URL and host name (or IP) info to the message.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class UrlAccessLogMessageAssembler {

    private static final String INFO_TEMPLATE = "URL: '%s', accessed from: '%s', message: '%s'";
    private static final String DENY_INFO_TEMPLATE = "Denied access to URL: '%s', from: '%s'";
    @Autowired
    private IpAddressResolver ipAddressResolver;

    /**
     * Prepends the provided request's url and the host of the requester to the message.
     * @param request the provided request where we get the info from
     * @param message the original message
     * @return the original message prepended with the request info
     */
    public String assembleMessage(HttpServletRequest request, String message) {
        String ip = request.getRemoteAddr();
        String host = ipAddressResolver.resolveToHostName(ip);
        return String.format(INFO_TEMPLATE, request.getRequestURI(), host, message);
    }

    /**
     * Returns a message for request denial logging.
     * @param request the request that was denied
     * @return message for logging with requested URL and the requestor's host.
     */
    public String assembleDenyMessage(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String host = ipAddressResolver.resolveToHostName(ip);
        return String.format(DENY_INFO_TEMPLATE, request.getRequestURI(), host);
    }

}
