package com.epam.wilma.webapp.config.servlet.logging;
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
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.webapp.config.servlet.helper.FileReader;
import com.epam.wilma.webapp.config.servlet.helper.LogFileHandler;

/**
 * Provides unit tests for the class {@link WilmaLogHandlerServlet}.
 * @author Tunde_Kovacs
 *
 */
public class WilmaLogHandlerServletTest {

    @Mock
    private FileReader messageFileReader;
    @Mock
    private LogFilePathProvider filePathProvider;
    @Mock
    private LogFileHandler logFileHandler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Path path;

    @InjectMocks
    private WilmaLogHandlerServlet underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "filePathProvider", filePathProvider);
        Whitebox.setInternalState(underTest, "logFileHandler", logFileHandler);
        given(filePathProvider.getAppLogFilePath()).willReturn(path);
    }

    @Test
    public final void testDoGetShouldCallWriteFileNamesToResponseWhenPathIsEmpty() throws ServletException, IOException {
        // GIVEN
        given(request.getPathInfo()).willReturn(null);
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(logFileHandler).writeFileNamesToResponse(response, path);
    }

    @Test
    public final void testDoGetShouldCallWriteFileContentToResponseWhenPathIsNotEmpty() throws ServletException, IOException {
        // GIVEN
        String pathInfo = "/filename";
        given(request.getPathInfo()).willReturn(null);
        given(request.getPathInfo()).willReturn(pathInfo);
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(logFileHandler).writeFileContentToResponse(request, response, pathInfo, path);
    }

    @Test
    public final void testDoPostShouldCallWriteFileContentToResponseWhenPathIsNotEmpty() throws ServletException, IOException {
        // GIVEN
        String pathInfo = "/filename";
        given(request.getPathInfo()).willReturn(null);
        given(request.getPathInfo()).willReturn(pathInfo);
        // WHEN
        underTest.doPost(request, response);
        // THEN
        verify(logFileHandler).writeFileContentToResponse(request, response, pathInfo, path);
    }

}
