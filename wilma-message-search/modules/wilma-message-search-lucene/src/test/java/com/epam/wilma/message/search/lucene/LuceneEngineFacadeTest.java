package com.epam.wilma.message.search.lucene;
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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.lucene.delete.LuceneDeleteEngine;
import com.epam.wilma.message.search.lucene.index.LuceneIndexEngine;
import com.epam.wilma.message.search.lucene.search.LuceneSearchEngine;

/**
 * Unit tests for the {@link LuceneEngineFacade}.
 * @author Tunde_Kovacs
 *
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

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
