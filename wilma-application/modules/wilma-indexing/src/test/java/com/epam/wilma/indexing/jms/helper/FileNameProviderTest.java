package com.epam.wilma.indexing.jms.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

/**
 * Unit tests for the class {@link FileNameProvider}.
 * @author Tunde_Kovacs
 *
 */
public class FileNameProviderTest {

    private static final String WILMA_MESSAGE_LOGGER_ID = "wilma_message_logger_id";
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private LogFilePathProvider logFilePathProvider;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private WilmaHttpResponse response;
    @Mock
    private Path path;

    @InjectMocks
    private FileNameProvider underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(logFilePathProvider.getLogFilePath().toAbsolutePath()).willReturn(path);
    }

    @Test
    public void testGetFileNameShouldReturnRequestFileName() {
        //GIVEN
        given(request.getWilmaMessageLoggerId()).willReturn(WILMA_MESSAGE_LOGGER_ID + "req");
        //WHEN
        String actual = underTest.getFileName(request);
        //THEN
        assertEquals(actual, "path/" + WILMA_MESSAGE_LOGGER_ID + "req.txt");
    }

    @Test
    public void testGetFileNameShouldReturnResponseFileName() {
        //GIVEN
        given(response.getWilmaMessageLoggerId()).willReturn(WILMA_MESSAGE_LOGGER_ID + "resp");
        //WHEN
        String actual = underTest.getFileName(response);
        //THEN
        assertEquals(actual, "path/" + WILMA_MESSAGE_LOGGER_ID + "resp.txt");
    }

}
