package com.epam.wilma.maintainer.task.filelimit;
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

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.indexing.jms.delete.JmsIndexDeletionProcessor;
import com.epam.wilma.maintainer.configuration.MaintainerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.DeletedFileProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link FileLimitMaintainerTask}.
 *
 * @author Marton_Sereg
 */
public class FileLimitMaintainerTaskTest {

    @InjectMocks
    private FileLimitMaintainerTask underTest;
    @Mock
    private Logger logger;
    @Mock
    private FileFilter fileFilter;
    @Mock
    private LogFilePathProvider logFilePath;
    @Mock
    private Path path;
    @Mock
    private File logFolder;
    @Mock
    private MaintainerConfigurationAccess configurationAccess;
    @Mock
    private MaintainerProperties properties;
    @Mock
    private JmsIndexDeletionProcessor indexDeletionProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(configurationAccess.getProperties()).willReturn(properties);
    }

    @Test
    public void testRunShouldDeleteFilesWhenThereAreMoreFilesInTheFolderThanTheLimit() {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fileLimit", null);
        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        ReflectionTestUtils.setField(underTest, "deletedFileProvider", deletedFileProvider);
        given(properties.getFileLimit()).willReturn(2);
        File file1 = Mockito.mock(File.class);
        given(file1.getName()).willReturn("20130702151251.0038resp");
        File file2 = Mockito.mock(File.class);
        given(file2.getName()).willReturn("20130702151251.0037req");
        File file3 = Mockito.mock(File.class);
        given(file3.getName()).willReturn("20130702151251.0038req");
        File file4 = Mockito.mock(File.class);
        given(file4.getName()).willReturn("20130702151251.0037resp");
        File[] messageFiles = new File[4];
        messageFiles[0] = file1;
        messageFiles[1] = file2;
        messageFiles[2] = file3;
        messageFiles[3] = file4;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);
        // WHEN
        underTest.run();
        // THEN
        Mockito.verify(file2).delete();
        Mockito.verify(file4).delete();
    }

    @Test
    public void testRunShouldNotDeleteFilesWhenThereAreNotMoreFilesInTheFolderThanTheLimit() {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fileLimit", 4);
        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        ReflectionTestUtils.setField(underTest, "deletedFileProvider", deletedFileProvider);
        given(properties.getFileLimit()).willReturn(4);
        File file1 = mock(File.class);
        given(file1.getName()).willReturn("20130702151251.0038resp");
        File file2 = mock(File.class);
        given(file2.getName()).willReturn("20130702151251.0037req");
        File file3 = mock(File.class);
        given(file3.getName()).willReturn("20130702151251.0038req");
        File file4 = mock(File.class);
        given(file4.getName()).willReturn("20130702151251.0037resp");
        File[] messageFiles = new File[4];
        messageFiles[0] = file1;
        messageFiles[1] = file2;
        messageFiles[2] = file3;
        messageFiles[3] = file4;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);
        // WHEN
        underTest.run();
        // THEN
        verify(file1, never()).delete();
        verify(file2, never()).delete();
        verify(file3, never()).delete();
        verify(file4, never()).delete();
    }

    @Test
    public void testRunShouldSendFileForDeletionFromIndex() {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fileLimit", null);
        given(properties.getFileLimit()).willReturn(0);
        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        ReflectionTestUtils.setField(underTest, "deletedFileProvider", deletedFileProvider);

        File file1 = mock(File.class);
        File[] messageFiles = new File[1];
        messageFiles[0] = file1;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);
        given(file1.delete()).willReturn(true);
        given(file1.getAbsolutePath()).willReturn("path");
        // WHEN
        underTest.run();
        // THEN
        verify(indexDeletionProcessor).process("path");
    }

    @Test
    public void testRunShouldNotDoAnythingWhenThereAreNoFilesInTheFolder() {
        // GIVEN
        File[] messageFiles = null;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);
        // WHEN
        underTest.run();
        // THEN NullPointerException is not thrown
    }

    @Test
    public void testLogParameters() {
        // GIVEN
        given(properties.getFileLimit()).willReturn(4);
        ReflectionTestUtils.setField(underTest, "logger", logger);
        // WHEN
        underTest.logParameters();
        // THEN
        verify(logger).info("Filelimit method is used to maintain log files with parameters: filelimit: {}", 4);
    }

    @Test
    public void testRunShouldSaveDeleteFilesCountWhenItDeleteFiles() {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fileLimit", null);
        given(properties.getFileLimit()).willReturn(2);
        File file1 = Mockito.mock(File.class);
        given(file1.getName()).willReturn("20130702151251.0038resp");
        File file2 = Mockito.mock(File.class);
        given(file2.getName()).willReturn("20130702151251.0037req");
        File file3 = Mockito.mock(File.class);
        given(file3.getName()).willReturn("20130702151251.0038req");
        File file4 = Mockito.mock(File.class);
        given(file4.getName()).willReturn("20130702151251.0037resp");
        File[] messageFiles = new File[4];
        messageFiles[0] = file1;
        messageFiles[1] = file2;
        messageFiles[2] = file3;
        messageFiles[3] = file4;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);

        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        ReflectionTestUtils.setField(underTest, "deletedFileProvider", deletedFileProvider);
        // WHEN
        underTest.run();
        // THEN
        deletedFileProvider = (DeletedFileProvider) ReflectionTestUtils.getField(underTest, "deletedFileProvider");
        assert deletedFileProvider != null;
        int actual = deletedFileProvider.getDeletedFilesCount();
        assertEquals(2, actual);
    }

    @Test
    public void testRunShouldSaveZeroWhenItDoNotDeleteFiles() {
        // GIVEN
        ReflectionTestUtils.setField(underTest, "fileLimit", null);
        given(properties.getFileLimit()).willReturn(4);
        File file1 = Mockito.mock(File.class);
        given(file1.getName()).willReturn("20130702151251.0038resp");
        File file2 = Mockito.mock(File.class);
        given(file2.getName()).willReturn("20130702151251.0037req");
        File file3 = Mockito.mock(File.class);
        given(file3.getName()).willReturn("20130702151251.0038req");
        File file4 = Mockito.mock(File.class);
        given(file4.getName()).willReturn("20130702151251.0037resp");
        File[] messageFiles = new File[4];
        messageFiles[0] = file1;
        messageFiles[1] = file2;
        messageFiles[2] = file3;
        messageFiles[3] = file4;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);

        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        ReflectionTestUtils.setField(underTest, "deletedFileProvider", deletedFileProvider);
        // WHEN
        underTest.run();
        // THEN
        deletedFileProvider = (DeletedFileProvider) ReflectionTestUtils.getField(underTest, "deletedFileProvider");
        assert deletedFileProvider != null;
        int actual = deletedFileProvider.getDeletedFilesCount();
        assertEquals(0, actual);
    }
}
