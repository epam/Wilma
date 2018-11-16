package com.epam.wilma.webapp.config.servlet.stub.upload;
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
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Provides unit tests for the class {@link ExternalResponseFormatterUploadServlet}.
 * @author Tunde_Kovacs
 *
 */
public class ExternalResponseFormatterUploadServletTest {

    private static final String FILE_NAME = "resource file";
    private static final String EXCEPTION_MESSAGE = "Could not upload external response formatter class: ";
    private static final String PATH = "path";

    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletInputStream inputStream;
    @Mock
    private FileWriter fileWriter;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @InjectMocks
    private ExternalResponseFormatterUploadServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        Whitebox.setInternalState(underTest, "stubResourcePathProvider", stubResourcePathProvider);
        Whitebox.setInternalState(underTest, "fileWriter", fileWriter);
        given(request.getInputStream()).willReturn(inputStream);
        given(stubResourcePathProvider.getResponseFormattersPathAsString()).willReturn(PATH);
    }

    @Test
    public void testDoGetShouldCallFileWriter() throws ServletException, IOException {
        //GIVEN
        given(request.getParameter("fileName")).willReturn(FILE_NAME);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(fileWriter).write(inputStream, PATH + "/" + FILE_NAME, EXCEPTION_MESSAGE);
    }

    @Test
    public void testDoGetWhenExceptionShouldWriteErrorToResponse() throws ServletException, IOException {
        //GIVEN
        given(request.getParameter("fileName")).willReturn(FILE_NAME);
        given(response.getWriter()).willReturn(printWriter);
        willThrow(new CannotUploadExternalResourceException(EXCEPTION_MESSAGE, new Throwable())).given(fileWriter).write(inputStream,
                PATH + "/" + FILE_NAME, EXCEPTION_MESSAGE);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(printWriter).write(Mockito.anyString());
    }

    @Test
    public void testDoGetWhenFileNameIsNullShouldWriteErrorToResponse() throws ServletException, IOException {
        //GIVEN
        given(request.getParameter("fileName")).willReturn(null);
        given(response.getWriter()).willReturn(printWriter);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(printWriter).write("Please give a name to the response formatter! e.g.:.../responseformatter?fileName=ExternalResponseFormatter.class");
    }

    @Test
    public void testDoPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN
        given(request.getParameter("fileName")).willReturn(FILE_NAME);
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(fileWriter).write(inputStream, PATH + "/" + FILE_NAME, EXCEPTION_MESSAGE);
    }
}
