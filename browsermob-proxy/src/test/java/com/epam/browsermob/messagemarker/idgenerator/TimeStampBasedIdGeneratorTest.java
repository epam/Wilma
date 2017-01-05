package com.epam.browsermob.messagemarker.idgenerator;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test class for TimestampSimpleNumberIdGenerator.
 * @author Marton_Sereg, Tamas Kohegyi
 *
 */
public class TimeStampBasedIdGeneratorTest {

    @InjectMocks
    private TimeStampBasedIdGenerator underTest;

    @Mock
    private Logger logger;

    @Mock
    private CurrentDateProvider currentDateProvider;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousTimestampIsNull() {
        // GIVEN
        String expected = "20130624145212.0000";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        Whitebox.setInternalState(underTest, "previousSimpleDate", null);
        // WHEN
        String actual = underTest.nextIdentifier();
        // THEN
        verify(currentDateProvider).getCurrentDate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousTimestampDiffersFromTheCurrent() {
        // GIVEN
        String expected = "20130624145212.0000";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        Whitebox.setInternalState(underTest, "previousSimpleDate", "20130624145211");
        // WHEN
        String actual = underTest.nextIdentifier();
        // THEN
        verify(currentDateProvider).getCurrentDate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousTimestampEqualsTheCurrent() {
        // GIVEN
        String expected = "20130624145212.0888";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        Whitebox.setInternalState(underTest, "previousSimpleDate", "20130624145212");
        Whitebox.setInternalState(underTest, "currentNumber", new AtomicInteger(888));
        // WHEN
        String actual = underTest.nextIdentifier();
        // THEN
        verify(currentDateProvider).getCurrentDate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousIDIsAtTheoreticalMaximum() {
        // GIVEN
        String expected = "20130624145212.10000";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();
        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        Whitebox.setInternalState(underTest, "previousSimpleDate", "20130624145212");
        Whitebox.setInternalState(underTest, "currentNumber", new AtomicInteger(9999));
        // WHEN
        underTest.nextIdentifier();
        String actual = underTest.nextIdentifier();
        // THEN
        Assert.assertEquals(actual, expected);
    }

}
