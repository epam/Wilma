package com.epam.wilma.browsermob.configuration.domain;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

/**
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class ProxyPropertyDTO {

    private final Integer proxyPort;
    private final Integer requestTimeout;

    /**
     * Constructs a new property holding object with the given fields.
     * @param proxyPort the port used by the proxy
     * @param requestTimeout the value of a request timeout in milliseconds
     */
    public ProxyPropertyDTO(final Integer proxyPort, final Integer requestTimeout) {
        super();
        this.proxyPort = proxyPort;
        this.requestTimeout = requestTimeout;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

}
