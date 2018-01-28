package com.epam.wilma.safeguard.configuration.domain;
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
 * Provides safeguard limits.
 * @author Tunde_Kovacs
 *
 */
public class SafeguardLimits {

    private final Long fiOffLimit;
    private final Long fiOnLimit;
    private final Long mwOffLimit;
    private final Long mwOnLimit;
    private final String jmxPort;

    /**
     * Constructs an objects holding all the safeguard limits.
     * @param fiOffLimit specifies limit when fastinfoset decompression is turned off
     * @param fiOnLimit specifies limit when fastinfoset decompression is turned on
     * @param mwOffLimit specifies limit when message logging is turned off
     * @param mwOnLimit specifies limit when message logging is turned on
     * @param jmxPort specifies the jmx port to be used to connect to jmx server
     */
    //CHECKSTYLE_OFF - we need to have 5 params accepted
    public SafeguardLimits(final Long fiOffLimit, final Long fiOnLimit, final Long mwOffLimit, final Long mwOnLimit, final String jmxPort) {
        //CHECKSTYLE_ON
        super();
        this.fiOffLimit = fiOffLimit;
        this.fiOnLimit = fiOnLimit;
        this.mwOffLimit = mwOffLimit;
        this.mwOnLimit = mwOnLimit;
        this.jmxPort = jmxPort;
    }

    public Long getFiOffLimit() {
        return fiOffLimit;
    }

    public Long getFiOnLimit() {
        return fiOnLimit;
    }

    public Long getMwOffLimit() {
        return mwOffLimit;
    }

    public Long getMwOnLimit() {
        return mwOnLimit;
    }

    public String getJmxPort() {
        return jmxPort;
    }

    /**
     * Checks fields for null value.
     * @return with true if null field exists else with false
     */
    public boolean hasNullField() {
        return fiOffLimit == null || fiOnLimit == null || mwOffLimit == null || mwOnLimit == null;
    }

}
