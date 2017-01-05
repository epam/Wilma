package com.epam.wilma.webapp.config.servlet.stub;
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

import com.epam.wilma.webapp.service.StubConfigurationStatusService;

/**
 * Provides unit tests for the class {@link StubConfigurationStatusServlet}.
 * @author Tibor_Kovacs
 *
 */
public class StubConfigurationStatusServletTest {

    private static final String PARAMETER_CONTANS_NEXTSTATUS = "nextstatus";
    private static final String DEFAULT_GROUPNAME = "test";
    private static final String PARAMETER_CONSTANS_STATUS = PARAMETER_CONTANS_NEXTSTATUS;
    private static final String PARAMETER_CONSTANS_GROUPNAME = "groupname";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private StubConfigurationStatusService stubConfigurationStatusService;
    @Mock
    private PrintWriter writer;

    @InjectMocks
    private StubConfigurationStatusServlet underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(request.getParameter(PARAMETER_CONSTANS_GROUPNAME)).willReturn(DEFAULT_GROUPNAME);
        given(request.getParameter(PARAMETER_CONSTANS_STATUS)).willReturn("true");
    }

    @Test
    public void testDoGetShouldCallDoChangeOfStubConfigurationService() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(stubConfigurationStatusService).changeStatus(true, DEFAULT_GROUPNAME, request);
    }

    @Test
    public void testDoPostShouldCallDoChangeOfStubConfigurationService() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(stubConfigurationStatusService).changeStatus(true, DEFAULT_GROUPNAME, request);
    }

    @Test
    public void testDoGetShouldCallWriteErrorToResponseBecauseOfWrongParameter() throws ServletException, IOException {
        //GIVEN
        given(request.getParameter(PARAMETER_CONTANS_NEXTSTATUS)).willReturn("falseaaa");
        given(response.getWriter()).willReturn(writer);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).getWriter();
    }
}
