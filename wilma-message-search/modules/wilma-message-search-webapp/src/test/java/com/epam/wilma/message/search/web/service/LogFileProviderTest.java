package com.epam.wilma.message.search.web.service;
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

import static org.mockito.BDDMockito.given;
import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.web.support.FileUtils;

/**
 * Unit test for {@link LogFileProvider}.
 * @author Adam_Csaba_Kiraly
 */
public class LogFileProviderTest {

    private static final String LOG_EXTENSION = "txt";
    private static final String LOG_PATH = "log";
    private static final String FILE_NOT_FOUND_MESSAGE = "File not found!";
    private static final String ERROR_MESSAGE = "Error occurred while reading file!";

    @Mock
    private FileUtils fileUtils;

    @InjectMocks
    private LogFileProvider underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLogFileNames() {
        Collection<File> files = getFileCollection();
        Collection<String> expectedNames = new ArrayList<>();
        expectedNames.add("a");
        expectedNames.add("b");
        expectedNames.add("c");
        given(fileUtils.getFilesWithExtension(LOG_PATH, LOG_EXTENSION)).willReturn(files);
        //WHEN
        Collection<String> result = underTest.getLogFileNames();
        //THEN
        assertEquals(expectedNames, result);
    }

    @Test
    public void testGetLogContentWhenFileDoesNotExistShouldReturnFileNotFoundMessage() {
        //GIVEN
        Collection<File> files = getFileCollection();
        String fileName = "non-existent-file";
        given(fileUtils.getFilesWithExtension(LOG_PATH, LOG_EXTENSION)).willReturn(files);
        //WHEN
        String result = underTest.getLogContent(fileName);
        //THEN
        assertEquals(FILE_NOT_FOUND_MESSAGE, result);
    }

    @Test
    public void testGetLogContent() throws IOException {
        //GIVEN
        String expectedFileContent = "content";
        Collection<File> files = new ArrayList<>();
        String fileName = "a";
        File file = new File("a");
        files.add(file);
        given(fileUtils.getFilesWithExtension(LOG_PATH, LOG_EXTENSION)).willReturn(files);
        given(fileUtils.readFileToString(file)).willReturn(expectedFileContent);
        //WHEN
        String result = underTest.getLogContent(fileName);
        //THEN
        assertEquals(expectedFileContent, result);
    }

    @Test
    public void testGetLogContentWhenErrorOccursShouldReturnErrorMessage() throws IOException {
        //GIVEN
        Collection<File> files = new ArrayList<>();
        String fileName = "a";
        File file = new File("a");
        files.add(file);
        given(fileUtils.getFilesWithExtension(LOG_PATH, LOG_EXTENSION)).willReturn(files);
        given(fileUtils.readFileToString(file)).willThrow(new IOException());
        //WHEN
        String result = underTest.getLogContent(fileName);
        //THEN
        assertEquals(ERROR_MESSAGE, result);
    }

    private Collection<File> getFileCollection() {
        Collection<File> files = new ArrayList<>();
        files.add(new File("a"));
        files.add(new File("b"));
        files.add(new File("c"));
        return files;
    }
}
