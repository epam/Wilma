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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.JavaClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.JavaClassFactory;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;

/**
 * Provides unit tests for the class {@link ClassFilePathAssembler}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ClassFilePathAssemblerTest {
    private static final String PARENT_DIRECTORY = "config/something";
    private static final String SIMPLE_CLASS_NAME = "ClassName";
    private static final String FILE_NAME = SIMPLE_CLASS_NAME + ".class";
    private static final String EXCEPTION_MESSAGE = "exception";
    private static final String PACKAGE_NAME = "com.epam.wilma";

    @Mock
    private JavaClassFactory javaClassFactory;
    @Mock
    private JavaClass javaClass;
    @Mock
    private FileFactory fileFactory;
    @Mock
    private File file;
    @Mock
    private InputStream classFile;
    @InjectMocks
    private ClassFilePathAssembler underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFilePathShouldReturnProperPath() throws IOException {
        //GIVEN
        String expected = PARENT_DIRECTORY + "/" + PACKAGE_NAME.replace(".", "/") + "/" + SIMPLE_CLASS_NAME;
        given(fileFactory.createFile(FILE_NAME)).willReturn(file);
        given(file.getParent()).willReturn(PARENT_DIRECTORY);
        given(file.getName()).willReturn(SIMPLE_CLASS_NAME);
        given(javaClassFactory.createJavaClass(classFile, FILE_NAME)).willReturn(javaClass);
        given(javaClass.getPackageName()).willReturn(PACKAGE_NAME);
        given(javaClass.getClassName()).willReturn(PACKAGE_NAME + "." + SIMPLE_CLASS_NAME);
        //WHEN
        String result = underTest.createFilePath(classFile, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN
        assertEquals(result, expected);
        verify(fileFactory).createFile(FILE_NAME);
        verify(file).getParent();
        verify(file).getName();
        verify(javaClassFactory).createJavaClass(classFile, FILE_NAME);
        verify(javaClass).getPackageName();
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testCreateFilePathShouldRethrowIOExceptionAsCannotUploadExternalResourceException() throws IOException {
        //GIVEN
        given(fileFactory.createFile(FILE_NAME)).willReturn(file);
        given(file.getParent()).willReturn(PARENT_DIRECTORY);
        given(file.getName()).willReturn(SIMPLE_CLASS_NAME);
        given(javaClassFactory.createJavaClass(classFile, FILE_NAME)).willThrow(new IOException());
        //WHEN
        underTest.createFilePath(classFile, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN error is thrown
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testCreateFilePathShouldRethrowClassFormatExceptionAsCannotUploadExternalResourceException() throws IOException {
        //GIVEN
        given(fileFactory.createFile(FILE_NAME)).willReturn(file);
        given(file.getParent()).willReturn(PARENT_DIRECTORY);
        given(file.getName()).willReturn(SIMPLE_CLASS_NAME);
        given(javaClassFactory.createJavaClass(classFile, FILE_NAME)).willThrow(new ClassFormatException());
        //WHEN
        underTest.createFilePath(classFile, FILE_NAME, EXCEPTION_MESSAGE);
        //THEN error is thrown
    }
}
