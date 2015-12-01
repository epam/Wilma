package com.epam.wilma.browsermob.configuration.domain;
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
 * @author Tamas Kohegyi
 *
 */
public class MessagePropertyDTO {

    private final String instancePrefix;

    /**
     * Constructs a new property holding object with the given fields.
     * @param instancePrefix is used as Wilma instance prefix for logging the messages
     */
    public MessagePropertyDTO(String instancePrefix) {
        super();
        this.instancePrefix = instancePrefix;
    }

    public String getInstancePrefix() {
        return instancePrefix;
    }
}
