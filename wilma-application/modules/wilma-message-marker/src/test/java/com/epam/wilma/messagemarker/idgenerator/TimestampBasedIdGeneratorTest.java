package com.epam.wilma.messagemarker.idgenerator;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.exception.TooManyRequestsException;
import com.epam.wilma.messagemarker.abortcounter.AbortedMessageCounter;
import com.epam.wilma.messagemarker.configuration.MessageMarkerConfigurationAccess;
import com.epam.wilma.messagemarker.configuration.domain.RequestLimits;

/**
 * Test class for TimestampSimpleNumberIdGenerator.
 * @author Marton_Sereg
 *
 */
public class TimestampBasedIdGeneratorTest {

    @InjectMocks
    private TimestampBasedIdGenerator underTest;

    @Mock
    private AbortedMessageCounter abortedMessageCounter;
    @Mock
    private CurrentDateProvider currentDateProvider;
    @Mock
    private Logger logger;
    @Mock
    private MessageMarkerConfigurationAccess configurationAccess;
    @Mock
    private RequestLimits requestLimits;
    @Mock
    private ContextRefreshedEvent event;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "fileSimpleDateFormat", new SimpleDateFormat("MMddHHmmss"));
        Whitebox.setInternalState(underTest, "alertSimpleDateFormat", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss"));
        Whitebox.setInternalState(underTest, "errorLimit", 9999);
        Whitebox.setInternalState(underTest, "warningLimit", 9000);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousTimestampIsNull() throws TooManyRequestsException {
        // GIVEN
        String expected = "0624145212.0000";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date date = cal.getTime();
        Whitebox.setInternalState(underTest, "previousSimpleDate", null);
        given(currentDateProvider.getCurrentDate()).willReturn(date);
        // WHEN
        String actual = underTest.nextIdentifier();
        // THEN
        verify(currentDateProvider).getCurrentDate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousTimestampDiffersFromTheCurrent() throws TooManyRequestsException {
        // GIVEN
        String expected = "0624145212.0000";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date date = cal.getTime();
        Whitebox.setInternalState(underTest, "previousSimpleDate", "0624145211");
        given(currentDateProvider.getCurrentDate()).willReturn(date);
        // WHEN
        String actual = underTest.nextIdentifier();
        // THEN
        verify(currentDateProvider).getCurrentDate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNextIdentifierShouldReturnAProperIdWhenThePreviousTimestampEqualsTheCurrent() throws TooManyRequestsException {
        // GIVEN
        String expected = "0624145212.0888";

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();
        Whitebox.setInternalState(underTest, "previousSimpleDate", "0624145212");
        Whitebox.setInternalState(underTest, "currentNumber", new AtomicInteger(888));
        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        // WHEN
        String actual = underTest.nextIdentifier();
        // THEN
        verify(currentDateProvider).getCurrentDate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testNextIdentifierShouldLogAbortedMessagesWhenThePreviousTimestampEqualsTheCurrent() throws TooManyRequestsException {
        // GIVEN
        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();
        Whitebox.setInternalState(underTest, "previousSimpleDate", "0624145212");
        Whitebox.setInternalState(underTest, "currentNumber", new AtomicInteger(888));
        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        // WHEN
        underTest.nextIdentifier();
        // THEN
        verify(abortedMessageCounter).logNumberOfAbortedMessages();
    }

    @Test
    public void testNextIdentifierShouldLogWarningIfIdReachesWarningLimit() throws TooManyRequestsException {
        // GIVEN
        Whitebox.setInternalState(underTest, "logger", logger);

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();

        Whitebox.setInternalState(underTest, "previousSimpleDate", "0624145212");
        Whitebox.setInternalState(underTest, "currentNumber", new AtomicInteger(8999));

        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        // WHEN
        underTest.nextIdentifier();
        // THEN
        verify(logger).warn("ALERT: Message load close to its limit! Timestamp: @2013-06-24 14:52:12.");
    }

    @Test(expectedExceptions = TooManyRequestsException.class)
    public void testNextIdentifierShouldThrowExceptionIfIdReachesErrorLimit() throws TooManyRequestsException {
        // GIVEN
        Whitebox.setInternalState(underTest, "logger", logger);

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 5, 24, 14, 52, 12);
        Date currentDate = cal.getTime();

        Whitebox.setInternalState(underTest, "previousSimpleDate", "0624145212");
        Whitebox.setInternalState(underTest, "currentNumber", new AtomicInteger(9999));

        given(currentDateProvider.getCurrentDate()).willReturn(currentDate);
        // WHEN
        underTest.nextIdentifier();
        // THEN
        verify(abortedMessageCounter).incrementAbortedMessages(Mockito.anyString());
    }

    @Test
    public void testOnApplicationEventShouldSetErrorLimit() {
        //GIVEN
        Integer errorLimit = 9999;
        given(configurationAccess.getProperties()).willReturn(requestLimits);
        given(requestLimits.getErrorLimit()).willReturn(errorLimit);
        //WHEN
        underTest.onApplicationEvent(event);
        //THEN
        Integer actual = (Integer) Whitebox.getInternalState(underTest, "errorLimit");
        assertEquals(actual, errorLimit);
    }

    @Test
    public void testOnApplicationEventShouldSetWarningLimit() {
        //GIVEN
        Integer warningLimit = 9000;
        given(configurationAccess.getProperties()).willReturn(requestLimits);
        given(requestLimits.getWarningLimit()).willReturn(warningLimit);
        //WHEN
        underTest.onApplicationEvent(event);
        //THEN
        Integer actual = (Integer) Whitebox.getInternalState(underTest, "warningLimit");
        assertEquals(actual, warningLimit);
    }
}
