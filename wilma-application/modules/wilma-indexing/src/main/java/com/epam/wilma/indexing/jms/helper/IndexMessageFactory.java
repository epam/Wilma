package com.epam.wilma.indexing.jms.helper;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.indexing.domain.IndexMessage;

/**
 * Creates new instances of {@link IndexMessage}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class IndexMessageFactory {

    /**
     * Creates a new instance of {@link IndexMessage} with the given parameters.
     * @param fileName the name of the file to be indexed or to be deleted
     * from the index
     * @param type type information about the file, should it
     * be added to the index or deleted from the index
     * @return the new instance
     */
    public IndexMessage createIndexMessage(final String fileName, final String type) {
        return new IndexMessage(fileName, type);
    }

}
