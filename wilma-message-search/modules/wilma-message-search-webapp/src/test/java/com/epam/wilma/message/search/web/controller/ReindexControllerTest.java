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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.lucene.index.scheduler.IndexTaskScheduler;

/**
 * Provides unit tests for the class {@link ReindexController}.
 * @author Tibor_Kovacs
 *
 */
public class ReindexControllerTest {
    @Mock
    private Logger logger;
    @Mock
    private HttpServletResponse response;
    @Mock
    private IndexTaskScheduler indexTaskScheduler;
    @Mock
    private PrintWriter writer;

    @InjectMocks
    private ReindexController underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(response.getWriter()).willReturn(writer);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testReindexShouldRunReindex() throws IOException {
        //GIVEN
        //WHEN
        underTest.reindex(response);
        //THEN
        verify(indexTaskScheduler).runReindexOnDemand();
    }

    @Test
    public void testReindexShouldRunReindexThenExceptionRaisedInGetWriter() throws IOException {
        //GIVEN
        IOException exception = new IOException();
        doThrow(exception).when(response).getWriter();
        //WHEN
        underTest.reindex(response);
        //THEN
        verify(indexTaskScheduler).runReindexOnDemand();
        verify(logger).info("error occurred when accessing /reindex", exception);
    }

}
