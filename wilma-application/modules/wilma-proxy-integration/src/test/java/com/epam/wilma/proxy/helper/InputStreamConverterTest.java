package com.epam.wilma.proxy.helper;
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

import com.epam.wilma.domain.exception.ApplicationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the <tt>InputStreamConverter</tt>.
 *
 * @author Tunde_Kovacs
 */
public class InputStreamConverterTest {

    private static final String TEST = "test";

    @Mock
    private InputStream inputStreamMock;

    private InputStreamConverter underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new InputStreamConverter();
    }

    @Test
    public void testGetStringFromStreamShouldReturnCorrectString() throws ApplicationException {
        //GIVEN
        byte[] buffer = TEST.getBytes();
        InputStream inputStream = new ByteArrayInputStream(buffer);
        //WHEN
        String actual = underTest.getStringFromStream(inputStream);
        //THEN
        Assert.assertEquals(TEST, actual);
    }

    @Test
    public void testGetStringFromStreamShouldReturnEmptyStringWhenInputStreamIsNull() throws ApplicationException {
        //GIVEN
        //inputStream is null
        //WHEN
        String actual = underTest.getStringFromStream(null);
        //THEN
        Assert.assertEquals("", actual);
    }

    @Test(expected = ApplicationException.class)
    public void testGetStringFromStreamShouldThrowApplicationException() throws ApplicationException, IOException {
        //GIVEN
        given(inputStreamMock.available()).willThrow(new IOException());
        //WHEN
        underTest.getStringFromStream(inputStreamMock);
        //THEN it should throws ApplicationException
    }
}