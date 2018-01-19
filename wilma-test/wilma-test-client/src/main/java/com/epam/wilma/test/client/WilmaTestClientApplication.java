package com.epam.wilma.test.client;
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

import java.util.Properties;

/**
 * Starts the test client application. It is able to send an HTTP request to a configured address through a configured proxy.
 * It needs 2 program arguments: a properties file, where the server URL and the proxy address is configured, and the path of an XML file that will be sent in the request body.
 * @author Marton_Sereg
 *
 */
public final class WilmaTestClientApplication {

    private WilmaTestClientApplication() {
    }

    /**
     * Starts the test client application.
     * @param args the program arguments
     */
    public static void main(final String[] args) {
        new TestClientBootstrap().bootstrap(args, new Properties(), new HttpRequestSender());
    }

}
