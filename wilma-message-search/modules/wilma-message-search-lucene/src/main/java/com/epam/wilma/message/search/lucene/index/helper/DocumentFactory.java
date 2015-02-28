package com.epam.wilma.message.search.lucene.index.helper;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import org.apache.lucene.document.Document;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating new {@link Document}s.
 * Needed for unit testing purposes.
 * @author Tunde_Kovacs
 *
 */
@Component
public class DocumentFactory {

    /**
     * Creates a new {@link Document}.
     * @return the new instance
     */
    public Document createDocument() {
        return new Document();
    }
}
