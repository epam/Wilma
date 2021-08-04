package com.epam.wilma.maintainer.task.timelimit;
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

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.indexing.jms.delete.JmsIndexDeletionProcessor;
import com.epam.wilma.maintainer.configuration.MaintainerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.DeletedFileProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link TimeLimitMaintainerTask}.
 *
 * @author Marton_Sereg
 */
public class TimeLimitMaintainerTaskTest {

    @InjectMocks
    private TimeLimitMaintainerTask underTest;

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
    private CurrentDateProvider currentDateProvider;
    @Mock
    private MaintainerConfigurationAccess configurationAccess;
    private MaintainerProperties properties;
    @Mock
    private JmsIndexDeletionProcessor indexDeletionProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
        properties = new MaintainerProperties("", "timeLimit", 1, "10S");
        given(configurationAccess.getProperties()).willReturn(properties);
    }

    @Test
    public final void testRun() {
        // GIVEN
        Whitebox.setInternalState(underTest, "simpleDateFormat", new SimpleDateFormat("yyyyMMddHHmmss"));
        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        Whitebox.setInternalState(underTest, "deletedFileProvider", deletedFileProvider);
        File file1 = Mockito.mock(File.class);
        given(file1.getName()).willReturn("20130701151510.0000resp.txt");
        File file2 = Mockito.mock(File.class);
        given(file2.getName()).willReturn("20130701151510.0000req.txt");
        File file3 = Mockito.mock(File.class);
        given(file3.getName()).willReturn("20130701151513.0000req.txt");
        File file4 = Mockito.mock(File.class);
        given(file4.getName()).willReturn("20130701151513.0000resp.txt");
        File[] messageFiles = new File[4];
        messageFiles[0] = file1;
        messageFiles[1] = file2;
        messageFiles[2] = file3;
        messageFiles[3] = file4;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);

        Calendar cal = Calendar.getInstance();
        //note: sixth month is jul - starting from 0
        cal.set(2013, 6, 1, 15, 15, 21);
        Date date = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(date);
        // WHEN
        underTest.run();
        // THEN
        verify(file1).delete();
        verify(file2).delete();
        verify(file3, never()).delete();
        verify(file4, never()).delete();
    }

    @Test
    public final void testRunShouldSendMessageToIndexDeletion() {
        // GIVEN
        Whitebox.setInternalState(underTest, "simpleDateFormat", new SimpleDateFormat("yyyyMMddHHmmss"));
        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        Whitebox.setInternalState(underTest, "deletedFileProvider", deletedFileProvider);

        File file1 = Mockito.mock(File.class);
        given(file1.getAbsolutePath()).willReturn("20130701151510.0000resp.txt");
        given(file1.getName()).willReturn("20130701151510.0000resp.txt");
        File[] messageFiles = new File[1];
        messageFiles[0] = file1;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);
        given(file1.delete()).willReturn(true);

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 6, 1, 15, 15, 21);
        Date date = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(date);
        // WHEN
        underTest.run();
        // THEN
        verify(indexDeletionProcessor).process("20130701151510.0000resp.txt");
    }

    @Test
    public void testLogParameters() {
        // GIVEN
        properties = new MaintainerProperties("", "timeLimit", 1, "36H");
        given(configurationAccess.getProperties()).willReturn(properties);
        // WHEN
        underTest.logParameters();
        // THEN
        verify(logger).info("Timelimit method is used to maintain log files with parameters: timelimit: {}", "36H");
    }

    @Test
    public final void testRunShouldSaveDeleteFilesCountWhenItDeleteFiles() {
        // GIVEN
        Whitebox.setInternalState(underTest, "timeLimitInSeconds", 10);
        Whitebox.setInternalState(underTest, "simpleDateFormat", new SimpleDateFormat("yyyyMMddHHmmss"));
        DeletedFileProvider deletedFileProvider = new DeletedFileProvider();
        Whitebox.setInternalState(underTest, "deletedFileProvider", deletedFileProvider);
        File file1 = Mockito.mock(File.class);
        given(file1.getName()).willReturn("20130701151510.0000resp.txt");
        File file2 = Mockito.mock(File.class);
        given(file2.getName()).willReturn("20130701151510.0000req.txt");
        File file3 = Mockito.mock(File.class);
        given(file3.getName()).willReturn("20130701151513.0000req.txt");
        File file4 = Mockito.mock(File.class);
        given(file4.getName()).willReturn("20130701151513.0000resp.txt");
        File[] messageFiles = new File[4];
        messageFiles[0] = file1;
        messageFiles[1] = file2;
        messageFiles[2] = file3;
        messageFiles[3] = file4;

        given(logFilePath.getLogFilePath()).willReturn(path);
        given(path.toFile()).willReturn(logFolder);
        given(logFolder.listFiles(fileFilter)).willReturn(messageFiles);

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 6, 1, 15, 15, 21);
        Date date = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(date);
        // WHEN
        underTest.run();
        // THEN
        int actual = ((DeletedFileProvider) Whitebox.getInternalState(underTest, "deletedFileProvider")).getDeletedFilesCount();
        Assert.assertEquals(actual, 2);
    }
}
