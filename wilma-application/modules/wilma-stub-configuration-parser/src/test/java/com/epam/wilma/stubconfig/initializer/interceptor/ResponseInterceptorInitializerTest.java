package com.epam.wilma.stubconfig.initializer.interceptor;
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
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.interceptor.MockResponseInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Unit tests for the class {@link ResponseInterceptorInitializer}.
 * @author Tunde_Kovacs
 *
 */
public class ResponseInterceptorInitializerTest {

    private static final String CLASS_NAME = "MockResponseInterceptor";
    private static final String PATH = "config/interceptors";
    private List<ResponseInterceptor> responseInterceptors;

    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;
    @Mock
    private ExternalInitializer externalInitializer;
    @Mock
    private ApplicationContext appContext;

    @InjectMocks
    private ResponseInterceptorInitializer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        responseInterceptors = new ArrayList<>();
        given(stubResourceHolder.getResponseInterceptors()).willReturn(responseInterceptors);
    }

    @Test
    public void testGetResponseInterceptorShouldReturnClassBasedOnName() {
        //GIVEN
        given(appContext.getBean(CLASS_NAME, ResponseInterceptor.class)).willThrow(new NoSuchBeanDefinitionException(""));
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        responseInterceptors.add(responseInterceptor);
        //WHEN
        ResponseInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, responseInterceptor);
    }

    @Test
    public void testGetResponseInterceptorShouldReturnRequestInterceptorrWhenTheClassIsManagedBySpring() {
        //GIVEN
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(appContext.getBean(CLASS_NAME, ResponseInterceptor.class)).willReturn(responseInterceptor);
        //WHEN
        ResponseInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, responseInterceptor);
    }

    @Test
    public void testGetResponseInterceptorWhenInternalExistsShouldReturnResponseInterceptor() {
        //GIVEN
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        responseInterceptors.add(responseInterceptor);
        //WHEN
        ResponseInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, responseInterceptor);
    }

    @Test
    public void testGetResponseInterceptorWhenMoreInternalsExistShouldReturnResponseInterceptor() {
        //GIVEN
        responseInterceptors.clear();
        ResponseInterceptor responseInterceptor1 = new MockResponseInterceptor();
        responseInterceptors.add(responseInterceptor1);
        ResponseInterceptor responseInterceptor2 = new MockResponseInterceptor();
        responseInterceptors.add(responseInterceptor2);
        //WHEN
        ResponseInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, responseInterceptor1);
    }

    @Test
    public void testGetResponseInterceptorWhenExternalExistsShouldReturnResponseInterceptor() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn(PATH);
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(externalInitializer.loadExternalClass(CLASS_NAME, PATH, ResponseInterceptor.class)).willReturn(responseInterceptor);
        //WHEN
        ResponseInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual.getClass(), responseInterceptor.getClass());
    }

    @Test
    public void testGetResponseInterceptorWhenNoClassExistsShouldReturnNull() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn(null);
        //WHEN
        ResponseInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, null);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testGetResponseInterceptorWhenItDoesNotExistShouldThrowException() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn(PATH);
        given(externalInitializer.loadExternalClass(CLASS_NAME, PATH, ResponseInterceptor.class)).willThrow(
                new DescriptorValidationFailedException(CLASS_NAME));
        //WHEN
        underTest.getExternalClassObject(CLASS_NAME);
        //THEN it should throw exception
    }

}
