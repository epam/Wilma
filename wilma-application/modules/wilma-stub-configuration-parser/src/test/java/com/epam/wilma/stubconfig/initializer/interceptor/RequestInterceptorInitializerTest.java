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
import com.epam.wilma.domain.stubconfig.interceptor.MockRequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Unit tests for the class {@link RequestInterceptorInitializer}.
 * @author Tunde_Kovacs
 *
 */
public class RequestInterceptorInitializerTest {

    private static final String CLASS_NAME = "MockRequestInterceptor";
    private static final String PATH = "config/interceptors";
    private List<RequestInterceptor> requestInterceptors;

    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;
    @Mock
    private ExternalInitializer externalInitializer;
    @Mock
    private ApplicationContext appContext;

    @InjectMocks
    private RequestInterceptorInitializer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        requestInterceptors = new ArrayList<>();
        given(stubResourceHolder.getRequestInterceptors()).willReturn(requestInterceptors);
    }

    @Test
    public void testGetRequestInterceptorShouldReturnClassBasedOnName() {
        //GIVEN
        given(appContext.getBean(CLASS_NAME, RequestInterceptor.class)).willThrow(new NoSuchBeanDefinitionException(""));
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        requestInterceptors.add(requestInterceptor);
        //WHEN
        RequestInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, requestInterceptor);
    }

    @Test
    public void testGetRequestInterceptorShouldReturnRequestInterceptorrWhenTheClassIsManagedBySpring() {
        //GIVEN
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        given(appContext.getBean(CLASS_NAME, RequestInterceptor.class)).willReturn(requestInterceptor);
        //WHEN
        RequestInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, requestInterceptor);
    }

    @Test
    public void testGetRequestInterceptorWhenInternalExistsShouldReturnRequestInterceptor() {
        //GIVEN
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        requestInterceptors.add(requestInterceptor);
        //WHEN
        RequestInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, requestInterceptor);
    }

    @Test
    public void testGetRequestInterceptorWhenMoreInternalsExistShouldReturnRequestInterceptor() {
        //GIVEN
        requestInterceptors.clear();
        RequestInterceptor requestInterceptor1 = new MockRequestInterceptor();
        requestInterceptors.add(requestInterceptor1);
        RequestInterceptor requestInterceptor2 = new MockRequestInterceptor();
        requestInterceptors.add(requestInterceptor2);
        //WHEN
        RequestInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, requestInterceptor1);
    }

    @Test
    public void testGetRequestInterceptorWhenExternalExistsShouldReturnRequestInterceptor() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn(PATH);
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        given(externalInitializer.loadExternalClass(CLASS_NAME, PATH, RequestInterceptor.class)).willReturn(requestInterceptor);
        //WHEN
        RequestInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual.getClass(), requestInterceptor.getClass());
    }

    @Test
    public void testGetRequestInterceptorWhenNoClassExistsShouldReturnNull() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn(null);
        //WHEN
        RequestInterceptor actual = underTest.getExternalClassObject(CLASS_NAME);
        //THEN
        assertEquals(actual, null);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testRequestInterceptorWhenItDoesNotExistShouldThrowException() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn(PATH);
        given(externalInitializer.loadExternalClass(CLASS_NAME, PATH, RequestInterceptor.class)).willThrow(
                new DescriptorValidationFailedException(CLASS_NAME));
        //WHEN
        underTest.getExternalClassObject(CLASS_NAME);
        //THEN it should throw exception
    }
}
