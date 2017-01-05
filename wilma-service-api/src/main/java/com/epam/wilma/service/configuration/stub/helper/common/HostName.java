package com.epam.wilma.service.configuration.stub.helper.common;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

/**
 * Determines the actual host name.
 * Created by Tamas_Kohegyi on 2016-02-15.
 */
public class HostName {

    /**
     * Get the hostname of the machine.
     *
     * @return with the hostname
     */
    public String getHostName() {
        // try InetAddress.LocalHost first;
        // NOTE -- InetAddress.getLocalHost().getHostName() will not work in certain environments.
        String result;
        try {
            result = InetAddress.getLocalHost().getHostName();
            if (result == null || result.length() == 0) {
                throw new UnknownHostException();
            }
        } catch (UnknownHostException e) {
            // getHostName() failed;  try environment properties.
            result = System.getenv("COMPUTERNAME"); //win path
            if (result == null) {
                result = System.getenv("HOSTNAME"); //*nix path
            }
        }
        return result;
    }
}
