package com.epam.wilma.message.search.web.support;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Provides unit tests for the class {@link FileZipper}.
 * @author Tibor_Kovacs
 *
 */
public class FileZipperTest {

    @Mock
    private OutputStream outputStream;
    @Mock
    private ZipOutputStream zipOutputStream;
    @Mock
    private ZipOutputStreamFactory zipOutputStreamFactory;
    @Mock
    private ZipEntryFactory entryFactory;
    @Mock
    private ZipEntry zipEntry;

    @InjectMocks
    private FileZipper underTest;

    private List<List<String>> filePaths;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        filePaths = new ArrayList<>();
        given(zipOutputStreamFactory.createZipOutputStream(outputStream)).willReturn(zipOutputStream);
        given(entryFactory.createZipEntry(Mockito.anyString())).willReturn(zipEntry);
    }

    @Test
    public void testCreateZipWithFilesWhenFilePathsIsNotEmptyList() throws IOException {
        //GIVEN
        List<String> pair = new ArrayList<String>();
        pair.add("src/test/resources/first_testreq.txt");
        pair.add("src/test/resources/first_testresp.txt");
        filePaths.add(pair);
        //WHEN
        underTest.createZipWithFiles(filePaths, outputStream);
        //THEN
        verify(entryFactory).createZipEntry("first_testreq.txt");
        verify(entryFactory).createZipEntry("first_testresp.txt");
        verify(zipOutputStream, times(2)).putNextEntry(zipEntry);
        verify(zipOutputStream).close();
    }

    @Test
    public void testCreateZipWithFilesWhenFilePathContainsBackSlashes() throws IOException {
        //GIVEN
        List<String> pair = new ArrayList<String>();
        pair.add(FilenameUtils.separatorsToSystem("src\\test\\resources\\first_testreq.txt"));
        filePaths.add(pair);
        //WHEN
        underTest.createZipWithFiles(filePaths, outputStream);
        //THEN
        verify(entryFactory).createZipEntry("first_testreq.txt");
        verify(zipOutputStream).putNextEntry(zipEntry);
        verify(zipOutputStream).close();
    }

    @Test
    public void testCreateZipWithFilesWhenFileNotExists() throws IOException {
        //GIVEN
        List<String> pair = new ArrayList<String>();
        pair.add("src\\test\\resources\\testreq.txt");
        filePaths.add(pair);
        //WHEN
        underTest.createZipWithFiles(filePaths, outputStream);
        //THEN
        verify(entryFactory, times(0)).createZipEntry("first_testreq.txt");
        verify(zipOutputStream, times(0)).putNextEntry(zipEntry);
        verify(zipOutputStream).close();
    }
}
