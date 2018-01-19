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
 * Unit tests for {@link MessageFileHandler}.
 * @author Tibor_Kovacs
 */
public class MessageFileHandlerTest {
    private static final String APPLICATION_JSON = "application/json";
    @Mock
    private PrintWriter printWriter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private WebAppConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;
    @Mock
    private FileListJsonProperties fileListProperties;
    @Mock
    private Path path;
    @Mock
    private File file;
    @Mock
    private LogFileHandler logFileHandler;
    @Mock
    private FileListJsonBuilder messageFileJsonBuilder;

    @InjectMocks
    private MessageFileHandler underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(response.getWriter()).willReturn(printWriter);
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getFileListProperties()).willReturn(fileListProperties);
    }

    @Test
    public void testWriteFileNamesToResponse() throws IOException {
        //GIVEN
        String expectedJson = "{\"test\":\"test\"}";
        given(fileListProperties.getMaximumCountOfMessages()).willReturn(10);
        given(path.toFile()).willReturn(file);
        given(messageFileJsonBuilder.buildMessageFileListJson(file, 10)).willReturn(expectedJson);
        //WHEN
        underTest.writeFileNamesToResponse(response, path);
        //THEN
        verify(response).setContentType(APPLICATION_JSON);
        verify(messageFileJsonBuilder).buildMessageFileListJson(file, 10);
        verify(printWriter).write(expectedJson);
        verify(printWriter).flush();
    }

    @Test
    public void testWriteFileContentToResponse() throws IOException {
        //GIVEN
        String pathInfo = "test";
        //WHEN
        underTest.writeFileContentToResponse(request, response, pathInfo, path);
        //THEN
        verify(logFileHandler).writeFileContentToResponse(request, response, pathInfo, path);
    }

}
