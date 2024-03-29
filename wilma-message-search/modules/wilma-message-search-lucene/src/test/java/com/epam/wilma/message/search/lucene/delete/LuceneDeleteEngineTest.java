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

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.helper.TermFactory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link LuceneDeleteEngine}.
 *
 * @author Tunde_Kovacs
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        term = new Term(FIELD_NAME);
        ReflectionTestUtils.setField(underTest, "fieldName", FIELD_NAME);
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

    @Test
    public void testDeleteDocFromIndexWhenCannotDeleteShouldThrowException() {
        Assertions.assertThrows(SystemException.class, () -> {
            //GIVEN
            given(termFactory.createTerm(FIELD_NAME, TEXT)).willReturn(term);
            willThrow(new IOException()).given(indexWriter).deleteDocuments(term);
            //WHEN
            underTest.deleteDocFromIndex(TEXT);
            //THEN it should throw exceptions
        });
    }
}
