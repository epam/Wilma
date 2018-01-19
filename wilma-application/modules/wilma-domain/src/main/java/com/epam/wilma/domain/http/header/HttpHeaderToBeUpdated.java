package com.epam.wilma.domain.http.header;
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
 * This class is to be used when a header should be updated (either added or changed).
 *
 * @author Tamas_Kohegyi
 */
public class HttpHeaderToBeUpdated extends HttpHeaderChange {
    private String originalValue;
    private String newValue;

    /**
     * Represents a header value that need to be updated. Contains both original and new values.
     * Original value remains null if it is a brand new header.
     *
     * @param newValue is the new value of the header.
     */
    public HttpHeaderToBeUpdated(final String newValue) {
        this.newValue = newValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }
}
