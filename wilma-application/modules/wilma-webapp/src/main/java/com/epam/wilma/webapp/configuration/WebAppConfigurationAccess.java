package com.epam.wilma.webapp.configuration;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.webapp.configuration.domain.FileListJsonProperties;
import com.epam.wilma.webapp.configuration.domain.MaintainerProperties;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.Readme;
import com.epam.wilma.webapp.configuration.domain.SequenceResponseGuardProperties;
import com.epam.wilma.webapp.configuration.domain.ServerProperties;

/**
 * Provides configuration access for the module.
 * @author Tunde_Kovacs
 *
 */
@Component
public class WebAppConfigurationAccess implements ConfigurationAccessBase {

    private PropertyDTO properties;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        MaintainerProperties maintainerProperties = getMaintainerProperties();
        Readme readme = getReadme();
        ServerProperties serverProperties = getServerProperties();
        SequenceResponseGuardProperties sequenceResponseGuardProperties = getSequenceResponseGuardProperties();
        FileListJsonProperties fileListProperties = getFileListJsonProperties();
        properties = new PropertyDTO.Builder().maintainerProperties(maintainerProperties).readme(readme)
                .sequenceResponseGuardProperties(sequenceResponseGuardProperties).serverProperties(serverProperties)
                .fileListProperties(fileListProperties).build();
    }

    /**
     * Returns a {@link PropertyDTO} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public PropertyDTO getProperties() {
        return properties;
    }

    private FileListJsonProperties getFileListJsonProperties() {
        int maximumValue = propertyHolder.getInt("message.log.UI.maxsize");
        return new FileListJsonProperties(maximumValue);
    }

    private SequenceResponseGuardProperties getSequenceResponseGuardProperties() {
        int proxyTimeout = propertyHolder.getInt("proxy.request.timeout");
        int waitInterval = propertyHolder.getInt("sequence.response.wait.interval");
        return new SequenceResponseGuardProperties(proxyTimeout, waitInterval);
    }

    private ServerProperties getServerProperties() {
        int proxyPort = propertyHolder.getInt("internal.wilma.port");
        int requestBufferSize = propertyHolder.getInt("internal.wilma.request.buffer.size");
        int responseBufferSize = propertyHolder.getInt("internal.wilma.response.buffer.size");
        return new ServerProperties(proxyPort, requestBufferSize, responseBufferSize);
    }

    private Readme getReadme() {
        String readmeUrl = propertyHolder.get("wilma.readme.url");
        String readmeText = propertyHolder.get("wilma.readme.text");
        return new Readme(readmeUrl, readmeText);
    }

    private MaintainerProperties getMaintainerProperties() {
        String cronExpression = propertyHolder.get("log.maintainer.cron");
        String maintainerMethod = propertyHolder.get("log.maintainer.method");
        Integer fileLimit = propertyHolder.getInt("log.maintainer.file.limit");
        String timeLimit = propertyHolder.get("log.maintainer.time.limit");
        return new MaintainerProperties(cronExpression, maintainerMethod, fileLimit, timeLimit);
    }

}
