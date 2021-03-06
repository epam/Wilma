package com.epam.wilma.sequence.formatters.helper.converter;
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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link NeutralConverter}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class NeutralConverterTest {

    private NeutralConverter underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new NeutralConverter();
    }

    @Test
    public void testConvertShouldReturnTheSameData() {
        //GIVEN
        String data = "anything";
        String name = "not used";
        //WHEN
        String result = underTest.convert(data, name);
        //THEN
        assertEquals(data, result);
    }

}
