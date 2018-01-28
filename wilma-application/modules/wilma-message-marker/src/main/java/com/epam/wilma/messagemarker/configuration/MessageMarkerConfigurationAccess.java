package com.epam.wilma.messagemarker.configuration;

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
import com.epam.wilma.messagemarker.configuration.domain.MessageMarkerRequest;
import com.epam.wilma.properties.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides configuration access for the module.
 *
 * @author Tamas Kohegyi
 */
@Component
public class MessageMarkerConfigurationAccess implements ConfigurationAccessBase {

    private MessageMarkerRequest markerRequest;

    @Autowired
    private PropertyHolder propertyHolder;

    @Override
    public void loadProperties() {
        String messageMarking = propertyHolder.get("message.marking");
        markerRequest = new MessageMarkerRequest(messageMarking);
    }

    /**
     * Returns a {@link MessageMarkerRequest} holding all module specific properties.
     *
     * @return the propertiesDTO object
     */
    public MessageMarkerRequest getProperties() {
        return markerRequest;
    }

    /**
     * Sets the message marker status.
     * @param messageMarkerRequest to "on" in case of true, otherwise "off"
     */
    public void setMessageMarkerRequest(final boolean messageMarkerRequest) {
        markerRequest.setNeedMessageMarker(messageMarkerRequest);
    }

}
