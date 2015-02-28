package com.epam.wilma.message.search.lucene.index;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the class {@link FolderIndexer}.
 * @author Tunde_Kovacs
 *
 */
public class FolderIndexerTest {

    private static final String FILE_PATH = "src/test/resources/test-folder1/test-file.txt";
    private static final String FOLDER_PATH = "src/test/resources/test-folder1";
    private File file;
    private File folder1;

    @Mock
    private File folder2;

    @Mock
    private FileIndexer fileIndexer;

    @InjectMocks
    private FolderIndexer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        folder1 = new File(FOLDER_PATH);
        file = new File(FILE_PATH);
    }

    @Test
    public void testIndexFolderWhenFolderExistsShouldIndexFolder() {
        //GIVEN in setUp
        //WHEN
        underTest.indexFolder(folder1);
        //THEN
        verify(fileIndexer).indexFile(file);
    }

    @Test
    public void testIndexFolderWhenFolderCannotBeReadShouldDoNothing() {
        //GIVEN in setUp
        //WHEN
        underTest.indexFolder(new File("folder"));
        //THEN
        verify(fileIndexer, never()).indexFile(file);
    }

    @Test
    public void testIndexFolderWhenFolderEmptyShouldDoNothing() {
        //GIVEN
        given(folder2.canRead()).willReturn(true);
        given(folder2.isDirectory()).willReturn(true);
        given(folder2.list()).willReturn(null);
        //WHEN
        underTest.indexFolder(folder2);
        //THEN
        verify(fileIndexer, never()).indexFile(file);
    }
}
