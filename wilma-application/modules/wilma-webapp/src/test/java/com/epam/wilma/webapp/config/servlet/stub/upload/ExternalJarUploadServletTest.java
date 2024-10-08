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

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ExternalJarUploadServlet}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class ExternalJarUploadServletTest {

    private static final String FILE_NAME = "resource file";
    private static final String EXCEPTION_MESSAGE = "Could not upload external jar: ";
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
    private ExternalJarUploadServlet underTest;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        ReflectionTestUtils.setField(underTest, "stubResourcePathProvider", stubResourcePathProvider);
        ReflectionTestUtils.setField(underTest, "fileWriter", fileWriter);
        given(request.getInputStream()).willReturn(inputStream);
        given(stubResourcePathProvider.getJarPathAsString()).willReturn(PATH);
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
        verify(printWriter).write(underTest.returnHintMessage());
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
