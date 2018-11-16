package com.epam.wilma.engine.configuration.domain;
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
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class PropertyDTO {

    private final Integer proxyPort;
    private final Integer wilmaPort;
    private final String stubConfigFolderPath;
    private final String stubConfigPattern;
    private final String stubConfigCachePath;

    /**
     * Constructs a new property holding object with the given fields.
     * @param proxyPort the port used by the proxy
     * @param wilmaPort the port used by Wilma UI
     * @param stubConfigFolderPath the path of that folder from project root, what contains stub descriptor XML configuration files
     * @param stubConfigPattern extension pattern of stub descriptor XML configuration files
     * @param stubConfigCachePath is path of the cache folder of stub configuration xml files
     */
    //CHECKSTYLE OFF
    //more than 4 input field is allowed here
    public PropertyDTO(final Integer proxyPort, final Integer wilmaPort,
                       final String stubConfigFolderPath, final String stubConfigPattern, final String stubConfigCachePath) {
        //CHECKSTYLE ON
        super();
        this.proxyPort = proxyPort;
        this.wilmaPort = wilmaPort;
        this.stubConfigFolderPath = stubConfigFolderPath;
        this.stubConfigPattern = stubConfigPattern;
        this.stubConfigCachePath = stubConfigCachePath;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public Integer getWilmaPort() {
        return wilmaPort;
    }

    public String getStubConfigFolderPath() {
        return stubConfigFolderPath;
    }

    public String getStubConfigPattern() {
        return stubConfigPattern;
    }

    public String getStubConfigCachePath() {
        return stubConfigCachePath;
    }

}
