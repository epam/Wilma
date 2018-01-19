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

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that resolves the given IP address to its host name.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class IpAddressResolver {

    private static final String DEBUG_MESSAGE = "Host could not be resolved: ";
    private static final String UNKNOWN_HOST_TEMPLATE = "UNKNOWN HOST(%s)";
    private final Logger logger = LoggerFactory.getLogger(IpAddressResolver.class);

    @Autowired
    private InetAddressFactory inetAddressFactory;

    /**
     * Returns the host name that belongs to the given IP address.
     * @param ip the IP address to resolve
     * @return the host name or if it's not found then the String: UNKNOWN HOST(ip) , where ip is the the given ip to resolve
     */
    public String resolveToHostName(final String ip) {
        String host = "";
        try {
            InetAddress address = inetAddressFactory.createByName(ip);
            host = address.getHostName();
        } catch (UnknownHostException e) {
            logger.debug(DEBUG_MESSAGE + host, e);
            host = String.format(UNKNOWN_HOST_TEMPLATE, ip);
        }
        return host;
    }
}
