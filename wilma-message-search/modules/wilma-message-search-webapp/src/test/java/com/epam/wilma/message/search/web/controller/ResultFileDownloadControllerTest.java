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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.web.support.FileZipper;

/**
 * Provides unit tests for the class {@link ResultFileDownloadController}.
 * @author Tibor_Kovacs
 *
 */
public class ResultFileDownloadControllerTest {
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
    private ResultFileDownloadController underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOpenFileWhenTheFileExistsOnThePath() throws IOException {
        //GIVEN
        given(response.getOutputStream()).willReturn(outputStream);
        //WHEN
        underTest.openFile("src/test/resources/first_testreq.txt", response);
        //THEN
        verify(response, times(0)).setHeader(Mockito.anyString(), Mockito.anyString());
        verify(response).setContentType(Mockito.anyString());
        verify(response).flushBuffer();
    }

    @Test
    public void testOpenFileWhenTheFileNotExistsOnThePath() throws IOException {
        //GIVEN
        given(response.getOutputStream()).willReturn(outputStream);
        //WHEN
        underTest.openFile("src/test/resources/testreq.txt", response);
        //THEN
        verify(response, times(0)).setHeader(Mockito.anyString(), Mockito.anyString());
        verify(response).setContentType(Mockito.anyString());
    }

    @Test
    public void testDownloadFileWhenTheFileExistsOnThePath() throws IOException {
        //GIVEN
        given(response.getOutputStream()).willReturn(outputStream);
        //WHEN
        underTest.downloadFile("src/test/resources/first_testreq.txt", response);
        //THEN
        verify(response).setHeader(Mockito.anyString(), Mockito.anyString());
        verify(response).setContentType(Mockito.anyString());
        verify(response).flushBuffer();
    }

    @Test
    public void testDownloadFileWhenTheFileNotExistsOnThePath() throws IOException {
        //GIVEN
        given(response.getOutputStream()).willReturn(outputStream);
        //WHEN
        underTest.downloadFile("src/test/resources/testreq.txt", response);
        //THEN
        verify(response).setHeader(Mockito.anyString(), Mockito.anyString());
        verify(response).setContentType(Mockito.anyString());
    }
}
