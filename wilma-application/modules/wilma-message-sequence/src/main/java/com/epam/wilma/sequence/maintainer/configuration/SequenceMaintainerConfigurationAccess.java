package com.epam.wilma.sequence.maintainer.configuration;

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

import com.epam.wilma.common.configuration.ConfigurationAccessBase;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.sequence.maintainer.configuration.domain.SequenceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides configuration access for the {@link com.epam.wilma.sequence.maintainer.SequenceMaintainer}.
 *
 * @author Tibor_Kovacs
 */
@Component
public class SequenceMaintainerConfigurationAccess implements ConfigurationAccessBase {

    private SequenceProperties properties;
    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        String cronExpression = propertyHolder.get("sequence.cleanup.cron");
        properties = new SequenceProperties(cronExpression);
    }

    /**
     * Returns a {@link SequenceProperties} holding all module specific properties.
     *
     * @return with SequenceProperties object
     */
    public SequenceProperties getProperties() {
        return properties;
    }

}
