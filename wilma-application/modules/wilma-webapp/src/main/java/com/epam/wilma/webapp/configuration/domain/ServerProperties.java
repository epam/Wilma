package com.epam.wilma.webapp.configuration.domain;
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

/**
 * Holds the properties required for the web server.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ServerProperties {

    private final int proxyPort;

    private final int requestBufferSize;
    private final int responseBufferSize;

    /**
     * Constructor for {@link ServerProperties}.
     * @param proxyPort the internal port of the web application
     * @param requestBufferSize the buffer size (in bytes) for receiving requests
     * @param responseBufferSize the buffer size (in bytes) for sending responses
     */
    public ServerProperties(final int proxyPort, final int requestBufferSize, final int responseBufferSize) {
        this.proxyPort = proxyPort;
        this.requestBufferSize = requestBufferSize;
        this.responseBufferSize = responseBufferSize;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public int getRequestBufferSize() {
        return requestBufferSize;
    }

    public int getResponseBufferSize() {
        return responseBufferSize;
    }

}
