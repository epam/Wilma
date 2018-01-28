package com.epam.wilma.webapp.config.servlet.stub.helper;
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
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.config.servlet.helper.FileReader;

/**
 * Provides unit tests for the class {@link ResourceFileNameHandler}.
 * @author Tunde_Kovacs
 *
 */
public class ResourceFileNameHandlerTest {

    private static final String HTML = "text/html";
    private static final String TEXT = "text/plain";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String FILE_NOT_FOUND_MSG = "Requested file not found.";
    private static final String PATH_INFO = "/filename";
    private static final String FILE_NAME = "filename";

    @Mock
    private FileReader fileReader;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private InputStream inputStream;
    @Mock
    private PrintWriter writer;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private Path path;

    @InjectMocks
    private ResourceFileNameHandler underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWriteResourceFileShouldWriteInputStreamToOutputStream() throws IOException {
        //GIVEN
        given(fileReader.readFile(FILE_NAME, path)).willReturn(inputStream);
        given(response.getOutputStream()).willReturn(outputStream);
        given(inputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
        //WHEN
        underTest.writeResourceFile(response, PATH_INFO, path);
        //THEN
        verify(outputStream).write(Mockito.any(byte[].class), Mockito.eq(0), Mockito.eq(1));
    }

    @Test
    public void testWriteResourceFileShouldSetHeaders() throws IOException {
        //GIVEN
        given(fileReader.readFile(FILE_NAME, path)).willReturn(inputStream);
        given(response.getOutputStream()).willReturn(outputStream);
        given(inputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
        //WHEN
        underTest.writeResourceFile(response, PATH_INFO, path);
        //THEN
        verify(response).setContentType(TEXT);
        verify(response).setHeader(CONTENT_DISPOSITION, "attachment;filename=" + FILE_NAME);
    }

    @Test
    public void testWriteResourceFileShouldCloseOutputStream() throws IOException {
        //GIVEN
        given(fileReader.readFile(FILE_NAME, path)).willReturn(inputStream);
        given(response.getOutputStream()).willReturn(outputStream);
        given(inputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
        //WHEN
        underTest.writeResourceFile(response, PATH_INFO, path);
        //THEN
        verify(outputStream).flush();
        verify(outputStream).close();
    }

    @Test
    public void testWriteResourceFileWhenFileDoesNotExistShouldWriteErrorResponse() throws IOException {
        //GIVEN
        given(fileReader.readFile(FILE_NAME, path)).willReturn(null);
        given(response.getWriter()).willReturn(writer);
        //WHEN
        underTest.writeResourceFile(response, PATH_INFO, path);
        //THEN
        verify(response).setContentType(HTML);
        verify(writer).write(FILE_NOT_FOUND_MSG);
        verify(writer).flush();
        verify(writer).close();
    }
}
