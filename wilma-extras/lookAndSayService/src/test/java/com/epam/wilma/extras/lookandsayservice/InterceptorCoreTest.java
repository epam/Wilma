package com.epam.wilma.extras.lookandsayservice;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for InterceptorCore class.
 *
 * @author Tamas_Kohegyi
 */
public class InterceptorCoreTest {

    private InterceptorCore underTest;

    @BeforeEach
    public void setUp() {
        underTest = new InterceptorCore();
    }

    @Test
    public void testMinimumCall() {
        String response = underTest.handleIteration("");
        assertEquals(0, response.length());
    }

    @Test
    public void test1() {
        String response = underTest.handleIteration("1");
        assertEquals("11", response);
    }

    @Test
    public void test11() {
        String response = underTest.handleIteration("11");
        assertEquals("21", response);
    }

    @Test
    public void test21() {
        String response = underTest.handleIteration("21");
        assertEquals("1211", response);
    }

    @Test
    public void test1211() {
        String response = underTest.handleIteration("1211");
        assertEquals("111221", response);
    }

    @Test
    public void test1971() {
        String response = underTest.handleIteration("1971");
        assertEquals("11191711", response);
    }

}
