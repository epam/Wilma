package com.epam.wilma.message.search.lucene;
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.lucene.delete.LuceneDeleteEngine;
import com.epam.wilma.message.search.lucene.index.LuceneIndexEngine;
import com.epam.wilma.message.search.lucene.search.LuceneSearchEngine;

/**
 * Facade that redirects indexing and searching features with
 * a lucene engine.
 * @author Tunde_Kovacs
 *
 */
@Component
public class LuceneEngineFacade implements LuceneEngine {

    @Autowired
    private LuceneSearchEngine searchEngine;
    @Autowired
    private LuceneIndexEngine indexEngine;
    @Autowired
    private LuceneDeleteEngine deleteEngine;

    @Override
    public List<String> search(final String text) {
        return searchEngine.searchForText(text);
    }

    @Override
    public void addFileToIndex(final String fileName) {
        indexEngine.addFileToIndex(fileName);
    }

    @Override
    public void deleteFileFromIndex(final String fileName) {
        deleteEngine.deleteDocFromIndex(fileName);
    }

    @Override
    public void buildIndex(final String rootDirectory) {
        indexEngine.createIndex(rootDirectory);
    }

}
