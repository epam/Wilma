package com.epam.wilma.engine.bootstrap;

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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;
import com.epam.wilma.engine.bootstrap.helper.ApplicationContextFactory;
import com.epam.wilma.engine.bootstrap.helper.SystemExceptionSelector;
import com.epam.wilma.engine.bootstrap.helper.WilmaServiceListener;
import com.epam.wilma.engine.properties.helper.PropertiesNotAvailableException;
import com.epam.wilma.properties.InvalidPropertyException;
import com.google.common.util.concurrent.Service;

/**
 * Provides unit tests for <tt>WilmaBootstrap</tt> class.
 */
public class WilmaBootstrapTest {

    private static final String WILMA_CANNOT_BE_STARTED_ERROR_MSG = "Wilma cannot be started. ";
    private ApplicationContextFactory applicationContextFactory;

    @Mock
    private ClassPathXmlApplicationContext applicationContext;
    @Mock
    private MutablePropertySources propertyResources;
    @Mock
    private WilmaEngine wilmaEngine;
    @Mock
    private WilmaServiceListener wilmaServiceListener;
    @Mock
    private SchedulingCannotBeStartedException schedulingCannotBeStartedException;
    @Mock
    private InvalidPropertyException invalidPropertyException;
    @Mock
    private PropertiesNotAvailableException propertiesNotAvailableException;
    @Mock
    private Logger logger;
    @Mock
    private SystemExceptionSelector systemExceptionSelector;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BeanCreationException beanCreationException;
    @InjectMocks
    private WilmaBootstrap underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        applicationContextFactory = ApplicationContextFactory.getInstance();
        underTest = spy(new WilmaBootstrap(applicationContextFactory));
        doReturn(applicationContext).when(underTest).getApplicationContext();
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "systemExceptionSelector", systemExceptionSelector);
        Whitebox.setInternalState(wilmaEngine, "delegate", Mockito.mock(Service.class));
    }

    @Test
    public void testBootstrapShouldStartWilmaEngine() {
        //GIVEN
        given(applicationContext.getBean(WilmaEngine.class)).willReturn(wilmaEngine);
        //WHEN
        underTest.bootstrap();
        //THEN
        verify(wilmaEngine).start();
    }

    @Test
    public void testBootstrapWhenThrowsSchedulingCannotBeStartedExceptionShouldLogException() {
        //GIVEN
        given(applicationContext.getBean(WilmaEngine.class)).willThrow(beanCreationException);
        given(beanCreationException.getMostSpecificCause()).willReturn(schedulingCannotBeStartedException);
        given(beanCreationException.contains(SchedulingCannotBeStartedException.class)).willReturn(true);
        given(beanCreationException.getCause()).willReturn(schedulingCannotBeStartedException);
        given(systemExceptionSelector.getSystemException(beanCreationException)).willReturn(schedulingCannotBeStartedException);
        //WHEN
        underTest.bootstrap();
        //THEN
        verify(systemExceptionSelector).getSystemException(beanCreationException);
        verify(logger).error(WILMA_CANNOT_BE_STARTED_ERROR_MSG + beanCreationException.getMostSpecificCause().getMessage(),
                schedulingCannotBeStartedException);
    }

    @Test
    public void testBootstrapWhenThrowsInvalidPropertyExceptionShouldLogException() {
        //GIVEN
        given(applicationContext.getBean(WilmaEngine.class)).willThrow(beanCreationException);
        given(beanCreationException.getMostSpecificCause()).willReturn(invalidPropertyException);
        given(beanCreationException.contains(InvalidPropertyException.class)).willReturn(true);
        given(beanCreationException.getCause()).willReturn(invalidPropertyException);
        given(systemExceptionSelector.getSystemException(beanCreationException)).willReturn(invalidPropertyException);
        //WHEN
        underTest.bootstrap();
        //THEN
        verify(systemExceptionSelector).getSystemException(beanCreationException);
        verify(logger).error(WILMA_CANNOT_BE_STARTED_ERROR_MSG + beanCreationException.getMostSpecificCause().getMessage(), invalidPropertyException);
    }

    @Test
    public void testBootstrapWhenThrowsPropertiesNotAvailableExceptionShouldLogException() {
        //GIVEN
        given(applicationContext.getBean(WilmaEngine.class)).willThrow(beanCreationException);
        given(beanCreationException.getMostSpecificCause()).willReturn(schedulingCannotBeStartedException);
        given(beanCreationException.contains(PropertiesNotAvailableException.class)).willReturn(true);
        given(beanCreationException.getCause()).willReturn(schedulingCannotBeStartedException);
        given(systemExceptionSelector.getSystemException(beanCreationException)).willReturn(schedulingCannotBeStartedException);
        //WHEN
        underTest.bootstrap();
        //THEN
        verify(systemExceptionSelector).getSystemException(beanCreationException);
        verify(logger).error(WILMA_CANNOT_BE_STARTED_ERROR_MSG + beanCreationException.getMostSpecificCause().getMessage(),
                schedulingCannotBeStartedException);
    }

    @Test
    public void testBootstrapShouldLogAnyNonWilmaSpecificException() {
        //GIVEN
        given(applicationContext.getBean(WilmaEngine.class)).willThrow(beanCreationException);
        given(beanCreationException.getMostSpecificCause()).willReturn(schedulingCannotBeStartedException);
        given(beanCreationException.contains(RuntimeException.class)).willReturn(true);
        given(systemExceptionSelector.getSystemException(beanCreationException)).willReturn(null);
        given(beanCreationException.getCause()).willReturn(null);
        //WHEN
        underTest.bootstrap();
        //THEN
        verify(systemExceptionSelector).getSystemException(beanCreationException);
        verify(logger).error(WILMA_CANNOT_BE_STARTED_ERROR_MSG, beanCreationException);
    }
}
