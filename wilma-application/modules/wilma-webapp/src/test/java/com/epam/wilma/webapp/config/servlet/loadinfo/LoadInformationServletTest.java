package com.epam.wilma.webapp.config.servlet.loadinfo;

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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.maintainer.domain.DeletedFileProvider;
import com.epam.wilma.maintainer.task.helper.MessageFileCounter;
import com.epam.wilma.safeguard.configuration.domain.QueueSizeProvider;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;

/**
 * Test class for {@link LoadInformationServlet}.
 * @author Tibor_Kovacs
 *
 */
public class LoadInformationServletTest {
    @InjectMocks
    private LoadInformationServlet underTest;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private QueueSizeProvider queueSizeProvider;
    @Mock
    private DeletedFileProvider deletedFileProvider;
    @Mock
    private PropertyDTO properties;
    @Mock
    private MessageFileCounter messageCounter;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testDoGetSetsResponseTypeAndWritesResponseUsingLoadInformation() throws ServletException, IOException {
        // GIVEN
        given(response.getWriter()).willReturn(printWriter);
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(response).setContentType("application/json");
        verify(printWriter).write(Mockito.anyString());
    }

    @Test
    public final void testDoPostSetsResponseTypeAndWritesResponseUsingLoadInformation() throws ServletException, IOException {
        // GIVEN
        Whitebox.setInternalState(underTest, "deletedFilesCount", 0);
        Whitebox.setInternalState(underTest, "messagesCount", 0);
        Whitebox.setInternalState(underTest, "loggerQueueSize", 0L);
        Whitebox.setInternalState(underTest, "responseQueueSize", 0L);
        given(response.getWriter()).willReturn(printWriter);
        // WHEN
        underTest.doPost(request, response);
        // THEN
        verify(response).setContentType("application/json");
        verify(printWriter).write(Mockito.anyString());
    }
}
