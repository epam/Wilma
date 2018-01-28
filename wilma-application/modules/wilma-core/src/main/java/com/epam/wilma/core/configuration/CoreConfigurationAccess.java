package com.epam.wilma.core.configuration;

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
import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.common.helper.SequenceHandlingState;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Provides configuration access for the module.
 * @author Tunde_Kovacs
 *
 */
@Component
public class CoreConfigurationAccess implements ConfigurationAccessBase {

    private PropertyDto properties;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        String messageLogging = propertyHolder.get("message.logging");
        String operationModeProperty = propertyHolder.get("switch");
        OperationMode operationMode = OperationMode.valueOf(operationModeProperty.toUpperCase());
        String interceptorMode = propertyHolder.get("interceptor");
        String blockLocalhostUsageProperty = propertyHolder.get("block.localhost.usage");
        String sequenceHandlingUsageProperty = propertyHolder.get("sequence.handling.state");
        BlockLocalhostUsage blockLocalhostUsage = BlockLocalhostUsage.valueOf(blockLocalhostUsageProperty.toUpperCase());
        SequenceHandlingState sequenceHandlingUsage = SequenceHandlingState.valueOf(sequenceHandlingUsageProperty.toUpperCase());
        properties = new PropertyDto.Builder().messageLogging(messageLogging).operationMode(operationMode).interceptorMode(interceptorMode)
                .blockLocalhostUsage(blockLocalhostUsage).sequenceHandlingState(sequenceHandlingUsage).build();
    }

    /**
     * Returns a {@link PropertyDto} holding all module specific properties.
     * @return the propertiesDTO object
     */
    public PropertyDto getProperties() {
        return properties;
    }

}
