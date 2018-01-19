package com.epam.wilma.stubconfig.initializer.template;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;

/**
 * Test class for {@link FilePathReader}.
 * @author Tamas_Bihari
 *
 */
public class FilePathReaderTest {
    private static final String SHORT_PATH = "MOCK_SHORT_PATH_WITH_FILE_NAME";
    private static final String PATH = "MOCK_PATH";
    @Mock
    private FileFactory fileFactory;
    @Mock
    private File file;
    @Mock
    private Collection<String> filePaths;
    @Mock
    private FileUtils fileUtils;

    @InjectMocks
    private FilePathReader underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFilePathsShouldReturnWithEmptyListWhenTargetFolderIsEmpty() {
        //GIVEN
        given(fileFactory.createFile(PATH)).willReturn(file);
        given(fileUtils.listFiles(file)).willReturn(new ArrayList<File>());
        //WHEN
        List<String> actual = underTest.getFilePaths(PATH);
        //THEN
        Assert.assertEquals(actual.size(), 0);
    }

    @Test
    public void testGetFilePathsShouldReturnListWithOneElementWhenTargetFolderContainsOneFile() {
        //GIVEN
        given(fileFactory.createFile(PATH)).willReturn(file);
        List<File> fileList = new ArrayList<File>();
        fileList.add(file);
        given(fileUtils.listFiles(file)).willReturn(fileList);
        given(file.toString()).willReturn(PATH);
        //WHEN
        List<String> actual = underTest.getFilePaths(PATH);
        //THEN
        Assert.assertEquals(actual.get(0), PATH);
    }

    @Test
    public void testGetShortFilePathsShouldReturnWithEmptyListWhenTargetFolderIsEmpty() {
        //GIVEN
        given(fileFactory.createFile(PATH)).willReturn(file);
        given(fileUtils.listFiles(file)).willReturn(new ArrayList<File>());
        //WHEN
        List<String> actual = underTest.getShortFilePaths(PATH);
        //THEN
        Assert.assertEquals(actual.size(), 0);
    }

    @Test
    public void testGetShortFilePathsShouldReturnListWithOneElementWhenTargetFolderContainsOneFile() {
        //GIVEN
        given(fileFactory.createFile(PATH)).willReturn(file);
        List<File> fileList = new ArrayList<File>();
        fileList.add(file);
        given(fileUtils.listFiles(file)).willReturn(fileList);
        given(file.toString()).willReturn(PATH + "\\" + SHORT_PATH);
        //WHEN
        List<String> actual = underTest.getShortFilePaths(PATH);
        //THEN
        Assert.assertEquals(actual.get(0), SHORT_PATH);
    }

}
