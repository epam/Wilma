package com.epam.wilma.service.domain;

/*==========================================================================
 Copyright 2015 EPAM Systems

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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Holds mock client specific configuration.
 *
 * @author Tamas_Pinter
 *
 */
public final class WilmaServiceConfig {

    private String host;
    private Integer port;

    private WilmaServiceConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(host, port);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof WilmaServiceConfig) {
            WilmaServiceConfig that = (WilmaServiceConfig) object;
            return Objects.equal(this.host, that.host)
                    && Objects.equal(this.port, that.port);
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("host", host)
                .add("port", port)
                .toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Builder for {@link WilmaServiceConfig} object.
     *
     * @author Tamas_Pinter
     *
     */
    public static class Builder {

        private String host;
        private Integer port;

        /**
         * Sets the host value.
         *
         * @param host the host of Wilma server
         * @return the {@link Builder} for chaining
         */
        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        /**
         * Sets the port value.
         *
         * @param port the port of Wilma server
         * @return the {@link Builder} for chaining
         */
        public Builder withPort(Integer port) {
            this.port = port;
            return this;
        }

        /**
         * Constructs a new object.
         *
         * @return the new {@link WilmaServiceConfig} instance.
         */
        public WilmaServiceConfig build() {
            return new WilmaServiceConfig(this);
        }
    }

}
