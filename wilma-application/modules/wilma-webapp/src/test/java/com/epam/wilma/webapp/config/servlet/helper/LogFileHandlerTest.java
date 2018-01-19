package com.epam.wilma.webapp.config.servlet.helper;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.FileListJsonProperties;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;

/**
 * Provides unit tests for the class {@link LogFileHandler}.
 * @author Tunde_Kovacs
 *
 */
public class LogFileHandlerTest {

    private static final String FILE_CONTENT = "content";
    @Mock
    private FileListJsonBuilder messageFileListJsonBuilder;
    @Mock
    private FileReader messageFileReader;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private FileInputStream fileInputStream;
    @Mock
    private Path path;
    @Mock
    private File file;
    @Mock
    private InputStreamUtil inputStreamConverter;
    @Mock
    private WebAppConfigurationAccess configurationAccess;
    @Mock
    private FileListJsonProperties properties;
    @Mock
    private PropertyDTO propertyDTO;

    @InjectMocks
    private LogFileHandler underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(response.getWriter()).willReturn(printWriter);
        given(inputStreamConverter.toString(fileInputStream)).willReturn(FILE_CONTENT);
    }

    @Test
    public final void testWriteFileNamesToResponseShouldWriteFileNames() throws IOException {
        // GIVEN
        given(path.toFile()).willReturn(file);
        given(messageFileListJsonBuilder.buildLogFileListJson(file)).willReturn("files");
        given(request.getHeader("User-Agent")).willReturn("windows");
        // WHEN
        underTest.writeFileNamesToResponse(response, path);
        // THEN
        verify(response).setContentType("application/json");
        verify(printWriter).write("files");
        verify(printWriter).flush();
        verify(printWriter).close();
    }



    @Test
    public final void testWriteFileContentToResponseShouldWriteErrorMessageWhenPathIsAnInvalidFileName() throws IOException {
        // GIVEN
        given(messageFileReader.readFile("invalidfilename", path)).willReturn(null);
        // WHEN
        underTest.writeFileContentToResponse(request, response, "invalidfilename", path);
        // THEN
        verify(response).setContentType("text/html");
        verify(printWriter).write("Requested file not found.");
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public final void testWriteFileContentToResponseShouldSendFileWhenPathIsAValidFileName() throws IOException {
        // GIVEN
        given(messageFileReader.readFile("filename", path)).willReturn(fileInputStream);
        given(request.getHeader("User-Agent")).willReturn("windows");
        // WHEN
        underTest.writeFileContentToResponse(request, response, "/filename", path);
        // THEN
        verify(response).setContentType("text/plain");
        verify(response).setHeader("Content-Disposition", "attachment;filename=filename");
        verify(printWriter).write(FILE_CONTENT);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public final void testWriteFileContentToResponseShouldSendFileAsAttachmentWhenPathIsAValidFileName() throws IOException {
        // GIVEN
        given(request.getPathInfo()).willReturn("/filename");
        given(messageFileReader.readFile("filename", path)).willReturn(fileInputStream);
        given(request.getHeader("User-Agent")).willReturn("windows");
        // WHEN
        underTest.writeFileContentToResponse(request, response, "/filename", path);
        // THEN
        verify(response).setContentType("text/plain");
        verify(response).setHeader("Content-Disposition", "attachment;filename=filename");
        verify(printWriter).write(FILE_CONTENT);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public final void testWriteFileContentToResponseShouldSendFileContentWhenTheSourceParameterIsTrue() throws IOException {
        // GIVEN
        given(messageFileReader.readFile("filename", path)).willReturn(fileInputStream);
        given(request.getParameter("source")).willReturn("true");
        given(request.getHeader("User-Agent")).willReturn("windows");
        // WHEN
        underTest.writeFileContentToResponse(request, response, "/filename", path);
        // THEN
        verify(response).setContentType("text/plain");
        verify(printWriter).write(FILE_CONTENT);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public final void testWriteFileContentToResponseShouldSendFileAsAttachmentWhenTheSourceParameterIsNotTrue() throws IOException {
        // GIVEN
        given(messageFileReader.readFile("filename", path)).willReturn(fileInputStream);
        given(request.getParameter("source")).willReturn("");
        given(request.getHeader("User-Agent")).willReturn("windows");
        // WHEN
        underTest.writeFileContentToResponse(request, response, "/filename", path);
        // THEN
        verify(response).setContentType("text/plain");
        verify(response).setHeader("Content-Disposition", "attachment;filename=filename");
        verify(printWriter).write(FILE_CONTENT);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public final void testWriteFileContentToResponseShouldNotReplaceNewLineWhenNotOnWindows() throws IOException {
        // GIVEN
        given(messageFileReader.readFile("filename", path)).willReturn(fileInputStream);
        given(request.getParameter("source")).willReturn("");
        given(request.getHeader("User-Agent")).willReturn("mac");
        String fileContent = "multi\nline";
        given(inputStreamConverter.toString(fileInputStream)).willReturn(fileContent);
        // WHEN
        underTest.writeFileContentToResponse(request, response, "/filename", path);
        // THEN
        verify(printWriter).write(fileContent);
    }

    @Test
    public final void testWriteFileContentToResponseShouldNotReplaceNewLineWhenNoUserAgentHeader() throws IOException {
        // GIVEN
        given(messageFileReader.readFile("filename", path)).willReturn(fileInputStream);
        given(request.getParameter("source")).willReturn("");
        given(request.getHeader("User-Agent")).willReturn(null);
        String fileContent = "multi\nline";
        given(inputStreamConverter.toString(fileInputStream)).willReturn(fileContent);
        // WHEN
        underTest.writeFileContentToResponse(request, response, "/filename", path);
        // THEN
        verify(printWriter).write(fileContent);
    }
}
