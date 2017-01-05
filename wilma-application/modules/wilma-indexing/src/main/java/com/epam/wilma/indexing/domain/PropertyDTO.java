package com.epam.wilma.indexing.domain;
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

/**
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class PropertyDTO {

    private final String brokerHost;
    private final Integer brokerPort;

    /**
     * Constructs a new property holding object with the given fields.
     * @param brokerHost is the host used to communicate with the indexing application via an activemq broker
     * @param brokerPort is the port used to communicate with the indexing application via an activemq broker
     */
    public PropertyDTO(final String brokerHost, final Integer brokerPort) {
        super();
        this.brokerHost = brokerHost;
        this.brokerPort = brokerPort;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

}
