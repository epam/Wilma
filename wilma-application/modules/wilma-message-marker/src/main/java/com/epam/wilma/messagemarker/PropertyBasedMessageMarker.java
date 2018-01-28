package com.epam.wilma.messagemarker;
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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.messagemarker.configuration.MessageMarkerConfigurationAccess;
import com.epam.wilma.messagemarker.configuration.domain.MessageMarkerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Marks a HTTP message by adding a new request header to it.
 * The value of the header contains a generated identifier, that's based on the timestamp when the request is intercepted.
 * @author Marton_Sereg
 *
 */
@Component
public class PropertyBasedMessageMarker implements MessageMarker {

    @Autowired
    private MessageMarkerConfigurationAccess configurationAccess;

    @Override
    public void markMessageHeader(final WilmaHttpRequest request) {
        MessageMarkerRequest messageMarkerRequest = configurationAccess.getProperties();
        if (messageMarkerRequest.getNeedMessageMarker()) {
            request.addHeaderUpdate(WilmaHttpEntity.WILMA_LOGGER_ID, request.getWilmaMessageId());
        }
    }

}
