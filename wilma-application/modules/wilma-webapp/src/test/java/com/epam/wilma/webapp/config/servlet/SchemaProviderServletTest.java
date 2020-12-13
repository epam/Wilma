package com.epam.wilma.webapp.config.servlet;

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

import com.epam.wilma.webapp.config.servlet.helper.BufferedReaderFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link SchemaProviderServlet}.
 *
 * @author Tunde_Kovacs
 */
public class SchemaProviderServletTest {

    private static final String SCHEMA = "StubConfig.json";
    @Mock
    private BufferedReaderFactory bufferedReaderFactory;
    @Mock
    private Logger logger;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    @Mock
    private BufferedReader reader;

    private SchemaProviderServlet underTest;

    @Before
    public void setUp() throws IOException, NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        underTest = new SchemaProviderServlet(SCHEMA, bufferedReaderFactory);
        given(response.getWriter()).willReturn(writer);
    }

    @Test
    public void testDoGetShouldWriteSchemaFile() throws ServletException, IOException {
        //GIVEN
        String nullString = null;
        given(bufferedReaderFactory.createBufferedReader(SCHEMA)).willReturn(reader);
        given(reader.readLine()).willReturn("first line", nullString);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write(Mockito.anyString());
    }

    @Test
    public void testDoGetShouldSetContentTypeToJson() throws ServletException, IOException {
        //GIVEN
        String nullString = null;
        given(bufferedReaderFactory.createBufferedReader(SCHEMA)).willReturn(reader);
        given(reader.readLine()).willReturn(nullString);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("application/json");
    }

    @Test
    public void testDoGetShouldCloseWriter() throws ServletException, IOException {
        //GIVEN
        String nullString = null;
        given(bufferedReaderFactory.createBufferedReader(SCHEMA)).willReturn(reader);
        given(reader.readLine()).willReturn(nullString);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).close();
    }

    @Test
    public void testDoGetWhenIOExceptionShouldLogError() throws ServletException, IOException, NoSuchFieldException {
        //GIVEN
        given(bufferedReaderFactory.createBufferedReader(SCHEMA)).willReturn(reader);
        IOException exception = new IOException();
        given(reader.readLine()).willThrow(exception);
        FieldSetter.setField(underTest, underTest.getClass().getDeclaredField("logger"), logger);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(logger).error(SCHEMA + " could not be read!", exception);
    }

    @Test
    public void testDoPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN
        String nullString = null;
        given(bufferedReaderFactory.createBufferedReader(SCHEMA)).willReturn(reader);
        given(reader.readLine()).willReturn("first line", nullString);
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(writer).write(Mockito.anyString());
    }
}
