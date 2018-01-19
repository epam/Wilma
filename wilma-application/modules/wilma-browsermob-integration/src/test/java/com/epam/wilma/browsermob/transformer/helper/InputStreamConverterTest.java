package com.epam.wilma.browsermob.transformer.helper;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;

/**
 * Provides unit tests for the <tt>InputStreamConverter</tt>.
 * @author Tunde_Kovacs
 */
public class InputStreamConverterTest {

    private static final String TEST = "test";

    private InputStream inputStream;
    @Mock
    private InputStream inputStreamMock;

    private InputStreamConverter underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new InputStreamConverter();
    }

    @Test
    public void testGetStringFromStreamShouldReturnCorrectString() throws ApplicationException {
        //GIVEN
        String s = TEST;
        byte[] buffer = s.getBytes();
        inputStream = new ByteArrayInputStream(buffer);
        //WHEN
        String actual = underTest.getStringFromStream(inputStream);
        //THEN
        Assert.assertEquals(actual, TEST);
    }

    @Test
    public void testGetStringFromStreamShouldReturnEmptyStringWhenInputStreamIsNull() throws ApplicationException {
        //GIVEN
        inputStream = null;
        //WHEN
        String actual = underTest.getStringFromStream(inputStream);
        //THEN
        Assert.assertEquals(actual, "");
    }

    @Test(expectedExceptions = ApplicationException.class)
    public void testGetStringFromStreamShouldThrowApplicationException() throws ApplicationException, IOException {
        //GIVEN
        given(inputStreamMock.available()).willThrow(new IOException());
        //WHEN
        underTest.getStringFromStream(inputStreamMock);
        //THEN it should throws ApplicationException
    }
}
