package com.epam.wilma.message.search.lucene.search;

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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.domain.exception.QueryCannotBeParsedException;
import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.search.helper.CurrentTimeProvider;
import com.epam.wilma.message.search.lucene.search.helper.IndexReaderFactory;
import com.epam.wilma.message.search.lucene.search.helper.IndexSearcherFactory;

/**
 * Searches for 'Lucene' indexes which match the given pattern.
 * @author Tamas_Bihari
 *
 */
@Component
public class LuceneSearchEngine {

    private static final String FIELD_PATH = "path";
    private final Logger logger = LoggerFactory.getLogger(LuceneSearchEngine.class);
    @Value("#{topQueryHits}")
    private int topQueryHits;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    @Autowired
    private IndexReaderFactory readerFactory;
    @Autowired
    private QueryParser queryParser;
    @Autowired
    private IndexSearcherFactory searcherFactory;
    @Autowired
    private CurrentTimeProvider currentTimeProvider;

    /**
     * Sets {@link QueryParser} instance properties.
     */
    @PostConstruct
    public void initQueryParserProperties() {
        queryParser.setLowercaseExpandedTerms(true);
        queryParser.setAllowLeadingWildcard(true);
    }

    /**
     * Searches for the given text in the index.
     * @param text which we search for
     * @return with the file names where the text matches found. If there is no match will return with empty list.
     */
    public List<String> searchForText(final String text) {
        List<String> fileNames = new ArrayList<>();
        getNewReaderAndSearcherUsingWriter();
        Query query = createQuery(text);
        try {
            TopDocs queryResult = doSearch(query);
            fileNames = getFileNames(queryResult);
            closeIndexReader();
        } catch (IOException e) {
            logger.error(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
        return fileNames;
    }

    private TopDocs doSearch(final Query query) throws IOException {
        long start = currentTimeProvider.getCurrentTimeInMillis();
        TopDocs queryResult = indexSearcher.search(query, topQueryHits);
        long end = currentTimeProvider.getCurrentTimeInMillis();
        logger.info("search in millis:" + (end - start) + " hits:" + queryResult.totalHits);
        return queryResult;
    }

    private List<String> getFileNames(final TopDocs result) throws IOException {
        List<String> fileNames = new ArrayList<>();
        ScoreDoc[] scoreDocs = result.scoreDocs;
        for (int i = 0; i < scoreDocs.length; ++i) {
            int docNumber = scoreDocs[i].doc;
            Document document = getDocument(docNumber);
            fileNames.add(document.get(FIELD_PATH));
        }
        return fileNames;
    }

    private void getNewReaderAndSearcherUsingWriter() {
        try {
            indexReader = readerFactory.create(true);
            indexSearcher = searcherFactory.create(indexReader);
        } catch (IOException e) {
            throw new SystemException("Could not create index reader during search", e);
        }
    }

    private Query createQuery(final String query) {
        Query result = null;
        try {
            result = queryParser.parse(query);
        } catch (ParseException e) {
            throw new QueryCannotBeParsedException("Query " + query + "cannot be parsed. Reason:" + e.getMessage());
        }
        return result;
    }

    Document getDocument(final int docNumber) throws IOException {
        return indexReader.document(docNumber);
    }

    void closeIndexReader() throws IOException {
        indexReader.close();
    }

}
