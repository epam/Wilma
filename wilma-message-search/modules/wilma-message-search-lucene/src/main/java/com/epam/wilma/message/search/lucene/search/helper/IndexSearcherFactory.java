package com.epam.wilma.message.search.lucene.search.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.springframework.stereotype.Component;

/**
 * Factory for creating new {@link IndexSearcher} instance.
 * @author Tamas_Bihari
 *
 */
@Component
public class IndexSearcherFactory {

    /**
     * Factory for creating new {@link IndexSearcher} instance using the given {@link IndexReader}.
     * @param indexReader is necessary to create {@link IndexSearcher}
     * @return with the new {@link IndexSearcher} instance
     */
    public IndexSearcher create(final IndexReader indexReader) {
        return new IndexSearcher(indexReader);
    }
}
