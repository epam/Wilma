package com.epam.wilma.stubconfig.configuration.domain;
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
public class PropertyDto {

    private final int maxDepthOfTree;
    private final long defaultSequenceTimeout;

    /**
     * Constructs a new property holding object with the given fields.
     * @param maxDepthOfTree  the max depth of the stub configuration
     * dialog descriptor elements (condition-set-invoker, template-formatter-set-invoker)
     * child's subtree. This is necessary because of recursive self referring.
     * @param defaultSequenceTimeout when no timeout is provided to a sequence descriptor, it will use this value
     */
    public PropertyDto(final int maxDepthOfTree, final long defaultSequenceTimeout) {
        super();
        this.defaultSequenceTimeout = defaultSequenceTimeout;
        this.maxDepthOfTree = maxDepthOfTree;
    }

    public long getDefaultSequenceTimeout() {
        return defaultSequenceTimeout;
    }

    public int getMaxDepthOfTree() {
        return maxDepthOfTree;
    }

}
