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

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.stubconfig.dom.parser.node.helper.ClassNameMapper;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.condition.helper.ClassFactory;
import com.epam.wilma.stubconfig.initializer.condition.helper.ClassPathExtender;
import com.epam.wilma.stubconfig.initializer.support.helper.BeanRegistryService;
import com.epam.wilma.stubconfig.initializer.support.helper.ClassInstantiator;
import com.epam.wilma.stubconfig.initializer.support.helper.ClassValidator;

/**
 * Provides unit tests for the class {@link ExternalClassInitializer}.
 * @author Tamas_Bihari, Tamas Kohegyi
 *
 */
public class ExternalClassInitializerTest {
    private static final String CLASS = "PACKAGE.EXTERNAL_CLASS_NAME";
    private static final String SIMPLE_CLASS_NAME = "EXTERNAL_CLASS_NAME";
    private static final String PATH = "PATH_OF_THE_CLASS";
    @Mock
    private ClassPathExtender classPathExtender;
    @Mock
    private ClassFactory classFactory;
    @Mock
    private BeanRegistryService beanRegistryService;
    @Mock
    private ClassNameMapper classNameMapper;
    @Mock
    private ClassValidator classValidator;
    @Mock
    private ClassInstantiator classInstantiator;

    @InjectMocks
    private ExternalClassInitializer underTest;

    @BeforeMethod
    public void setUp() {
        underTest = spy(new ExternalClassInitializer());
        initMocks(this);
        given(classNameMapper.get(SIMPLE_CLASS_NAME)).willReturn(CLASS);
    }

    @Test
    public void testLoadExternalClassShouldCallClassPathExtenderAddFileFunction() throws ClassNotFoundException {
        //GIVEN
        given(beanRegistryService.getBean(SIMPLE_CLASS_NAME, TemplateGenerator.class)).willThrow(new NoSuchBeanDefinitionException(""));
        given(classFactory.getClassToLoad(CLASS)).willReturn(Object.class);
        ignoreInterfaceTypeValidation();
        //WHEN
        underTest.loadExternalClass(SIMPLE_CLASS_NAME, PATH, TemplateGenerator.class);
        //THEN
        verify(classPathExtender).addFile(PATH);
        verify(classNameMapper).get(SIMPLE_CLASS_NAME);
    }

    @Test
    public void testLoadExternalClassShouldBeRegisteredToApplicationContext() throws ClassNotFoundException {
        //GIVEN
        given(beanRegistryService.getBean(SIMPLE_CLASS_NAME, TemplateGenerator.class)).willThrow(new NoSuchBeanDefinitionException(""));
        given(classFactory.getClassToLoad(CLASS)).willReturn(Object.class);
        ignoreInterfaceTypeValidation();
        //WHEN
        underTest.loadExternalClass(SIMPLE_CLASS_NAME, PATH, TemplateGenerator.class);
        //THEN
        verify(beanRegistryService).register(eq(SIMPLE_CLASS_NAME), any(Object.class));
        verify(classNameMapper).get(SIMPLE_CLASS_NAME);
    }

    @Test
    public void testLoadExternalClassShouldReturnTheClassWhenAlreadyInApplicationContexg() throws ClassNotFoundException {
        //GIVEN
        TemplateGenerator generator = new DummyGenerator();
        given(beanRegistryService.getBean(SIMPLE_CLASS_NAME, TemplateGenerator.class)).willReturn(generator);
        //WHEN
        TemplateGenerator actual = underTest.loadExternalClass(CLASS, PATH, TemplateGenerator.class);
        //THEN
        assertEquals(actual, generator);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testLoadExternalClassShouldThrowExceptionWhenClassFactoryThrowException() throws ClassNotFoundException {
        //GIVEN
        given(beanRegistryService.getBean(SIMPLE_CLASS_NAME, TemplateGenerator.class)).willThrow(new NoSuchBeanDefinitionException(""));
        given(classFactory.getClassToLoad(CLASS)).willThrow(new ClassNotFoundException());
        //WHEN
        underTest.loadExternalClass(SIMPLE_CLASS_NAME, PATH, TemplateGenerator.class);
        //THEN exception is thrown
        verify(classNameMapper).get(SIMPLE_CLASS_NAME);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testLoadExternalClassShouldThrowExceptionWhenClassFactoryThrowClassFormatException() throws ClassNotFoundException {
        //GIVEN
        given(beanRegistryService.getBean(SIMPLE_CLASS_NAME, TemplateGenerator.class)).willThrow(new NoSuchBeanDefinitionException(""));
        given(classFactory.getClassToLoad(CLASS)).willThrow(new ClassFormatError());
        //WHEN
        underTest.loadExternalClass(SIMPLE_CLASS_NAME, PATH, TemplateGenerator.class);
        //THEN exception is thrown
        verify(classNameMapper).get(SIMPLE_CLASS_NAME);
    }

    @SuppressWarnings("unchecked")
    private void ignoreInterfaceTypeValidation() {
        doNothing().when(classValidator).validateInterface(any(), any(Class.class), anyString());
    }

    private static final class DummyGenerator implements TemplateGenerator {

        @Override
        public byte[] generateTemplate() {
            return null;
        }
    }
}
