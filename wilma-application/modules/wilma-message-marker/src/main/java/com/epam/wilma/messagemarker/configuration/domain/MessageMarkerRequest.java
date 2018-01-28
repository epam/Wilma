package com.epam.wilma.messagemarker.configuration.domain;
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
 * @author Tamas Kohegyi
 *
 */
public class MessageMarkerRequest {

    private boolean needMessageMarker;

    /**
     * Constructs a new property holding object with the given fields.
     * @param needMessageMarker if Wilma Message ID should be added to the message requests or not
     */
    public MessageMarkerRequest(final String needMessageMarker) {
        this.needMessageMarker = !"off".equals(needMessageMarker);
    }

    public boolean getNeedMessageMarker() {
        return needMessageMarker;
    }

    public void setNeedMessageMarker(final boolean needMessageMarker) {
        this.needMessageMarker = needMessageMarker;
    }

}
