package com.epam.gepard.datadriven.feeders;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.epam.gepard.datadriven.DataDrivenParameterArray;

/**
 * Unit tests for {@link SingleRowSelectorDataFeeder}.
 * @author Tibor_Kovacs
 */
public class SingleRowSelectorDataFeederTest {

    private SingleRowSelectorDataFeeder underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new SingleRowSelectorDataFeeder();
    }

    @Test
    public void testInitWhenParameterIsNull() {
        //GIVEN
        int expected = GepardDataFeeder.INIT_FAILED_WITH_INVALID_PARAMETER;
        //WHEN
        int actual = underTest.init("com.epam.test.TestClass", null, null);
        //THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitWhenParameterIsGreaterThenZero() {
        //GIVEN
        int expected = 0;
        //WHEN
        int actual = underTest.init("com.epam.test.TestClass", "2", null);
        //THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitWhenParameterIsLesserThenZero() {
        //GIVEN
        int expected = GepardDataFeeder.INIT_FAILED_WITH_PARAMETER_ERROR;
        //WHEN
        int actual = underTest.init("com.epam.test.TestClass", "-2", null);
        //THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitWhenParameterIsNotInteger() {
        //GIVEN
        int expected = GepardDataFeeder.INIT_FAILED_WITH_INVALID_PARAMETER;
        //WHEN
        int actual = underTest.init("com.epam.test.TestClass", "bla", null);
        //THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateRuns() {
        //GIVEN
        int expected = 1;
        //WHEN
        int actual = underTest.calculateRuns("com.epam.test.TestClass", 2);
        //THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateParameterArrayWhenInputArrayIsNotNull() {
        //GIVEN
        Whitebox.setInternalState(underTest, "selectedRow", 0);
        String[] expectedParameterNames = new String[]{"something"};
        DataDrivenParameterArray testArray = new DataDrivenParameterArray();
        String[] parameterNames = new String[1];
        parameterNames[0] = "something";
        testArray.setParameterNames(parameterNames);
        String[] firstRow = new String[1];
        firstRow[0] = "oneRow";
        testArray.put(0, firstRow);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("com.epam.test.TestClass", testArray);
        //THEN
        String[] actualParameterNames = actual.getParameterNames();
        for (int i = 0; i < actualParameterNames.length; i++) {
            Assert.assertEquals(expectedParameterNames[i], actualParameterNames[i]);
        }
    }

    @Test
    public void testCalculateParameterArrayWhenInputArrayIsNull() {
        //GIVEN
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("com.epam.test.TestClass", null);
        //THEN
        Assert.assertNull(actual);
    }

    @Test
    public void testCalculateParameterArrayWhenSelectedRowIsGreaterThenInputArraySize() {
        //GIVEN
        Whitebox.setInternalState(underTest, "selectedRow", 5);
        DataDrivenParameterArray testArray = new DataDrivenParameterArray();
        String[] parameterNames = new String[1];
        parameterNames[0] = "something";
        testArray.setParameterNames(parameterNames);
        String[] firstRow = new String[1];
        firstRow[0] = "oneRow";
        testArray.put(0, firstRow);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("com.epam.test.TestClass", testArray);
        //THEN
        Assert.assertNull(actual);
    }
}
