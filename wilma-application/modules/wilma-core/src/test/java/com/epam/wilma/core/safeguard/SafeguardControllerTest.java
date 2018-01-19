package com.epam.wilma.core.safeguard;
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

import static org.mockito.Mockito.verify;

import com.epam.wilma.logger.request.jms.JmsRequestLogger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.core.processor.response.jms.ResponseQueueListener;

/**
 * Provides unit tests for the class {@link SafeguardController}.
 * @author Tunde_Kovacs
 *
 */
public class SafeguardControllerTest {

    @Mock
    private JmsRequestLogger jmsRequestLogger;
    @Mock
    private ResponseQueueListener responseQueueListener;

    @InjectMocks
    private SafeguardController underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetFIDecompressionEnabledShouldCallResponseQueueListener() {
        //GIVEN in setUp
        //WHEN
        underTest.setFIDecompressionEnabled(true);
        //THEN
        verify(responseQueueListener).setFiDecompressionEnabled(true);
    }

    @Test
    public void testSetMessageWritingEnabledShouldCallBothRequestAndResponseQueueListeners() {
        //GIVEN in setUp
        //WHEN
        underTest.setMessageWritingEnabled(true);
        //THEN
        verify(responseQueueListener).setMessageLoggingEnabled(true);
        verify(jmsRequestLogger).setMessageLoggingEnabled(true);
    }
}
