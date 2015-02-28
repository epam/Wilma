package com.epam.wilma.webapp.configuration.domain;

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
 * @author Tunde_Kovacs
 *
 */
public class PropertyDTO {

    private final MaintainerProperties maintainerProperties;
    private final Readme readme;
    private final ServerProperties serverProperties;
    private final SequenceResponseGuardProperties sequenceResponseGuardProperties;

    /**
     * Constructs a new dto with the given parameters.
     * @param serverProperties Properties needed by the web server
     * @param maintainerProperties Properties needed by the log maintainer
     * @param readme the url of readme and the text of the readme url
     * @param sequenceResponseGuardProperties properties needed by SequenceResponseGuardProperties
     */
    public PropertyDTO(final ServerProperties serverProperties, final MaintainerProperties maintainerProperties, final Readme readme,
            final SequenceResponseGuardProperties sequenceResponseGuardProperties) {
        this.serverProperties = serverProperties;
        this.maintainerProperties = maintainerProperties;
        this.readme = readme;
        this.sequenceResponseGuardProperties = sequenceResponseGuardProperties;
    }

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

}
