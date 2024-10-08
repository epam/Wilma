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

import com.epam.wilma.common.stream.helper.FileInputStreamFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link FileReader}.
 *
 * @author Marton_Sereg
 */
public class FileReaderTest {

    private static final String FILE_NAME = "file";

    @InjectMocks
    private FileReader underTest;

    @Mock
    private FileInputStreamFactory fileInputStreamFactory;

    @Mock
    private Path messageFilePath;

    @Mock
    private Path messageDirPath;

    @Mock
    private File messageFile;

    @Mock
    private FileInputStream fileInputStream;

    @Mock
    private Logger logger;

    @Mock
    private Path path;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "logger", logger);
    }

    @Test
    public final void testReadFileShouldReturnNullWhenRequestedFileDoesNotExist() {
        // GIVEN
        given(path.resolve(FILE_NAME)).willReturn(messageFilePath);
        given(messageFilePath.toFile()).willReturn(messageFile);
        given(messageFile.isFile()).willReturn(true);
        given(messageFile.exists()).willReturn(false);
        // WHEN
        InputStream actual = underTest.readFile(FILE_NAME, path);
        // THEN
        assertNull(actual);
    }

    @Test
    public final void testReadFileShouldReturnNullWhenRequestedFileIsNotAFile() {
        // GIVEN
        given(path.resolve(FILE_NAME)).willReturn(messageFilePath);
        given(messageFilePath.toFile()).willReturn(messageFile);
        given(messageFile.isFile()).willReturn(false);
        given(messageFile.exists()).willReturn(false);
        // WHEN
        InputStream actual = underTest.readFile(FILE_NAME, path);
        // THEN
        assertNull(actual);
    }

    @Test
    public final void testReadFileShouldReturnFileInputStreamIfFileExists() throws FileNotFoundException {
        // GIVEN
        given(path.resolve(FILE_NAME)).willReturn(messageFilePath);
        given(messageFilePath.toFile()).willReturn(messageFile);
        given(messageFile.exists()).willReturn(true);
        given(messageFile.isFile()).willReturn(true);
        given(fileInputStreamFactory.createFileInputStream(messageFile)).willReturn(fileInputStream);
        // WHEN
        InputStream actual = underTest.readFile(FILE_NAME, path);
        // THEN
        assertEquals(actual, fileInputStream);
    }

    @Test
    public final void testReadFileShouldLogWarningWhenFileWasNotFoundAndReturnNull() throws FileNotFoundException {
        // GIVEN
        given(path.resolve(FILE_NAME)).willReturn(messageFilePath);
        given(messageFilePath.toFile()).willReturn(messageFile);
        given(messageFile.exists()).willReturn(true);
        given(messageFile.isFile()).willReturn(true);
        given(fileInputStreamFactory.createFileInputStream(messageFile)).willThrow(new FileNotFoundException());
        // WHEN
        InputStream actual = underTest.readFile(FILE_NAME, path);
        // THEN
        assertNull(actual);
        verify(logger).warn(anyString(), Mockito.any(FileNotFoundException.class));
    }

}
