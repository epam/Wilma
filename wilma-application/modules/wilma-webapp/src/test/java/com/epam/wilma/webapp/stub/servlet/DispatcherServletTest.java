package com.epam.wilma.webapp.stub.servlet;
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

import com.epam.wilma.webapp.stub.response.StubResponseWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the <tt>DispatcherServlet</tt> class.
 *
 * @author Tamas_Bihari
 */
public class DispatcherServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private StubResponseWriter stubResponseWriter;

    @InjectMocks
    private DispatcherServlet underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "stubResponseWriter", stubResponseWriter);
    }

    @Test
    public void testDoGetShouldCallWriteResponse() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(stubResponseWriter).writeResponse(request, response);
    }

    @Test
    public void testDoPostShouldCallWriteResponse() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(stubResponseWriter).writeResponse(request, response);
    }
}
