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
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class PropertyDTO {

    private final SafeguardLimits safeguardLimits;
    private final String cronExpression;

    /**
     * Constructs a new property holding object with the given fields.
     * @param safeguardLimits the {@link SafeguardLimits} that control
     * the max number of items in a queue that logs the messages
     * @param cronExpression guard period of the overload safeguard in seconds.
     * 0 means that the safeguard is inactive, otherwise the time period can be
     * given with cron expressions (e.g.: 0/3 * * * * *).
     */
    public PropertyDTO(final SafeguardLimits safeguardLimits, final String cronExpression) {
        super();
        this.safeguardLimits = safeguardLimits;
        this.cronExpression = cronExpression;
    }

    public SafeguardLimits getSafeguardLimits() {
        return safeguardLimits;
    }

    public String getCronExpression() {
        return cronExpression;
    }
}
