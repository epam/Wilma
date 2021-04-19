package com.epam.gepard.common.threads.helper;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Factory class for {@link ServerSocket}.
 * @author Adam_Csaba_Kiraly
 */
public class ServerSocketFactory {

    /**
     * Creates a {@link ServerSocket} bound to the specific port.
     * @param port the port to bind to
     * @return a new instance of {@link ServerSocket}
     * @throws IOException if an IO error occurs when opening the socket
     */
    public ServerSocket create(final int port) throws IOException {
        return new ServerSocket(port);
    }
}
