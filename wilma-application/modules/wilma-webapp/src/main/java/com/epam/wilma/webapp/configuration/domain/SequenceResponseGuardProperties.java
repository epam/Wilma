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
 * The properties necessary for SequenceResponseGuard.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceResponseGuardProperties {

    private final int timeout;
    private final int waitInterval;

    /**
     * Constructs a new instance of {@link SequenceResponseGuardProperties}.
     * @param timeout the timeout value, should be the one used by the proxy
     * @param waitInterval the interval of waiting
     */
    public SequenceResponseGuardProperties(final int timeout, final int waitInterval) {
        this.timeout = timeout;
        this.waitInterval = waitInterval;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getWaitInterval() {
        return waitInterval;
    }
}
