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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.wilma.webapp.service.StubConfigurationDropperService;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Provides unit tests for the class {@link StubConfigurationDropperServlet}.
 * @author Tibor_Kovacs
 *
 */
public class StubConfigurationDropperServletTest {

    private static final String DEFAULT_GROUP_NAME = "test";
    private static final String PARAMETER_CONTAINS_GROUP_NAME = "groupname";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private StubConfigurationDropperService stubConfigurationDropperService;

    @InjectMocks
    private StubConfigurationDropperServlet underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "stubConfigurationDropperService", stubConfigurationDropperService);
        given(request.getParameter(PARAMETER_CONTAINS_GROUP_NAME)).willReturn(DEFAULT_GROUP_NAME);
    }

    @Test
    public void testDoGetShouldCallDoChangeOfStubConfigurationService() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(stubConfigurationDropperService).dropSelectedStubConfiguration(DEFAULT_GROUP_NAME, request);
    }

    @Test
    public void testDoPostShouldCallDoChangeOfStubConfigurationService() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(stubConfigurationDropperService).dropSelectedStubConfiguration(DEFAULT_GROUP_NAME, request);
    }
}
