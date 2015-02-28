package com.epam.wilma.message.search.lucene.delete;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.helper.TermFactory;

/**
 * Unit tests for the class {@link LuceneDeleteEngine}.
 * @author Tunde_Kovacs
 *
 */
public class LuceneDeleteEngineTest {

    private static final String TEXT = "text";
    private static final String FIELD_NAME = "path";
    private Term term;

    @Mock
    private IndexWriter indexWriter;
    @Mock
    private TermFactory termFactory;

    @InjectMocks
    private LuceneDeleteEngine underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        term = new Term(FIELD_NAME);
        Whitebox.setInternalState(underTest, "fieldName", FIELD_NAME);
    }

    @Test
    public void testDeleteDocFromIndexShouldDeleteFileFromIndex() throws IOException {
        //GIVEN
        given(termFactory.createTerm(FIELD_NAME, TEXT)).willReturn(term);
        //WHEN
        underTest.deleteDocFromIndex(TEXT);
        //THEN
        verify(indexWriter).deleteDocuments(term);
    }

    @Test(expectedExceptions = SystemException.class)
    public void testDeleteDocFromIndexWhenCannotDeleteShouldThrowException() throws IOException {
        //GIVEN
        given(termFactory.createTerm(FIELD_NAME, TEXT)).willReturn(term);
        willThrow(new IOException()).given(indexWriter).deleteDocuments(term);
        //WHEN
        underTest.deleteDocFromIndex(TEXT);
        //THEN it should throw exceptions
    }
}
