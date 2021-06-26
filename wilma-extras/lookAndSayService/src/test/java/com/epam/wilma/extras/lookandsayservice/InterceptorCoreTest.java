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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test class for InterceptorCore class.
 *
 * @author tkohegyi
 */
public class InterceptorCoreTest {

    private InterceptorCore underTest;

    @Before
    public void setUp() {
        underTest = new InterceptorCore();
    }

    @Test
    public void testMinimumCall() {
        String response = underTest.handleIteration("");
        assertTrue(response.length() == 0);
    }

    @Test
    public void test1() {
        String response = underTest.handleIteration("1");
        assertTrue("11".equals(response));
    }

    @Test
    public void test11() {
        String response = underTest.handleIteration("11");
        assertTrue("21".equals(response));
    }

    @Test
    public void test21() {
        String response = underTest.handleIteration("21");
        assertTrue("1211".equals(response));
    }

    @Test
    public void test1211() {
        String response = underTest.handleIteration("1211");
        assertTrue("111221".equals(response));
    }

    @Test
    public void test1971() {
        String response = underTest.handleIteration("1971");
        assertTrue("11191711".equals(response));
    }

}
