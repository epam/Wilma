package com.epam.wilma.messagemarker.abortcounter;
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

import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for AbortedMessageCounter class.
 * @author Tamas_Bihari
 *
 */
public class AbortedMessageCounterTest {
    private static final String A_TIMESTAMP = "A TIMESTAMP";

    private AbortedMessageCounter underTest;

    @Mock
    private Logger logger;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new AbortedMessageCounter();
    }

    @Test
    public void testIncrementAbortedMessagesShouldReturnProperly() {
        //GIVEN
        int before = (int) Whitebox.getInternalState(underTest, "abortedRequestCounter");
        //WHEN
        underTest.incrementAbortedMessages(A_TIMESTAMP);
        //THEN
        int expected = (int) Whitebox.getInternalState(underTest, "abortedRequestCounter");
        assertEquals(before + 1, expected);
    }

    @Test
    public void testLogNumberOfAbortedMessagesShouldReturnProperlyWhenAbortedMessageCountIsZero() {
        //GIVEN
        int before = (int) Whitebox.getInternalState(underTest, "abortedRequestCounter");
        //WHEN
        underTest.logNumberOfAbortedMessages();
        //THEN
        int after = (int) Whitebox.getInternalState(underTest, "abortedRequestCounter");
        assertEquals(before, after);
    }

    @Test
    public void testLogNumberOfAbortedMessagesShouldReturnProperlyWhenAbortedMessageCountIsGreaterThanZero() {
        //GIVEN
        String expected = "ALERT: Messages overload, " + 1 + " messages were dropped @" + A_TIMESTAMP + "!";
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.incrementAbortedMessages(A_TIMESTAMP);
        //WHEN
        underTest.logNumberOfAbortedMessages();
        //THEN
        int after = (int) Whitebox.getInternalState(underTest, "abortedRequestCounter");
        verify(logger).info(expected);
        assertEquals(0, after);
    }
}
