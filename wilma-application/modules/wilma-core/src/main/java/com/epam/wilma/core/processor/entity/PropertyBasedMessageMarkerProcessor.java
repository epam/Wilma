package com.epam.wilma.core.processor.entity;
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
import com.epam.wilma.messagemarker.PropertyBasedMessageMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Processes a {@link WilmaHttpEntity} by marking its message header.
 * @author Tunde_Kovacs
 *
 */
@Component
public class PropertyBasedMessageMarkerProcessor extends ProcessorBase {

    @Autowired
    private PropertyBasedMessageMarker marker;

    @Override
    public void process(final WilmaHttpEntity entity) {
        WilmaHttpRequest request = (WilmaHttpRequest) entity;
        marker.markMessageHeader(request);
    }
}
