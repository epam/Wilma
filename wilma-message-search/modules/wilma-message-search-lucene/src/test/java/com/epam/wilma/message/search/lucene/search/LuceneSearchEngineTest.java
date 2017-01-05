package com.epam.wilma.message.search.lucene.search;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.domain.exception.QueryCannotBeParsedException;
import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.search.helper.CurrentTimeProvider;
import com.epam.wilma.message.search.lucene.search.helper.IndexReaderFactory;
import com.epam.wilma.message.search.lucene.search.helper.IndexSearcherFactory;

/**
 * Unit tests for the class {@link LuceneSearchEngine}.
 * @author Tunde_Kovacs
 *
 */
public class LuceneSearchEngineTest {

    private static final String TEXT = "text";
    private static final String FIELD_PATH = "path";
    private static final int TOP_QUERY_HITS = 10000;
    private ScoreDoc[] scoreDocs;
    private TopDocs queryResult;
    private Document document;

    @Mock
    private IndexReader indexReader;
    @Mock
    private IndexSearcher indexSearcher;
    @Mock
    private IndexReaderFactory readerFactory;
    @Mock
    private QueryParser queryParser;
    @Mock
    private IndexSearcherFactory searcherFactory;
    @Mock
    private CurrentTimeProvider currentTimeProvider;
    @Mock
    private Logger logger;
    @Mock
    private Query query;

    @InjectMocks
    private LuceneSearchEngine underTest;

    @BeforeMethod
    public void setUp() {
        underTest = spy(new LuceneSearchEngine());
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "topQueryHits", TOP_QUERY_HITS);
        scoreDocs = new ScoreDoc[1];
        scoreDocs[0] = new ScoreDoc(12, 12);
        queryResult = new TopDocs(TOP_QUERY_HITS, scoreDocs, 10);
        document = new Document();
        document.add(new StringField(FIELD_PATH, TEXT, null));
    }

    @Test
    public void testInitQueryParserProperties() {
        //GIVEN in setUp
        //WHEN
        underTest.initQueryParserProperties();
        //THEN
        verify(queryParser).setLowercaseExpandedTerms(true);
        verify(queryParser).setAllowLeadingWildcard(true);
    }

    @Test
    public void testSearchForTextShouldReturnText() throws IOException, ParseException {
        //GIVEN
        given(readerFactory.create(true)).willReturn(indexReader);
        given(searcherFactory.create(indexReader)).willReturn(indexSearcher);
        given(queryParser.parse(TEXT)).willReturn(query);
        given(indexSearcher.search(query, TOP_QUERY_HITS)).willReturn(queryResult);
        doReturn(document).when(underTest).getDocument(12);
        doNothing().when(underTest).closeIndexReader();
        //WHEN
        List<String> actual = underTest.searchForText(TEXT);
        //THEN
        Assert.assertEquals(actual.get(0), TEXT);
    }

    @Test(expectedExceptions = QueryCannotBeParsedException.class)
    public void testSearchForTextWhenCannotParseQueryShouldThrowError() throws IOException, ParseException {
        //GIVEN
        given(readerFactory.create(true)).willReturn(indexReader);
        given(searcherFactory.create(indexReader)).willReturn(indexSearcher);
        given(queryParser.parse(TEXT)).willThrow(new ParseException());
        //WHEN
        underTest.searchForText(TEXT);
        //THEN it should throw exception
    }

    @Test(expectedExceptions = SystemException.class)
    public void testSearchForTextWhenCannotCreateIndexReadertShouldThrowException() throws IOException {
        //GIVEN
        given(readerFactory.create(true)).willThrow(new IOException());
        //WHEN
        underTest.searchForText(TEXT);
        //THEN it should throw exception
    }

    @Test
    public void testSearchForTextWhenCannotCloseIndexReadertShouldLogError() throws IOException, ParseException {
        //GIVEN
        Whitebox.setInternalState(underTest, "logger", logger);
        given(readerFactory.create(true)).willReturn(indexReader);
        given(searcherFactory.create(indexReader)).willReturn(indexSearcher);
        given(queryParser.parse(TEXT)).willReturn(query);
        given(indexSearcher.search(query, TOP_QUERY_HITS)).willReturn(queryResult);
        doReturn(document).when(underTest).getDocument(12);
        doThrow(new IOException()).when(underTest).closeIndexReader();
        //WHEN
        underTest.searchForText(TEXT);
        //THEN
        verify(logger).error(Mockito.anyString());
    }
}
