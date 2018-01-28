package com.epam.wilma.stubconfig.dom.parser.node.helper;

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.JavaClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.common.helper.JavaClassFactory;

/**
 * Tests for {@link ClassNameMapper}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ClassNameMapperTest {
    private static final String FOLDER_NAME = "a folder";
    private static final File FOLDER = new File(FOLDER_NAME);

    @Mock
    private FileUtils fileUtils;
    @Mock
    private JavaClassFactory javaClassFactory;
    @Mock
    private JavaClass javaClass;
    @Mock
    private Logger logger;

    @InjectMocks
    private ClassNameMapper underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitializeWithEmptyListShouldNotListFiles() {
        //GIVEN
        List<String> emptyList = Collections.emptyList();
        //WHEN
        underTest.initialize(emptyList);
        //THEN
        verify(fileUtils, never()).listFiles((File) Mockito.any());
    }

    @Test
    public void testInitializeWithClassListShouldParseJavaClass() throws IOException {
        //GIVEN
        List<String> list = new ArrayList<>();
        list.add(FOLDER_NAME);
        Collection<File> filesInFolder = new ArrayList<>();
        String classFileName = "Something.class";
        String className = "Something";
        File classFile = new File(classFileName);
        filesInFolder.add(classFile);
        given(fileUtils.listFiles(FOLDER)).willReturn(filesInFolder);
        given(javaClassFactory.createJavaClass(classFile)).willReturn(javaClass);
        given(javaClass.getClassName()).willReturn(className);
        //WHEN
        underTest.initialize(list);
        //THEN
        verify(javaClassFactory, times(1)).createJavaClass(classFile);
    }

    @Test
    public void testInitializeWithNonClassListShouldNotParseJavaClass() throws IOException {
        //GIVEN
        List<String> list = new ArrayList<>();
        list.add(FOLDER_NAME);
        Collection<File> filesInFolder = new ArrayList<>();
        File notClassFile = new File("NotClass.xml");
        filesInFolder.add(notClassFile);
        given(fileUtils.listFiles(FOLDER)).willReturn(filesInFolder);
        //WHEN
        underTest.initialize(list);
        //THEN
        verify(javaClassFactory, never()).createJavaClass(notClassFile);
    }

    @Test
    public void testInitializeWithInvalidClassListShouldThrowClassFormatException() throws IOException {
        //GIVEN
        List<String> list = new ArrayList<>();
        list.add(FOLDER_NAME);
        Collection<File> filesInFolder = new ArrayList<>();
        File invalidClass = new File("InvalidClass.class");
        filesInFolder.add(invalidClass);
        given(fileUtils.listFiles(FOLDER)).willReturn(filesInFolder);
        given(javaClassFactory.createJavaClass(invalidClass)).willThrow(new ClassFormatException());
        Whitebox.setInternalState(underTest, "logger", logger);
        //WHEN
        underTest.initialize(list);
        //THEN
        verify(logger).info(Mockito.anyString(), Mockito.any(ClassFormatException.class));
    }

}
