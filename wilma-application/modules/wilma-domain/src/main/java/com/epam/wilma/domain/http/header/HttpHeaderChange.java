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

import java.io.Serializable;

/**
 * This class is for handling Http header changes (add, remove).
 *
 * @author Tamas_Kohegyi
 */
public class HttpHeaderChange implements Serializable {

    private boolean applied;

    /**
     * By default the header Change is not applied. Cann this method to reflect,
     * that the header in the message is updated with this change, ie the change is applied.
     */
    public void setApplied() {
        applied = true;
    }

    public boolean isApplied() {
        return applied;
    }
}
