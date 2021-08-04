package com.epam.wilma.stubconfig.initializer.support;

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

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.domain.stubconfig.sequencehandler.DummySequenceHandler;
import com.epam.wilma.stubconfig.initializer.support.helper.BeanRegistryService;
import com.epam.wilma.stubconfig.initializer.support.helper.PackageBasedClassFinder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.slf4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link ExternalJarClassInitializer}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class ExternalJarClassInitializerTest {

    private static final String PACKAGE_NAME = "com.test.something";
    private static final String JAR_FOLDER_PATH = "config/jar";
    private static final Class<SequenceHandler> INTERFACE_TO_CAST = SequenceHandler.class;
    private static final String BEAN_NAME = PACKAGE_NAME + INTERFACE_TO_CAST.getSimpleName();

    @Mock
    private PackageBasedClassFinder packageBasedClassFinder;
    @Mock
    private FileUtils fileUtils;
    @Mock
    private FileFactory fileFactory;
    @Mock
    private BeanRegistryService beanRegistryService;
    @Mock
    private ExternalClassInitializer externalClassInitializer;

    @InjectMocks
    private ExternalJarClassInitializer underTest;

    @Mock
    private File folder;
    @Mock
    private Logger logger;
    @Mock
    private DummySequenceHandler object;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FieldSetter.setField(underTest, underTest.getClass().getDeclaredField("logger"), logger);
        FieldSetter.setField(underTest, underTest.getClass().getDeclaredField("packageBasedClassFinder"), packageBasedClassFinder);
        FieldSetter.setField(underTest, underTest.getClass().getDeclaredField("externalClassInitializer"), externalClassInitializer);
        given(fileFactory.createFile(JAR_FOLDER_PATH)).willReturn(folder);
        doReturn(DummySequenceHandler.class).when(externalClassInitializer).loadExternalClass(anyString(), anyString(), any());
        doReturn(DummySequenceHandler.class).when(packageBasedClassFinder).findClassInJar(JAR_FOLDER_PATH, INTERFACE_TO_CAST, PACKAGE_NAME);
        given(beanRegistryService.getBean(BEAN_NAME, INTERFACE_TO_CAST)).willReturn(object);
    }

    @Test
    public void testLoadExternalClassShouldGetClassAsBeanFirst() throws MalformedURLException {
        //GIVEN in setup
        //WHEN
        Object result = underTest.loadExternalClass(PACKAGE_NAME, JAR_FOLDER_PATH, INTERFACE_TO_CAST);
        //THEN
        verify(packageBasedClassFinder, never()).findClassInJar(any(), any(), any());
        verify(logger).info(Mockito.anyString(), any(), any(), any());
        assertNotNull(result);
    }

    @Test
    public void testLoadExternalClassShouldInstantiateClassWhenBeanWasNotFound() throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        //GIVEN
        given(beanRegistryService.getBean(BEAN_NAME, INTERFACE_TO_CAST)).willThrow(new NoSuchBeanDefinitionException("error"));
        Collection<File> jarFiles = new ArrayList<>();
        given(fileUtils.listFiles(folder, "jar")).willReturn(jarFiles);
        //WHEN
        Object result = underTest.loadExternalClass(PACKAGE_NAME, JAR_FOLDER_PATH, INTERFACE_TO_CAST);
        //THEN
        verify(logger).info(Mockito.anyString(), any(), any(), any());
        assertNotNull(result);
    }

    @Test(expected = DescriptorValidationFailedException.class)
    public void testLoadExternalClassShouldThrowDescriptorValidationFailedExceptionWhenNeitherBeanNorClassWasFound() throws MalformedURLException {
        //GIVEN
        given(beanRegistryService.getBean(BEAN_NAME, INTERFACE_TO_CAST)).willThrow(new NoSuchBeanDefinitionException("error"));
        Collection<File> jarFiles = new ArrayList<>();
        given(fileUtils.listFiles(folder, "jar")).willReturn(jarFiles);
        given(packageBasedClassFinder.findClassInJar(JAR_FOLDER_PATH, INTERFACE_TO_CAST, PACKAGE_NAME)).willThrow(new DescriptorValidationFailedException(""));
        //WHEN
        underTest.loadExternalClass(PACKAGE_NAME, JAR_FOLDER_PATH, INTERFACE_TO_CAST);
        //THEN exception is thrown
    }

    @Ignore
    @Test(expected = DescriptorValidationFailedException.class)
    public void testLoadExternalClassShouldThrowDescriptorValidationFailedExceptionWhenBeanWasNotFoundButClassIsAbstract() throws MalformedURLException {
        //TODO test abstract class load from jar
        //GIVEN
        given(beanRegistryService.getBean(BEAN_NAME, INTERFACE_TO_CAST)).willThrow(new NoSuchBeanDefinitionException("error"));
        Collection<File> jarFiles = new ArrayList<>();
        given(fileUtils.listFiles(folder, "jar")).willReturn(jarFiles);
        given(packageBasedClassFinder.findClassInJar(any(), any(), any())).willThrow(new DescriptorValidationFailedException(""));
        //WHEN
        underTest.loadExternalClass(PACKAGE_NAME, JAR_FOLDER_PATH, INTERFACE_TO_CAST);
        //THEN exception is thrown
    }
}
