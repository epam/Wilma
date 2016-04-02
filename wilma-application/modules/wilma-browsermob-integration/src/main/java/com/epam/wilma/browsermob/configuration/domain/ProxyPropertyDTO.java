package com.epam.wilma.browsermob.configuration.domain;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
 * @author Tunde_Kovacs, Tamas Kohegyi
 *
 */
public class ProxyPropertyDTO {

    private final Integer proxyPort;
    private final Integer requestTimeout;
    private final Boolean allowResponseUpdate;
    private final Boolean shouldKeepSslConnectionAlive;

    /**
     * Constructs a new property holding object with the given fields.
     * @param proxyPort the port used by the proxy
     * @param requestTimeout the value of a request timeout in milliseconds
     * @param allowResponseUpdate whether proxy should work in mode that allows response update (slower), or not (faster)
     * @param shouldKeepSslConnectionAlive if client expects CONNECT connections kept alive or not
     */
    public ProxyPropertyDTO(final Integer proxyPort, final Integer requestTimeout, final Boolean allowResponseUpdate, Boolean shouldKeepSslConnectionAlive) {
        super();
        this.proxyPort = proxyPort;
        this.requestTimeout = requestTimeout;
        this.allowResponseUpdate = allowResponseUpdate;
        this.shouldKeepSslConnectionAlive = shouldKeepSslConnectionAlive;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public Boolean getAllowResponseUpdate() {
        return allowResponseUpdate;
    }

    public Boolean getShouldKeepSslConnectionAlive() {
        return shouldKeepSslConnectionAlive;
    }
}
