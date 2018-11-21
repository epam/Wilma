package com.epam.wilma.message.search.lucene.delete;
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

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.helper.TermFactory;

/**
 * Responsible for deleting a file from the index.
 * @author Tamas_Bihari
 *
 */
@Component
public class LuceneDeleteEngine {

    @Value("#{fieldName}")
    private String fieldName;

    @Autowired
    @Qualifier("indexWriter")
    private IndexWriter indexWriter;
    @Autowired
    private TermFactory termFactory;

    /**
     * Deletes a file from the index with the given file name.
     * @param fileName the file to be deleted from the index
     */
    public void deleteDocFromIndex(final String fileName) {
        try {
            Term term = termFactory.createTerm(fieldName, fileName);
            indexWriter.deleteDocuments(term);
        } catch (IOException e) {
            throw new SystemException("Could not delete file " + fileName + " from the index. Reason:" + e.getMessage());
        }
    }

}
