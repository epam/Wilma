package com.epam.wilma.stubconfig.cache.cleaner.helper;
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

import com.epam.wilma.common.helper.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link StubConfigPathProvider}.
 *
 * @author Tibor_Kovacs
 */
public class StubConfigPathProviderTest {

    @Mock
    private FileUtils fileUtils;

    private List<String> expectedFilePaths;

    @InjectMocks
    private StubConfigPathProvider underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Collection<File> files = new ArrayList<>();
        files.add(new File("first.json"));
        files.add(new File("second.json"));
        expectedFilePaths = new ArrayList<>();
        expectedFilePaths.add("first.json");
        expectedFilePaths.add("second.json");
        given(fileUtils.listFilesWithFilter(Mockito.any(File.class), Mockito.anyString())).willReturn(files);
    }

    @Test
    public void testGetConfigPathsFromCache() {
        //GIVEN
        //WHEN
        List<String> result = underTest.getConfigPathsFromCache("Test");
        //THEN
        verify(fileUtils).listFilesWithFilter(Mockito.any(File.class), Mockito.anyString());
        assertEquals(result, expectedFilePaths);
    }

    @Test
    public void testGetConfigPathsFromSpecificFolder() {
        //GIVEN
        //WHEN
        List<String> result = underTest.getConfigPathsFromSpecificFolder("test/path", "*TestPattern");
        //THEN
        verify(fileUtils).listFilesWithFilter(Mockito.any(File.class), Mockito.anyString());
        assertEquals(result, expectedFilePaths);
    }

    @Test
    public void testGetConfigPathsFromSpecificFolderWhenThePatternIsASpecificFileNameAndThisIsInASpecificFolder() {
        //GIVEN
        expectedFilePaths = new ArrayList<>();
        expectedFilePaths.add(FilenameUtils.separatorsToSystem("test/path/TestPattern"));
        //WHEN
        List<String> result = underTest.getConfigPathsFromSpecificFolder("test/path", "TestPattern");
        //THEN
        assertEquals(result, expectedFilePaths);
    }

    @Test
    public void testGetConfigPathsFromSpecificFolderWhenThePatternIsASpecificFileNameAndThisIsInTheRootFolder() {
        //GIVEN
        expectedFilePaths = new ArrayList<>();
        expectedFilePaths.add("TestPattern");
        //WHEN
        List<String> result = underTest.getConfigPathsFromSpecificFolder("", "TestPattern");
        //THEN
        assertEquals(result, expectedFilePaths);
    }

}
