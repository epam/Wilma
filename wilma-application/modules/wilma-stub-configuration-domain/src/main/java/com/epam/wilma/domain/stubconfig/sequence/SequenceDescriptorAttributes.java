package com.epam.wilma.domain.stubconfig.sequence;
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

import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Represents the attributes needed by a {@link SequenceDescriptor}.
 * @author Tibor_Kovacs
 *
 */
public final class SequenceDescriptorAttributes {

    /**
     * Builder for {@link SequenceDescriptorAttributes}.
     * @author Tibor_Kovacs
     *
     */
    public static class Builder {
        private SequenceHandler handler;
        private String name;
        private String groupname;
        private long defaultTimeout;
        private ParameterList parameterList;

        /**
         * Sets the handler to the given value.
         * @param handler the given value
         * @return the builder for chaining
         */
        public Builder handler(final SequenceHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Sets the name to the given value.
         * @param name the given value
         * @return the builder for chaining
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the groupname to the given value.
         * @param groupname the given value
         * @return the builder for chaining
         */
        public Builder groupname(final String groupname) {
            this.groupname = groupname;
            return this;
        }

        /**
         * Sets the defaultTimeout to the given value.
         * @param defaultTimeout the given value
         * @return the builder for chaining
         */
        public Builder defaultTimeout(final long defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
            return this;
        }

        /**
         * Sets the parameterList to the given value.
         * @param parameterList the given value
         * @return the builder for chaining
         */
        public Builder parameterList(final ParameterList parameterList) {
            this.parameterList = parameterList;
            return this;
        }

        /**
         * Builds a new {@link SequenceDescriptorAttributes} object.
         * @return the new {@link SequenceDescriptorAttributes} object.
         */
        public SequenceDescriptorAttributes build() {
            SequenceDescriptorAttributes sequenceDescriptorAttributes = new SequenceDescriptorAttributes(handler, name, groupname, defaultTimeout);
            sequenceDescriptorAttributes.parameterList = parameterList;
            return sequenceDescriptorAttributes;
        }
    }

    private final SequenceHandler handler;
    private final String name;
    private final String groupname;
    private final long defaultTimeout;
    private boolean active = true;
    private ParameterList parameterList;

    private SequenceDescriptorAttributes(final SequenceHandler handler, final String name, final String groupname, final long timeout) {
        this.handler = handler;
        this.name = name;
        this.groupname = groupname;
        this.defaultTimeout = timeout;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public SequenceHandler getHandler() {
        return handler;
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupname;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public ParameterList getParameters() {
        return parameterList;
    }

}
