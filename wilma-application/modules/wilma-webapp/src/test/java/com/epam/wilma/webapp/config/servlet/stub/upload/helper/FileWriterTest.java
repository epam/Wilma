package com.epam.wilma.webapp.config.servlet.stub.upload.helper;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.webapp.config.servlet.helper.InputStreamUtil;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

/**
 * Provides unit tests for the class {@link FileWriter}.
 * @author Tunde_Kovacs
 *
 */
public class FileWriterTest {

    private static final String FILE_NAME = "testFile.txt";
    private static final String EXCEPTION_MESSAGE = "exception";
    private static final String DIRECTORIES = "";
    private static final byte[] FILE_AS_BYTES = new byte[0];

    @Mock
    private FileOutputStreamFactory fileOutputStreamFactory;
    @Mock
    private FileOutputStream fileOutputStream;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private FileFactory fileFactory;
    @Mock
    private File file;
    @Mock
    private InputStream inputStream;
    @Mock
    private ByteArrayInputStreamFactory byteArrayInputStreamFactory;
    @Mock
    private ByteArrayInputStream byteArrayInputStream;
    @Mock
    private ClassFilePathAssembler classFilePathAssembler;
    @Mock
    private InputStreamUtil inputStreamUtil;
    @Mock
    private JarValidator jarValidator;

    @InjectMocks
    private FileWriter underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(fileFactory.createFile(FILE_NAME)).willReturn(file);
        given(inputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
    }

    @Test
    public void testWriteShouldWriteFileToOutputStream() throws IOException {
        //GIVEN
        given(file.getParent()).willReturn(DIRECTORIES);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        //WHEN
        underTest.write(inputStream, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN
        verify(fileOutputStream).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testWriteWhenFileExistsShouldNotCreatNewFile() throws IOException {
        //GIVEN
        given(file.getParent()).willReturn(DIRECTORIES);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        given(file.exists()).willReturn(true);
        //WHEN
        underTest.write(inputStream, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN
        verify(file, Mockito.never()).createNewFile();
    }

    @Test
    public void testWriteShouldCloseOutputStream() throws IOException {
        //GIVEN
        given(file.getParent()).willReturn(DIRECTORIES);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        //WHEN
        underTest.write(inputStream, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN
        verify(fileOutputStream).close();
    }

    @Test
    public void testWriteWhenFileIsInPackageShouldCreateFolderStructure() throws IOException {
        //GIVEN
        String directories = "com/epam/wilma";
        given(file.getParent()).willReturn(directories);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        //WHEN
        underTest.write(inputStream, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN
        verify(fileFactory.createFile(directories)).mkdirs();
    }

    @Test
    public void testWriteWhenFileIsNotInPackageShouldCreateNotFolderStructure() throws IOException {
        //GIVEN
        given(file.getParent()).willReturn(null);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        //WHEN
        underTest.write(inputStream, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN
        verify(fileFactory.createFile(Mockito.anyString()), never()).mkdirs();
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testWriteWhenCannotWriteShouldThrowException() throws IOException {
        //GIVEN
        given(file.getParent()).willReturn(DIRECTORIES);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willThrow(new FileNotFoundException());
        //WHEN
        underTest.write(inputStream, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN it should throw exception
    }

    @Test
    public void testWriteWhenFileIsAClassFile() throws IOException {
        //GIVEN
        String fileName = "MyClass.class";
        given(byteArrayInputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
        given(inputStreamUtil.transformToByteArray(inputStream)).willReturn(FILE_AS_BYTES);
        given(byteArrayInputStreamFactory.createByteArrayInputStream(FILE_AS_BYTES)).willReturn(byteArrayInputStream);
        given(classFilePathAssembler.createFilePath(byteArrayInputStream, fileName, EXCEPTION_MESSAGE)).willReturn(fileName);
        given(fileFactory.createFile(fileName)).willReturn(file);
        given(file.getParent()).willReturn(DIRECTORIES);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        given(file.exists()).willReturn(true);
        //WHEN
        underTest.write(inputStream, fileName, EXCEPTION_MESSAGE);
        //THEN
        verify(fileFactory).createFile(Mockito.anyString());
        verify(inputStreamUtil).transformToByteArray(inputStream);
        verify(byteArrayInputStreamFactory, times(2)).createByteArrayInputStream(FILE_AS_BYTES);
        verify(classFilePathAssembler).createFilePath(byteArrayInputStream, fileName, EXCEPTION_MESSAGE);
        verify(fileFactory).createFile(fileName);
        verify(file).getParent();
        verify(fileOutputStreamFactory).createFileOutputStream(file);
        verify(file).exists();
    }

    @Test
    public void testWriteWhenFileIsAValidJarFile() throws IOException {
        //GIVEN
        String fileName = "myjar.jar";
        given(byteArrayInputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
        given(inputStreamUtil.transformToByteArray(inputStream)).willReturn(FILE_AS_BYTES);
        given(byteArrayInputStreamFactory.createByteArrayInputStream(FILE_AS_BYTES)).willReturn(byteArrayInputStream);
        given(fileFactory.createFile(fileName)).willReturn(file);
        given(file.getParent()).willReturn(DIRECTORIES);
        given(fileOutputStreamFactory.createFileOutputStream(file)).willReturn(fileOutputStream);
        given(file.exists()).willReturn(true);
        //WHEN
        underTest.write(inputStream, fileName, EXCEPTION_MESSAGE);
        //THEN
        verify(fileFactory).createFile(Mockito.anyString());
        verify(fileFactory).createFile(fileName);
        verify(file).getParent();
        verify(fileOutputStreamFactory).createFileOutputStream(file);
        verify(file).exists();
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testWriteWhenFileIsAnInvalidJarFile() throws IOException {
        //GIVEN
        String fileName = "myjar.jar";
        given(byteArrayInputStream.read(Mockito.any(byte[].class))).willReturn(1, -1);
        given(inputStreamUtil.transformToByteArray(inputStream)).willReturn(FILE_AS_BYTES);
        given(byteArrayInputStreamFactory.createByteArrayInputStream(FILE_AS_BYTES)).willReturn(byteArrayInputStream);
        doThrow(new CannotUploadExternalResourceException(EXCEPTION_MESSAGE)).when(jarValidator).validateInputStream(byteArrayInputStream);
        //WHEN
        underTest.write(inputStream, fileName, EXCEPTION_MESSAGE);
        //THEN error is thrown
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testWriteWhenFileIsAClassFileShouldThrowCannotUploadExternalResourceExceptionWhenTransformingFails() throws IOException {
        //GIVEN
        String fileName = "MyClass.class";
        given(inputStreamUtil.transformToByteArray(inputStream)).willThrow(new IOException());
        //WHEN
        underTest.write(inputStream, fileName, EXCEPTION_MESSAGE);
        //THEN error is thrown
    }
}
