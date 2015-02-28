package com.epam.wilma.message.search.lucene.search.helper;
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

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates a new {@link IndexReader} instance using previously opened {@link IndexWriter}
 * to be able to handle real time changes.
 * @author Tamas_Bihari
 *
 */
@Component
public class IndexReaderFactory {

    @Autowired
    private IndexWriter indexWriter;

    /**
     * Creates a new {@link IndexReader} instance using previously opened {@link IndexWriter}
     * to be able to handle real time changes.
     * @param applyAllDeletes If true, all buffered deletes will be applied (made visible) in the returned reader.
     * If false, the deletes are not applied but remain buffered (in IndexWriter) so that they will be applied in the future.
     * @return with the new {@link IndexReader} instance
     * @throws IOException was thrown when {@link IndexReader} creation failed
     */
    public IndexReader create(final boolean applyAllDeletes) throws IOException {
        return DirectoryReader.open(indexWriter, applyAllDeletes);
    }
}
