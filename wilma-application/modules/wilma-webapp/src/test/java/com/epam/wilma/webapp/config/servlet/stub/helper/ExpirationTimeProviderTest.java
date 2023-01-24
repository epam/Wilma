package com.epam.wilma.webapp.config.servlet.stub.helper;
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

import com.epam.wilma.common.helper.CurrentDateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link ExpirationTimeProvider}.
 *
 * @author Tibor_Kovacs
 */
public class ExpirationTimeProviderTest {
    @Mock
    private CurrentDateProvider currentDateProvider;

    @InjectMocks
    private ExpirationTimeProvider underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(currentDateProvider.getCurrentTimeInMillis()).willReturn(10000000L);
    }

    @Test
    public void testGetExpirationMinutes() {
        //GIVEN
        long expected = 10L;
        //WHEN
        long result = underTest.getExpirationMinutes(10600000L);
        //THEN
        assertEquals(result, expected);
    }

    @Test
    public void testGetExpirationSeconds() {
        //GIVEN
        long expected = 1L;
        //WHEN
        long result = underTest.getExpirationSeconds(106001000L);
        //THEN
        assertEquals(result, expected);
    }

}
