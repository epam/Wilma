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
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
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
 * Provides unit tests for the class {@link ResponseFormatterInitializer}.
 *
 * @author Tunde_Kovacs
 */
public class ResponseFormatterInitializerTest {

    private static final String CHECKER_CLASS = "TestResponseFormatter";
    private static final String PATH = "config/response-formatters";

    private ResponseFormatter responseFormatter;
    private List<ResponseFormatter> responseFormatters;

    @Mock
    private ApplicationContext appContext;
    @Mock
    private ExternalInitializer externalInitializer;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;

    @InjectMocks
    private ResponseFormatterInitializer underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new ResponseFormatterInitializer());
        MockitoAnnotations.initMocks(this);
        responseFormatters = new ArrayList<>();
        given(stubResourceHolder.getResponseFormatters()).willReturn(responseFormatters);
    }

    @Test
    public void testGetResponseFormatterWhenInternalExistsShouldReturnResponseFormatter() {
        //GIVEN
        responseFormatter = new TestResponseFormatter();
        responseFormatters.add(responseFormatter);
        //WHEN
        ResponseFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, responseFormatter);
    }

    @Test
    public void testGetResponseFormatterWhenMoreInternalsExistShouldReturnResponseFormatter() {
        //GIVEN
        responseFormatters.clear();
        responseFormatter = new ExampleFormatter();
        responseFormatters.add(responseFormatter);
        responseFormatter = new TestResponseFormatter();
        responseFormatters.add(responseFormatter);
        //WHEN
        ResponseFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, responseFormatter);
    }

    @Test
    public void testGetResponseFormatterWhenExternalExistsShouldReturnResponseFormatter() {
        //GIVEN
        given(stubResourcePathProvider.getResponseFormattersPathAsString()).willReturn(PATH);
        responseFormatter = new ExampleFormatter();
        given(externalInitializer.loadExternalClass(CHECKER_CLASS, PATH, ResponseFormatter.class)).willReturn(responseFormatter);
        //WHEN
        ResponseFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual.getClass(), responseFormatter.getClass());
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testGetResponseFormatterWhenItDoesNotExistShouldThrowException() {
        //GIVEN
        given(stubResourcePathProvider.getResponseFormattersPathAsString()).willReturn(PATH);
        given(externalInitializer.loadExternalClass(CHECKER_CLASS, PATH, ResponseFormatter.class)).willThrow(new DescriptorValidationFailedException(CHECKER_CLASS));
        //WHEN
        underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN it should throw exception
    }

    @Test
    public void testGetResponseFormatterShouldReturnResponseFormatter() {
        //GIVEN
        given(appContext.getBean(CHECKER_CLASS, ResponseFormatter.class)).willThrow(new NoSuchBeanDefinitionException(""));
        responseFormatter = new TestResponseFormatter();
        responseFormatters.add(responseFormatter);
        //WHEN
        ResponseFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, responseFormatter);
    }

    @Test
    public void testGetResponseFormatterShouldReturnResponseFormatterWhenTheClassIsManagedBySpring() {
        //GIVEN
        given(appContext.getBean(CHECKER_CLASS, ResponseFormatter.class)).willReturn(responseFormatter);
        //WHEN
        ResponseFormatter actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, responseFormatter);
    }

}
