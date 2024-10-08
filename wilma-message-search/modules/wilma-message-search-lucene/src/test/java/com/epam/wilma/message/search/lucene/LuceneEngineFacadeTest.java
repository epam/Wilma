package com.epam.wilma.message.search.lucene;
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

import com.epam.wilma.message.search.lucene.delete.LuceneDeleteEngine;
import com.epam.wilma.message.search.lucene.index.LuceneIndexEngine;
import com.epam.wilma.message.search.lucene.search.LuceneSearchEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link LuceneEngineFacade}.
 *
 * @author Tunde_Kovacs
 */
public class LuceneEngineFacadeTest {

    private static final String FILENAME = "filename";
    @Mock
    private LuceneSearchEngine searchEngine;
    @Mock
    private LuceneIndexEngine indexEngine;
    @Mock
    private LuceneDeleteEngine deleteEngine;

    @InjectMocks
    private LuceneEngineFacade underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchShouldReturnText() {
        //GIVEN
        String text = "text";
        List<String> expected = new ArrayList<>();
        expected.add("result");
        given(searchEngine.searchForText(text)).willReturn(expected);
        //WHEN
        List<String> actual = underTest.search(text);
        //THEN
        assertEquals(actual.get(0), expected.get(0));
    }

    @Test
    public void testAddFileToIndexShouldCallIndexEngine() {
        //GIVEN in setUp
        //WHEN
        underTest.addFileToIndex(FILENAME);
        //THEN
        verify(indexEngine).addFileToIndex(FILENAME);
    }

    @Test
    public void testDeleteFileFromIndexShouldCallDeleteEngine() {
        //GIVEN in setUp
        //WHEN
        underTest.deleteFileFromIndex(FILENAME);
        //THEN
        verify(deleteEngine).deleteDocFromIndex(FILENAME);
    }

    @Test
    public void testBuildIndexShouldCallIndexEngine() {
        //GIVEN in setUp
        //WHEN
        underTest.buildIndex(FILENAME);
        //THEN
        verify(indexEngine).createIndex(FILENAME);
    }
}
