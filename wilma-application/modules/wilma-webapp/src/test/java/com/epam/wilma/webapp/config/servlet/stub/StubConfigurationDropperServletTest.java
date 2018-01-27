package com.epam.wilma.webapp.config.servlet.stub;
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
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.service.StubConfigurationDropperService;

/**
 * Provides unit tests for the class {@link StubConfigurationDropperServlet}.
 * @author Tibor_Kovacs
 *
 */
public class StubConfigurationDropperServletTest {

    private static final String DEFAULT_GROUPNAME = "test";
    private static final String PARAMETER_CONSTANS_GROUPNAME = "groupname";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private StubConfigurationDropperService stubConfigurationDropperService;

    @InjectMocks
    private StubConfigurationDropperServlet underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "stubConfigurationDropperService", stubConfigurationDropperService);
        given(request.getParameter(PARAMETER_CONSTANS_GROUPNAME)).willReturn(DEFAULT_GROUPNAME);
    }

    @Test
    public void testDoGetShouldCallDoChangeOfStubConfigurationService() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(stubConfigurationDropperService).dropSelectedStubConfiguration(DEFAULT_GROUPNAME, request);
    }

    @Test
    public void testDoPostShouldCallDoChangeOfStubConfigurationService() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(stubConfigurationDropperService).dropSelectedStubConfiguration(DEFAULT_GROUPNAME, request);
    }
}
