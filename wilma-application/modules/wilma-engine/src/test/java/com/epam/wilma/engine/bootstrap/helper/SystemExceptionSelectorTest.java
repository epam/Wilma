package com.epam.wilma.engine.bootstrap.helper;
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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanCreationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.properties.InvalidPropertyException;

/**
 * Class for testing {@link SystemExceptionSelector}.
 * @author Tamas_Bihari
 *
 */
public class SystemExceptionSelectorTest {
    private SystemExceptionSelector underTest;

    @Mock
    private ApplicationException applicationException;
    @Mock
    private InvalidPropertyException invalidPropertyException;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BeanCreationException beanCreationException;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new SystemExceptionSelector();
    }

    @Test
    public void testGetSystemExceptionShouldReturnWithNullWhenExceptionMostSpecificCauseIsNull() {
        //GIVEN
        given(beanCreationException.getMostSpecificCause()).willReturn(null);
        //WHEN
        SystemException actual = underTest.getSystemException(beanCreationException);
        //THEN
        assertNull(actual);
    }

    @Test
    public void testGetSystemExceptionShouldReturnWithNullWhenExceptionCauseIsNull() {
        //GIVEN
        given(beanCreationException.getMostSpecificCause()).willReturn(invalidPropertyException);
        given(beanCreationException.getCause()).willReturn(null);
        //WHEN
        SystemException actual = underTest.getSystemException(beanCreationException);
        //THEN
        assertNull(actual);
    }

    @Test
    public void testGetSystemExceptionShouldReturnWithNullWhenExceptionCauseNotContainSystemException() {
        //GIVEN
        given(beanCreationException.getMostSpecificCause()).willReturn(applicationException);
        given(beanCreationException.getCause()).willReturn(applicationException, applicationException);
        //WHEN
        SystemException actual = underTest.getSystemException(beanCreationException);
        //THEN
        assertNull(actual);
    }

    @Test
    public void testGetSystemExceptionShouldReturnWithSystemExceptionWhenExceptionCauseContainsSystemException() {
        //GIVEN
        given(beanCreationException.getMostSpecificCause()).willReturn(invalidPropertyException);
        given(beanCreationException.getCause()).willReturn(invalidPropertyException, invalidPropertyException);
        //WHEN
        SystemException actual = underTest.getSystemException(beanCreationException);
        //THEN
        assertNotNull(actual);
    }
}
