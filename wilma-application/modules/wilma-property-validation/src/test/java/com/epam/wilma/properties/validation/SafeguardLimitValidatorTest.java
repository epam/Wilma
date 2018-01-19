package com.epam.wilma.properties.validation;
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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.properties.InvalidPropertyException;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Provides unit tests for the class {@link SafeguardLimitValidator}.
 * @author Tunde_Kovacs
 *
 */
public class SafeguardLimitValidatorTest {

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private SafeguardLimitValidator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidateShouldThrowExceptionWhenFiOffLimitLessThanFiOnLimit() {
        //GIVEN
        given(getFiOffLimit()).willReturn(2L);
        given(getFiOnLimit()).willReturn(3L);
        given(getMwOffLimit()).willReturn(5L);
        given(getMwOnLimit()).willReturn(4L);
        //WHEN
        underTest.validate();
        //THEN exception was thrown
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidateShouldThrowExceptionWhenMwOnLimitLessThanFiOnLimit() {
        //GIVEN
        given(getFiOffLimit()).willReturn(4L);
        given(getFiOnLimit()).willReturn(3L);
        given(getMwOffLimit()).willReturn(5L);
        given(getMwOnLimit()).willReturn(2L);
        //WHEN
        underTest.validate();
        //THEN exception was thrown
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidateShouldThrowExceptionWhenMwOffLimitLessThanMwOnLimit() {
        //GIVEN
        given(getFiOffLimit()).willReturn(4L);
        given(getFiOnLimit()).willReturn(3L);
        given(getMwOffLimit()).willReturn(5L);
        given(getMwOnLimit()).willReturn(6L);
        //WHEN
        underTest.validate();
        //THEN exception was thrown
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidateShouldThrowExceptionWhenMwOffLimitLessThanFiOffLimit() {
        //GIVEN
        given(getFiOffLimit()).willReturn(4L);
        given(getFiOnLimit()).willReturn(3L);
        given(getMwOffLimit()).willReturn(3L);
        given(getMwOnLimit()).willReturn(5L);
        //WHEN
        underTest.validate();
        //THEN exception was thrown
    }

    @Test(expectedExceptions = InvalidPropertyException.class)
    public void testValidateShouldThrowExceptionWhenLimitsAreNull() {
        //GIVEN
        given(getFiOffLimit()).willReturn(null);
        given(getFiOnLimit()).willReturn(null);
        given(getMwOffLimit()).willReturn(null);
        given(getMwOnLimit()).willReturn(null);
        //WHEN
        underTest.validate();
        //THEN exception was thrown
    }

    @Test
    public void testValidateShouldNotThrowExceptionWhenAllLimitsAreValid() {
        //GIVEN
        given(getFiOffLimit()).willReturn(10L);
        given(getFiOnLimit()).willReturn(8L);
        given(getMwOffLimit()).willReturn(10L);
        given(getMwOnLimit()).willReturn(8L);
        //WHEN
        underTest.validate();
        //THEN exception was thrown
    }

    private Long getMwOnLimit() {
        return propertyHolder.getLong("safeguard.responseMessageWriter.ONlimit");
    }

    private Long getMwOffLimit() {
        return propertyHolder.getLong("safeguard.responseMessageWriter.OFFlimit");
    }

    private Long getFiOnLimit() {
        return propertyHolder.getLong("safeguard.responseFIdecoder.ONlimit");
    }

    private Long getFiOffLimit() {
        return propertyHolder.getLong("safeguard.responseFIdecoder.OFFlimit");
    }
}
