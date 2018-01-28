package com.epam.wilma.router.helper;
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

import java.net.URI;

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Used to check if a request is directed to localhost.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class LocalhostRequestChecker {

    /**
     * Determines if the given request tries to access a localhost address.
     * @param request the given request
     * @return true if it targets localhost, false otherwise
     */
    public boolean checkIfRequestTargetsLocalhost(final WilmaHttpRequest request) {
        URI requestUri = request.getUri();
        return isLocalhost(requestUri);
    }

    private boolean isLocalhost(final URI uri) {
        String host = uri.getHost();
        return "localhost".equals(host) || "127.0.0.1".equals(host);
    }

}
