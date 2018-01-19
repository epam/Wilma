package com.epam.wilma.test.server;
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
 * Starts the test server. The server can listen on a configurable port and answer to certain requests.
 * @author Marton_Sereg
 *
 */
public final class WilmaTestServerApplication {

    private WilmaTestServerApplication() {
    }

    /**
     * Starts the test server.
     * @param args the program arguments. The properties file location is required.
     * @throws Exception
     */
    public static void main(final String[] args) {
        new TestServerBootstrap().bootstrap(args, new Properties(), new JettyServer());
    }
}
