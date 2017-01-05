package com.epam.wilma.webapp.config.servlet.stub.save;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.exception.DocumentTransformationException;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.service.StubConfigurationSaverService;

/**
 * Provides unit tests for the class {@link StubConfigurationSaverServlet}.
 * @author Tibor_Kovacs
 *
 */
public class StubConfigurationSaverServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private StubConfigurationSaverService saverService;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Mock
    private PrintWriter writer;

    @InjectMocks
    private StubConfigurationSaverServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(urlAccessLogMessageAssembler.assembleMessage(request, "Valami")).willReturn("Test log message");
        given(response.getWriter()).willReturn(writer);
    }

    @Test
    public void testDoGetShouldCallSaveStubConfigurations() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(saverService).saveStubConfigurations();
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPostShouldCallSaveStubConfigurations() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(saverService).saveStubConfigurations();
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGetShouldCallSaveStubConfigurationsWhenAnExceptionOccurred() throws ServletException, IOException,
        DocumentTransformationException {
        //GIVEN
        doThrow(new DocumentTransformationException("Test", new NullPointerException())).when(saverService).saveStubConfigurations();
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
