package com.epam.wilma.message.search.engine.initalize;
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
public class PropertyDto {

    private final Integer port;
    private final String messageFolders;
    private final String indexFolder;
    private final String jmsBrokerHost;
    private final Integer jmsBrokerPort;

    /**
     * Constructs a new property holding object with the given fields.
     * @param port the port used by the web application
     * @param messageFolders the comma separated list of directories that will
     * be indexed by the application
     * @param indexFolder the folder the application stores the index files
     * @param jmsBrokerHost port used to communicate with message producers via an activemq broker
     * @param jmsBrokerPort port used to communicate with message producers via an activemq broker
     */
    //CHECKSTYLE OFF - too many parameters
    public PropertyDto(final Integer port, final String messageFolders, final String indexFolder, final String jmsBrokerHost, final Integer jmsBrokerPort) {
        //CHECKSTYLE ON
        super();
        this.port = port;
        this.messageFolders = messageFolders;
        this.indexFolder = indexFolder;
        this.jmsBrokerHost = jmsBrokerHost;
        this.jmsBrokerPort = jmsBrokerPort;
    }

    public Integer getPort() {
        return port;
    }

    public String getMessageFolders() {
        return messageFolders;
    }

    public String getIndexFolder() {
        return indexFolder;
    }

    public String getJmsBrokerHost() {
        return jmsBrokerHost;
    }

    public Integer getJmsBrokerPort() {
        return jmsBrokerPort;
    }

}
