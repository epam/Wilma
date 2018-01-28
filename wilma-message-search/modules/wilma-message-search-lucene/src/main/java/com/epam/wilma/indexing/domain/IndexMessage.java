package com.epam.wilma.indexing.domain;

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
 * Object sent to jms queue with information for a file
 * indexing application.
 * @author Tunde_Kovacs
 *
 */
public class IndexMessage implements Serializable {

    /**
     * Auto-generated serial version ID to check the classes matching after deserialization.
     */
    //CHECKSTYLE.OFF
    private static final long serialVersionUID = 4161216415893996829L;
    //CHECKSTYLE.ON

    private final String fileName;
    private final String type;

    /**
     * Constructs a new message with the given parameters.
     * @param fileName the name of the file to be indexed or to be deleted
     * from the index
     * @param type information about the file, should it
     * be added to the index or deleted from the index
     */
    public IndexMessage(final String fileName, final String type) {
        super();
        this.fileName = fileName;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public String getType() {
        return type;
    }

}
