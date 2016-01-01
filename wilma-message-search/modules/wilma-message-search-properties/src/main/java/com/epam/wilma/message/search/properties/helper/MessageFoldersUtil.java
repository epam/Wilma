package com.epam.wilma.message.search.properties.helper;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import org.springframework.stereotype.Component;

/**
 * Utility class for the message.folders property.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class MessageFoldersUtil {

    /**
     * Takes a String representing one or multiple folders and splits them up.
     * @param messageFolders the value of the property
     * @return an array of {@link String} containing the path(s)
     */
    public String[] getFolders(final String messageFolders) {
        return messageFolders.split(",");
    }
}
