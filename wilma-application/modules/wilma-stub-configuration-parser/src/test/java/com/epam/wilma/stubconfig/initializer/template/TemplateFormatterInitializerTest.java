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

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

/**
 * Provides unit tests for the class {@link TemplateFormatterInitializer}.
 *
 * @author Tunde_Kovacs
 */
public class TemplateFormatterInitializerTest {

    private static final String CHECKER_CLASS = "TestTemplateFormatter";
    private static final String PATH = "config/template-formatters";

    private TemplateFormatter templateFormatter;
    private List<TemplateFormatter> templateFormatters;

    @Mock
    private ApplicationContext appContext;
    @Mock
    private ExternalInitializer externalInitializer;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;

    @InjectMocks
    private TemplateFormatterInitializer underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new TemplateFormatterInitializer());
        MockitoAnnotations.initMocks(this);
        templateFormatters = new ArrayList<>();
        given(stubResourceHolder.getTemplateFormatters()).willReturn(templateFormatters);
    }

    @Test
    public void testGetTemplateFormatterWhenInternalExistsShouldReturnTemplateFormatter() {
        //GIVEN
        templateFormatter = new TestTemplateFormatter();
        templateFormatters.add(templateFormatter);
        //WHEN
        TemplateFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, templateFormatter);
    }

    @Test
    public void testGetTemplateFormatterWhenMoreInternalsExistShouldReturnTemplateFormatter() {
        //GIVEN
        templateFormatters.clear();
        templateFormatter = new ExampleFormatter();
        templateFormatters.add(templateFormatter);
        templateFormatter = new TestTemplateFormatter();
        templateFormatters.add(templateFormatter);
        //WHEN
        TemplateFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, templateFormatter);
    }

    @Test
    public void testGetTemplateFormatterWhenExternalExistsShouldReturnTemplateFormatter() {
        //GIVEN
        given(stubResourcePathProvider.getTemplateFormattersPathAsString()).willReturn(PATH);
        templateFormatter = new ExampleFormatter();
        given(externalInitializer.loadExternalClass(CHECKER_CLASS, PATH, TemplateFormatter.class)).willReturn(templateFormatter);
        //WHEN
        TemplateFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual.getClass(), templateFormatter.getClass());
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testGetTemplateFormatterWhenItDoesNotExistShouldThrowException() {
        //GIVEN
        given(stubResourcePathProvider.getTemplateFormattersPathAsString()).willReturn(PATH);
        given(externalInitializer.loadExternalClass(CHECKER_CLASS, PATH, TemplateFormatter.class)).willThrow(new DescriptorValidationFailedException(CHECKER_CLASS));
        //WHEN
        underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN it should throw exception
    }

    @Test
    public void testGetTemplateFormatterShouldReturnTemplateFormatter() {
        //GIVEN
        given(appContext.getBean(CHECKER_CLASS, TemplateFormatter.class)).willThrow(new NoSuchBeanDefinitionException(""));
        templateFormatter = new TestTemplateFormatter();
        templateFormatters.add(templateFormatter);
        //WHEN
        TemplateFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, templateFormatter);
    }

    @Test
    public void testGetTemplateFormatterShouldReturnTemplateFormatterWhenTheClassIsManagedBySpring() {
        //GIVEN
        given(appContext.getBean(CHECKER_CLASS, TemplateFormatter.class)).willReturn(templateFormatter);
        //WHEN
        TemplateFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, templateFormatter);
    }

}
