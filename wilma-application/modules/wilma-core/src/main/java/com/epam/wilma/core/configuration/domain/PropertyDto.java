package com.epam.wilma.core.configuration.domain;
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

import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.common.helper.SequenceHandlingState;
import com.google.common.base.Preconditions;

/**
 * Holds module specific properties.
 * @author Tunde_Kovacs
 * @author Tamas_Kohegyi
 *
 */
public final class PropertyDto {

    private String messageLogging;
    private OperationMode operationMode;
    private String interceptorMode;
    private BlockLocalhostUsage blockLocalhostUsage;
    private SequenceHandlingState sequenceHandlingState;

    public SequenceHandlingState getSequenceHandlingUsage() {
        return sequenceHandlingState;
    }

    public BlockLocalhostUsage getBlockLocalhostUsage() {
        return blockLocalhostUsage;
    }

    public String getMessageLogging() {
        return messageLogging;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }

    public String getInterceptorMode() {
        return interceptorMode;
    }

    /**
     * Builder for {@link PropertyDto} object.
     * @author Adam_Csaba_Kiraly
     *
     */
    public static class Builder {
        private String messageLogging;
        private OperationMode operationMode;
        private String interceptorMode;
        private BlockLocalhostUsage blockLocalhostUsage;
        private SequenceHandlingState sequenceHandlingState;

        /**
         * Sets the messageLogging value.
         * @param messageLogging turns message logging on/off when Wilma starts
         * @return the {@link Builder} for chaining
         */
        public Builder messageLogging(final String messageLogging) {
            this.messageLogging = messageLogging;
            return this;
        }

        /**
         * Sets the operationMode value.
         * @param operationMode  switch between the following operation modes:
         * proxy mode, stub mode and normal mode (valid inputs are: stub, proxy, wilma)
         * @return the {@link Builder} for chaining
         */
        public Builder operationMode(final OperationMode operationMode) {
            this.operationMode = operationMode;
            return this;
        }

        /**
         * Sets the interceptorMode value.
         * @param interceptorMode enable/disable MVT - Interceptor.
         * @return the {@link Builder} for chaining
         */
        public Builder interceptorMode(final String interceptorMode) {
            this.interceptorMode = interceptorMode;
            return this;
        }

        /**
         * Sets the blockLocalhostUsage value.
         * @param blockLocalhostUsage  enable/disable blocking requests directed to localhost
         * @return the {@link Builder} for chaining
         */
        public Builder blockLocalhostUsage(final BlockLocalhostUsage blockLocalhostUsage) {
            this.blockLocalhostUsage = blockLocalhostUsage;
            return this;
        }

        /**
         * Sets the sequenceHandlingUsage value.
         * @param sequenceHandlingState enable/disable sequence handling
         * @return the {@link Builder} for chaining
         */
        public Builder sequenceHandlingState(final SequenceHandlingState sequenceHandlingState) {
            this.sequenceHandlingState = sequenceHandlingState;
            return this;
        }

        /**
         * Constructs a new property holding object.
         * @return the new {@link PropertyDto} instance.
         */
        public PropertyDto build() {
            PropertyDto propertyDto = new PropertyDto();
            validateFields();
            setFields(propertyDto);
            return propertyDto;
        }

        private void setFields(final PropertyDto propertyDto) {
            propertyDto.messageLogging = messageLogging;
            propertyDto.operationMode = operationMode;
            propertyDto.interceptorMode = interceptorMode;
            propertyDto.blockLocalhostUsage = blockLocalhostUsage;
            propertyDto.sequenceHandlingState = sequenceHandlingState;
        }

        private void validateFields() {
            Preconditions.checkNotNull(operationMode);
            Preconditions.checkNotNull(blockLocalhostUsage);
            Preconditions.checkNotNull(sequenceHandlingState);
        }
    }

}
