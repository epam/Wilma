package com.epam.wilma.message.search.web.controller;

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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.domain.IndexStatus;
import com.epam.wilma.message.search.domain.exception.QueryCannotBeParsedException;
import com.epam.wilma.message.search.lucene.LuceneEngine;
import com.epam.wilma.message.search.web.support.FileChecker;
import com.epam.wilma.message.search.web.support.FileZipper;

/**
 * Provides unit tests for the class {@link SearchController}.
 * @author Tibor_Kovacs
 *
 */
public class SearchControllerTest {

    @Mock
    private LuceneEngine luceneEngine;
    @Mock
    private FileZipper fileZipper;
    @Mock
    private FileChecker fileChecker;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private IndexStatus indexStatus;

    private List<String> lucineResult;
    private List<List<String>> listOfLists;

    @InjectMocks
    private SearchController underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        lucineResult = new ArrayList<>();
        listOfLists = new ArrayList<>();
        given(response.getOutputStream()).willReturn(outputStream);
        given(fileChecker.checkFilesExistsWithPairs(Mockito.anyList())).willReturn(listOfLists);
        given(luceneEngine.search("test")).willReturn(lucineResult);
    }

    @Test
    public void testSearchAndZipWhenSearchedTextIsNotEmpty() throws IOException {
        //GIVEN
        //WHEN
        underTest.searchAndZip("test", response);
        //THEN
        verify(luceneEngine).search("test");
        verify(fileChecker).checkFilesExistsWithPairs(lucineResult);
        verify(fileZipper).createZipWithFiles(listOfLists, outputStream);
    }

    @Test
    public void testSearchAndZipWhenSearchedTextIsEmptyString() throws IOException {
        //GIVEN
        //WHEN
        underTest.searchAndZip("", response);
        //THEN
        verify(fileChecker, times(0)).checkFilesExistsWithPairs(lucineResult);
        verify(fileZipper, times(0)).createZipWithFiles(listOfLists, outputStream);
    }

    @Test
    public void testSearchAndZipWhenSearchedTextIsNull() throws IOException {
        //GIVEN
        //WHEN
        underTest.searchAndZip(null, response);
        //THEN
        verify(fileChecker, times(0)).checkFilesExistsWithPairs(lucineResult);
        verify(fileZipper, times(0)).createZipWithFiles(listOfLists, outputStream);
    }

    @Test
    public void testSearchAndZipWhenQueryIsNotValid() throws IOException {
        //GIVEN
        given(luceneEngine.search("test")).willThrow(new QueryCannotBeParsedException("Parse Exception"));
        //WHEN
        underTest.searchAndZip("test", response);
        //THEN
        verify(fileZipper).createZipWithFiles(new ArrayList<List<String>>(), outputStream);
    }

    @Test
    public void testSearchForFilesWhenSearchedTextIsNotEmpty() throws IOException {
        //GIVEN
        //WHEN
        underTest.searchForFiles("test", session);
        //THEN
        verify(luceneEngine).search("test");
        verify(fileChecker).checkFilesExistsWithPairs(lucineResult);
        verify(session).setAttribute("searchResult", listOfLists);
    }

    @Test
    public void testSearchForFilesWhenSearchedTextIsEmptyString() throws IOException {
        //GIVEN
        //WHEN
        underTest.searchForFiles("", session);
        //THEN
        verify(luceneEngine).search("");
        verify(fileChecker).checkFilesExistsWithPairs(lucineResult);
        verify(session).setAttribute("searchResult", listOfLists);
    }

    @Test
    public void testSearchForFilesWhenSearchedTextIsNull() throws IOException {
        //GIVEN
        //WHEN
        underTest.searchForFiles(null, session);
        //THEN
        verify(luceneEngine).search(null);
        verify(fileChecker).checkFilesExistsWithPairs(lucineResult);
        verify(session).setAttribute("searchResult", listOfLists);
    }

    @Test
    public void testSearchForFilesWhenIndexIsNotReady() throws IOException {
        //GIVEN
        given(indexStatus.isReady()).willReturn(false);
        //WHEN
        ResponseEntity<String> result = underTest.searchForFiles("test", session);
        //THEN
        Assert.assertEquals(result.getStatusCode(), HttpStatus.PARTIAL_CONTENT);
        verify(luceneEngine).search("test");
        verify(fileChecker).checkFilesExistsWithPairs(lucineResult);
        verify(session).setAttribute("searchResult", listOfLists);
    }

    @Test
    public void testSearchForFilesWhenIndexIsReady() throws IOException {
        //GIVEN
        given(indexStatus.isReady()).willReturn(true);
        //WHEN
        ResponseEntity<String> result = underTest.searchForFiles("test", session);
        //THEN
        Assert.assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        verify(luceneEngine).search("test");
        verify(fileChecker).checkFilesExistsWithPairs(lucineResult);
        verify(session).setAttribute("searchResult", listOfLists);
    }

    @Test
    public void testSearchForFilesWhenQueryIsNotValid() throws IOException {
        //GIVEN
        given(luceneEngine.search("test")).willThrow(new QueryCannotBeParsedException("Parse Exception"));
        //WHEN
        ResponseEntity<String> result = underTest.searchForFiles("test", session);
        //THEN
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
