package com.epam.wilma.message.search.lucene.index;
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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.lucene.index.helper.FileFactory;

/**
 * Unit tests for the class {@link LuceneIndexEngine}.
 * @author Tunde_Kovacs
 *
 */
public class LuceneIndexEngineTest {

    private static final String FOLDER_PATH = "src/test/resources/test-folder1";
    private File docDir;

    @Mock
    private FolderIndexer folderIndexer;
    @Mock
    private FileFactory fileFactory;
    @Mock
    private Logger logger;
    @Mock
    private File mockFolder;
    @Mock
    private File file;

    @InjectMocks
    private LuceneIndexEngine underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        docDir = new File(FOLDER_PATH);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testCreateIndexShouldCallFolderIndexer() {
        //GIVEN
        given(fileFactory.createFile(FOLDER_PATH)).willReturn(docDir);
        //WHEN
        underTest.createIndex(FOLDER_PATH);
        //THEN
        verify(folderIndexer).indexFolder(docDir);
    }

    @Test
    public void testCreateIndexWhenDocDirDoesNotExistShouldLogError() {
        //GIVEN
        given(fileFactory.createFile(FOLDER_PATH)).willReturn(mockFolder);
        //WHEN
        underTest.createIndex(FOLDER_PATH);
        //THEN
        verify(logger).error(Mockito.anyString());
    }

    @Test
    public void testCreateIndexWhenDocDirCannotBeReadShouldLogError() {
        //GIVEN
        given(mockFolder.exists()).willReturn(true);
        given(fileFactory.createFile(FOLDER_PATH)).willReturn(mockFolder);
        //WHEN
        underTest.createIndex(FOLDER_PATH);
        //THEN
        verify(logger).error(Mockito.anyString());
    }

    @Test
    public void testAddFileToIndex() {
        //GIVEN
        given(fileFactory.createFile(FOLDER_PATH)).willReturn(file);
        //WHEN
        underTest.addFileToIndex(FOLDER_PATH);
        //THEN
        verify(folderIndexer).indexFolder(file);
    }
}
