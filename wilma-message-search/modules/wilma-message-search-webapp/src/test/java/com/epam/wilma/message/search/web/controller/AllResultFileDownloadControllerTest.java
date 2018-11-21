package com.epam.wilma.message.search.web.controller;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.web.support.FileZipper;

/**
 * Provides unit tests for the class {@link AllResultFileDownloadController}.
 * @author Tibor_Kovacs
 *
 */
public class AllResultFileDownloadControllerTest {

    private static final String SEARCH_RESULT_KEY = "searchResult";

    @Mock
    private FileZipper fileZipper;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletOutputStream outputStream;

    @InjectMocks
    private AllResultFileDownloadController underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDownloadFilesWhenThereIsSearchingResult() throws ServletException, IOException {
        //GIVEN
        List<String> pair = new ArrayList<String>();
        pair.add("first");
        pair.add("test");
        List<List<String>> list = new ArrayList<>();
        list.add(pair);
        given(session.getAttribute(SEARCH_RESULT_KEY)).willReturn(list);
        given(response.getOutputStream()).willReturn(outputStream);
        //WHEN
        underTest.downloadFiles(session, response);
        //THEN
        verify(fileZipper).createZipWithFiles(list, outputStream);
        verify(session).removeAttribute(SEARCH_RESULT_KEY);
    }

    @Test
    public void testDownloadFilesWithoutSearchingResult() throws ServletException, IOException {
        //GIVEN
        List<List<String>> list = new ArrayList<>();
        given(session.getAttribute(SEARCH_RESULT_KEY)).willReturn(list);
        given(response.getOutputStream()).willReturn(outputStream);
        //WHEN
        underTest.downloadFiles(session, response);
        //THEN
        verify(fileZipper, times(0)).createZipWithFiles(list, outputStream);
    }
}
