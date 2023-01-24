package com.epam.wilma.message.search.web.controller;
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

import com.epam.wilma.message.search.web.service.LogFileProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

/**
 * Unit test for {@link AppLogController}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class AppLogControllerTest {

    private static final String JSON_NAME = "files";
    private static final String NOT_IMPORTANT = "";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_TEMPLATE = "attachment; filename=%s";
    @Mock
    private LogFileProvider logFileProvider;

    @InjectMocks
    private AppLogController underTest;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLogFilesShouldRespondWithLogFilesWithJsonName() {
        //GIVEN
        Map<String, Collection<String>> expected = new HashMap<>();
        Collection<String> fileNames = new ArrayList<>();
        fileNames.add("a");
        expected.put(JSON_NAME, fileNames);
        given(logFileProvider.getLogFileNames()).willReturn(fileNames);
        //WHEN
        Map<String, Collection<String>> result = underTest.getLogFiles();
        //THEN
        assertEquals(expected, result);
    }

    @Test
    public void testGetLogFileContentWhenSourceIsTrueShouldNotSetContentDisposition() {
        //GIVEN
        String expectedBody = "content";
        String fileName = "something";
        given(logFileProvider.getLogContent(fileName)).willReturn(expectedBody);
        //WHEN
        ResponseEntity<String> result = underTest.getLogFileContent(fileName, true, NOT_IMPORTANT);
        //THEN
        assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getHeaders().get(CONTENT_DISPOSITION));
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    public void testGetLogFileContentWhenSourceIsFalseShouldSetContentDispositionToAttachment() {
        //GIVEN
        String expectedBody = "content";
        String fileName = "something";
        given(logFileProvider.getLogContent(fileName)).willReturn(expectedBody);
        //WHEN
        ResponseEntity<String> result = underTest.getLogFileContent(fileName, false, NOT_IMPORTANT);
        //THEN
        assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(String.format(ATTACHMENT_TEMPLATE, fileName), result.getHeaders().getFirst(CONTENT_DISPOSITION));
        assertEquals(expectedBody, result.getBody());
    }

    @Test
    public void testGetLogFileContentWhenUserIsOnWindowsShouldConvertLineBreaks() {
        //GIVEN
        String expectedBody = "content\r\n";
        String userAgentWindows = "SOMETHINGSOMETHING-WINDOWS";
        String body = "content\n";
        String fileName = "something";
        given(logFileProvider.getLogContent(fileName)).willReturn(body);
        //WHEN
        ResponseEntity<String> result = underTest.getLogFileContent(fileName, true, userAgentWindows);
        //THEN
        assertEquals(expectedBody, result.getBody());
    }
}
