package com.epam.wilma.test.server;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for {@link ExampleHandler}.
 * @author Marton_Sereg
 *
 */
public class ExampleHandlerTest {

    private static final String EXAMPLE_XML = "example.xml";

    @InjectMocks
    private ExampleHandler underTest;

    @Mock
    private InputStreamConverter inputStreamConverter;

    @Mock
    private Request request;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private ServletInputStream requestBody;

    @Mock
    private InputStream responseBody;

    @Mock
    private PrintWriter writer;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = Mockito.spy(underTest);
    }

    @Test(enabled = false)
    public final void testHandleShouldReturnResponse() throws IOException, ServletException {
        // GIVEN
        given(inputStreamConverter.getStringFromStream(requestBody)).willReturn("exampleID=\"123\"");
        given(inputStreamConverter.getStringFromStream(responseBody)).willReturn("server-response");
        given(underTest.getXmlFromFile(EXAMPLE_XML)).willReturn(responseBody);
        given(httpServletRequest.getInputStream()).willReturn(requestBody);
        given(httpServletResponse.getWriter()).willReturn(writer);
        // WHEN
        underTest.handle("/example", request, httpServletRequest, httpServletResponse);
        // THEN
        verify(httpServletResponse).setContentType(anyString());
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println("server-response");
        verify(request).setHandled(true);
    }
}
