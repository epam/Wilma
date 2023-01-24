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

import com.epam.wilma.message.search.lucene.index.helper.FileWrapper;
import com.epam.wilma.message.search.lucene.index.helper.FileWrapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link FolderIndexer}.
 *
 * @author Tunde_Kovacs
 */
public class FolderIndexerTest {

    private static final String FILE_PATH = "src/test/resources/test-folder1/test-file.txt";

    @Mock
    private FileWrapper file;
    @Mock
    private FileWrapper folder1;
    @Mock
    private FileWrapper folder2;
    @Mock
    private FileIndexer fileIndexer;

    @Mock
    private FileWrapperFactory fileWrapperFactory;

    @InjectMocks
    private FolderIndexer underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(folder1.isDirectory()).willReturn(true);
        given(folder1.exists()).willReturn(true);
        given(folder1.canRead()).willReturn(true);
        String[] list = new String[] {FILE_PATH};
        given(folder1.list()).willReturn(list);
        given(folder2.isDirectory()).willReturn(true);
        given(folder2.exists()).willReturn(true);
        given(folder2.canRead()).willReturn(true);
        given(folder2.list()).willReturn(null);
        given(file.isDirectory()).willReturn(false);
        given(file.exists()).willReturn(true);
        given(file.canRead()).willReturn(true);
        given(fileWrapperFactory.createFile(any(), anyString())).willReturn(file);
        ReflectionTestUtils.setField(underTest, "fileWrapperFactory", fileWrapperFactory);
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
        given(folder1.canRead()).willReturn(false);
        //WHEN
        underTest.indexFolder(folder1);
        //THEN
        verify(fileIndexer, never()).indexFile(any());
    }

    @Test
    public void testIndexFolderWhenFolderEmptyShouldDoNothing() {
        //GIVEN
        //WHEN
        underTest.indexFolder(folder2);
        //THEN
        verify(fileIndexer, never()).indexFile(file);
    }
}
