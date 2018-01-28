package com.epam.wilma.webapp.config.servlet.stub.helper;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CurrentDateProvider;

/**
 * Provides expiration time when a DialogDescriptor's Usage is set
 * to TIMEOUT.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ExpirationTimeProvider {

    private static final long MINUTE_DIVIDER = 60L;
    private static final long SECOND_DIVIDER = 1000L;

    @Autowired
    private CurrentDateProvider currentDateProvider;

    /**
     * Returns the expiration time of a DialogDescriptor in minutes.
     * @param validityValue the expiration time in milliseconds
     * @return the expiration time in minutes
     */
    public long getExpirationMinutes(final long validityValue) {
        return ((validityValue - currentDateProvider.getCurrentTimeInMillis()) / SECOND_DIVIDER) / MINUTE_DIVIDER;
    }

    /**
     * Returns the seconds left within a minute until a DialogDescriptor expires.
     * @param validityValue the expiration time in milliseconds
     * @return the expiration seconds within the minute
     */
    public long getExpirationSeconds(final long validityValue) {
        return ((validityValue - currentDateProvider.getCurrentTimeInMillis()) / SECOND_DIVIDER) % MINUTE_DIVIDER;
    }

}
