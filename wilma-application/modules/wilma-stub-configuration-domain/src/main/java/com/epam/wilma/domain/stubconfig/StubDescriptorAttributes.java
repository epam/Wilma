package com.epam.wilma.domain.stubconfig;
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
 * Represents the attributes needed by a {@link StubDescriptor}.
 * @author Tibor_Kovacs
 *
 */
public class StubDescriptorAttributes {
    private final String groupName;
    private boolean active = true;

    /**
     * Constructs a new instance of stub descriptor attributes.
     * @param groupName the name of that team which owns the current stub configuration
     */
    public StubDescriptorAttributes(final String groupName) {
        super();
        this.groupName = groupName;
    }

    /**
     * Constructs a new instance of stub descriptor attributes.
     * @param groupName the name of that team which owns the current stub configuration
     * @param active the enabled/disabled value of that stub descriptor which contains this object
     */
    public StubDescriptorAttributes(final String groupName, final boolean active) {
        super();
        this.groupName = groupName;
        this.active = active;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

}
