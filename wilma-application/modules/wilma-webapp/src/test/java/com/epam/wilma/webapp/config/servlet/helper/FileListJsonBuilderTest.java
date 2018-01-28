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
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileUtils;

/**
 * Test class for {@link FileListJsonBuilder}.
 * @author Marton_Sereg
 * @author Tunde_Kovacs
 *
 */
public class FileListJsonBuilderTest {

    @Mock
    private File file;
    @Mock
    private FileUtils fileUtilsWrapper;
    @Mock
    private File directory;
    @Mock
    private File logFile;
    @Mock
    private File simpleFile;

    @InjectMocks
    private FileListJsonBuilder underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testBuildLogFileListJsonShouldCreateProperJson() {
        //GIVEN
        String expected = "{\"files\":[\"20130829103650.0000resp.txt\",\"20130829103651.0000req.txt\"]}";
        String[] fileNames = {"20130829103651.0000req.txt", "20130829103650.0000resp.txt"};
        given(file.list()).willReturn(fileNames);
        //WHEN
        String actual = underTest.buildLogFileListJson(file);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildLogFileListJsonShouldCreateProperJsonWhenNoFilesAvailable() {
        //GIVEN
        String expected = "{\"files\":[]}";
        String[] fileNames = {};
        given(file.list()).willReturn(fileNames);
        //WHEN
        String actual = underTest.buildLogFileListJson(file);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildLogFileListJsonShouldCreateEmptyJsonWhenDirectoryIsNotADirectory() {
        //GIVEN
        String expected = "{\"files\":[]}";
        given(file.list()).willReturn(null);
        //WHEN
        String actual = underTest.buildLogFileListJson(file);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildFileListJsonShouldCreateProperJsonWhenDirectoryIncluded() {
        //GIVEN
        String expected = "{\"files\":[\"File1.class\",\"File2.class\",\"File3.class\"]}";
        Collection<File> filesInDirectory = new ArrayList<File>();
        filesInDirectory.add(new File("folder/File1.class"));
        filesInDirectory.add(new File("folder/File2.class"));
        filesInDirectory.add(new File("folder/File3.class"));
        given(fileUtilsWrapper.listFiles(directory)).willReturn(filesInDirectory);
        //WHEN
        String result = underTest.buildFileListJson(directory);
        //THEN
        assertEquals(result, expected);
    }

    @Test
    public final void testBuildFileListJsonShouldCreateProperJsonWhenNoFilesAvailable() {
        //GIVEN
        String expected = "{\"files\":[]}";
        String[] fileNames = {};
        given(file.list()).willReturn(fileNames);
        //WHEN
        String actual = underTest.buildFileListJson(file);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildFileListJsonShouldEmptyJsonWhenDirectoryIsNotADirectory() {
        //GIVEN
        String expected = "{\"files\":[]}";
        given(file.list()).willReturn(null);
        //WHEN
        String actual = underTest.buildFileListJson(file);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildMessageFileListJson() {
        //GIVEN
        String expected = "{\"files\":[\"20130829103651.0000req.txt\"]}";
        String[] fileNames = {"20130829103651.0000req.txt", "20130829103650.0000resp.txt"};
        given(file.list()).willReturn(fileNames);
        //WHEN
        String actual = underTest.buildMessageFileListJson(file, 1);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildMessageFileListJsonWhenThereAreFewerMessagesThanTheLimit() {
        //GIVEN
        String expected = "{\"files\":[\"20130829103650.0000resp.txt\",\"20130829103651.0000req.txt\"]}";
        String[] fileNames = {"20130829103651.0000req.txt", "20130829103650.0000resp.txt"};
        given(file.list()).willReturn(fileNames);
        //WHEN
        String actual = underTest.buildMessageFileListJson(file, 5);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildMessageFileListJsonShouldCreateProperJsonWhenNoFilesAvailable() {
        //GIVEN
        String expected = "{\"files\":[]}";
        String[] fileNames = {};
        given(file.list()).willReturn(fileNames);
        //WHEN
        String actual = underTest.buildMessageFileListJson(file, 5);
        //THEN
        assertEquals(actual, expected);
    }

    @Test
    public final void testBuildMessageFileListJsonShouldCreateEmptyJsonWhenDirectoryIsNotADirectory() {
        //GIVEN
        String expected = "{\"files\":[]}";
        given(file.list()).willReturn(null);
        //WHEN
        String actual = underTest.buildMessageFileListJson(file, 5);
        //THEN
        assertEquals(actual, expected);
    }
}
