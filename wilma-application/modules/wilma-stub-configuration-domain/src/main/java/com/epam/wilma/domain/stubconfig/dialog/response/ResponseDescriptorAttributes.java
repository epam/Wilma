package com.epam.wilma.domain.stubconfig.dialog.response;
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

import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.google.common.base.Preconditions;

/**
 * Represents the attributes needed by a {@link ResponseDescriptor}.
 * @author Tunde_Kovacs
 *
 */
public final class ResponseDescriptorAttributes {

    private Integer delay;
    private String code;
    private String mimeType;
    private Template template;
    private String sequenceDescriptorKey;

    public int getDelay() {
        return delay;
    }

    public String getCode() {
        return code;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Template getTemplate() {
        return template;
    }

    public String getSequenceDescriptorKey() {
        return sequenceDescriptorKey;
    }

    /**
     * Builder for {@link ResponseDescriptorAttributes}.
     * @author Adam_Csaba_Kiraly
     *
     */
    public static class Builder {
        private Integer delay;
        private String code = "200";
        private String mimeType;
        private Template template;
        private String sequenceDescriptorKey = "";

        /**
         * Sets the delay to the given value.
         * @param delay the given value
         * @return the builder for chaining
         */
        public Builder delay(final int delay) {
            this.delay = delay;
            return this;
        }

        /**
         * Sets the code to the given value. (Optional)
         * @param code the given value
         * @return the builder for chaining
         */
        public Builder code(final String code) {
            this.code = code;
            return this;
        }

        /**
         * Sets the mimeType to the given value.
         * @param mimeType the given value
         * @return the builder for chaining
         */
        public Builder mimeType(final String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        /**
         * Sets the template to the given {@link Template}.
         * @param template the given {@link Template}
         * @return the builder for chaining
         */
        public Builder template(final Template template) {
            this.template = template;
            return this;
        }

        /**
         * Sets the sequenceDescriptorKey to the given value. (Optional)
         * @param sequenceDescriptorKey the given value
         * @return the builder for chaining
         */
        public Builder sequenceDescriptorKey(final String sequenceDescriptorKey) {
            this.sequenceDescriptorKey = sequenceDescriptorKey;
            return this;
        }

        /**
         * Builds a new {@link ResponseDescriptorAttributes} object.
         * @return the new {@link ResponseDescriptorAttributes} object.
         */
        public ResponseDescriptorAttributes build() {
            ResponseDescriptorAttributes responseDescriptorAttributes = new ResponseDescriptorAttributes();
            validateFields();
            setFields(responseDescriptorAttributes);
            return responseDescriptorAttributes;
        }

        private void setFields(final ResponseDescriptorAttributes responseDescriptorAttributes) {
            responseDescriptorAttributes.delay = delay;
            responseDescriptorAttributes.mimeType = mimeType;
            responseDescriptorAttributes.template = template;
            responseDescriptorAttributes.code = code;
            responseDescriptorAttributes.sequenceDescriptorKey = sequenceDescriptorKey;
        }

        private void validateFields() {
            Preconditions.checkNotNull(delay);
            Preconditions.checkNotNull(mimeType);
            Preconditions.checkNotNull(template);
            Preconditions.checkNotNull(code);
            Preconditions.checkNotNull(sequenceDescriptorKey);
        }
    }
}
