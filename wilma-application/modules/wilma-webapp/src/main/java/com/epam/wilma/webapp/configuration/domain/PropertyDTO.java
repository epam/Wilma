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
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class PropertyDTO {

    private MaintainerProperties maintainerProperties;
    private Readme readme;
    private ServerProperties serverProperties;
    private SequenceResponseGuardProperties sequenceResponseGuardProperties;
    private FileListJsonProperties fileListProperties;

    public SequenceResponseGuardProperties getSequenceResponseGuardProperties() {
        return sequenceResponseGuardProperties;
    }

    public ServerProperties getServerProperties() {
        return serverProperties;
    }

    public MaintainerProperties getMaintainerProperties() {
        return maintainerProperties;
    }

    public Readme getReadme() {
        return readme;
    }

    public FileListJsonProperties getFileListProperties() {
        return fileListProperties;
    }

    /**
     * Builder for {@link PropertyDTO} object.
     * @author Tibor_Kovacs
     *
     */
    public static class Builder {
        private MaintainerProperties maintainerProperties;
        private Readme readme;
        private ServerProperties serverProperties;
        private SequenceResponseGuardProperties sequenceResponseGuardProperties;
        private FileListJsonProperties fileListProperties;

        /**
         * Sets the maintainerProperties value.
         * @param maintainerProperties holds properties for maintainer.
         * @return the {@link Builder} for chaining
         */
        public Builder maintainerProperties(final MaintainerProperties maintainerProperties) {
            this.maintainerProperties = maintainerProperties;
            return this;
        }

        /**
         * Sets the readme value.
         * @param readme holds properties for readme.
         * @return the {@link Builder} for chaining
         */
        public Builder readme(final Readme readme) {
            this.readme = readme;
            return this;
        }

        /**
         * Sets the serverProperties value.
         * @param serverProperties holds properties for server.
         * @return the {@link Builder} for chaining
         */
        public Builder serverProperties(final ServerProperties serverProperties) {
            this.serverProperties = serverProperties;
            return this;
        }

        /**
         * Sets the sequenceResponseGuardProperties value.
         * @param sequenceResponseGuardProperties holds properties for SequenceResponseGuard.
         * @return the {@link Builder} for chaining
         */
        public Builder sequenceResponseGuardProperties(final SequenceResponseGuardProperties sequenceResponseGuardProperties) {
            this.sequenceResponseGuardProperties = sequenceResponseGuardProperties;
            return this;
        }

        /**
         * Sets the fileListProperties value.
         * @param fileListProperties holds properties for FileListJsonBuilder.
         * @return the {@link Builder} for chaining
         */
        public Builder fileListProperties(final FileListJsonProperties fileListProperties) {
            this.fileListProperties = fileListProperties;
            return this;
        }

        /**
         * Constructs a new property holding object.
         * @return the new {@link PropertyDTO} instance.
         */
        public PropertyDTO build() {
            PropertyDTO propertyDTO = new PropertyDTO();
            setFields(propertyDTO);
            return propertyDTO;
        }

        private void setFields(final PropertyDTO propertyDTO) {
            propertyDTO.maintainerProperties = maintainerProperties;
            propertyDTO.readme = readme;
            propertyDTO.serverProperties = serverProperties;
            propertyDTO.sequenceResponseGuardProperties = sequenceResponseGuardProperties;
            propertyDTO.fileListProperties = fileListProperties;
        }
    }

}
