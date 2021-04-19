package com.epam.gepard.datadriven;

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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.epam.gepard.common.Environment;
import com.epam.gepard.datadriven.feeders.GepardDataFeeder;
import com.epam.gepard.exception.ShutDownException;

/**
 * Unit tests for {@link DataFeederLoader}.
 * @author Tibor_Kovacs
 */
public class DataFeederLoaderTest {

    @Mock
    private GepardDataFeeder feeder;
    @Mock
    private DataFeederLoader nextFeederLoader;
    @Mock
    private DataDrivenParameterArray parameterArray;
    @Mock
    private DataDrivenParameterArray inputParameterArray;
    @Mock
    private DataDrivenParameterArray outputParameterArray;

    private DataFeederLoader underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Environment environment = new Environment();
        underTest = new DataFeederLoader("com.epam.test.TestClass", "", environment);
    }

    @Test
    public void testCalculateRunsWhenThereIsOnlyOneFeeder() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        int count = 2;
        int expected = 4;
        Whitebox.setInternalState(underTest, "feeder", feeder);
        Whitebox.setInternalState(underTest, "nextDataFeederLoader", null);
        given(feeder.calculateRuns(className, count)).willReturn(expected);
        //WHEN
        int actual = underTest.calculateRuns(className, count);
        //THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateRunsWhenThereAreSomeFeeder() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        int count = 2;
        int partialResult = 4;
        int expected = 5;
        Whitebox.setInternalState(underTest, "feeder", feeder);
        Whitebox.setInternalState(underTest, "nextDataFeederLoader", nextFeederLoader);
        given(feeder.calculateRuns(className, count)).willReturn(partialResult);
        given(nextFeederLoader.calculateRuns(className, partialResult)).willReturn(expected);
        //WHEN
        int actual = underTest.calculateRuns(className, count);
        //THEN
        verify(nextFeederLoader).calculateRuns(className, partialResult);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ShutDownException.class)
    public void testCalculateRunsWhenFeederIsNull() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        int count = 2;
        //WHEN
        underTest.calculateRuns(className, count);
        //THEN
    }

    @Test(expected = ShutDownException.class)
    public void testCalculateRunsWhenCalculateResultIsMinus() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        int count = 2;
        Whitebox.setInternalState(underTest, "feeder", feeder);
        given(feeder.calculateRuns(className, count)).willReturn(-2);
        //WHEN
        underTest.calculateRuns(className, count);
        //THEN
    }

    @Test
    public void testGetParameterRowWhenParameterNamesNotNull() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        int rowNumber = 1;
        String[] arrayElement = new String[]{"something"};
        String[] parameterNames = new String[]{"PAR0"};
        Whitebox.setInternalState(underTest, "parameterArray", parameterArray);
        given(parameterArray.get(rowNumber)).willReturn(arrayElement);
        //WHEN
        DataDrivenParameters actual = underTest.getParameterRow(className, rowNumber);
        //THEN
        Assert.assertArrayEquals(parameterNames, actual.getParameterNames());
        Assert.assertArrayEquals(arrayElement, actual.getParameters());
    }

    @Test
    public void testGetParameterRowWhenParameterNamesNull() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        int rowNumber = 1;
        String[] arrayElement = new String[]{"something"};
        String[] parameterNames = new String[]{"test"};
        Whitebox.setInternalState(underTest, "parameterArray", parameterArray);
        given(parameterArray.get(rowNumber)).willReturn(arrayElement);
        given(parameterArray.getParameterNames()).willReturn(parameterNames);
        //WHEN
        DataDrivenParameters actual = underTest.getParameterRow(className, rowNumber);
        //THEN
        Assert.assertArrayEquals(parameterNames, actual.getParameterNames());
        Assert.assertArrayEquals(arrayElement, actual.getParameters());
    }

    @Test
    public void testCalculateParameterArray() {
        //GIVEN
        DataDrivenParameterArray expectedArray = new DataDrivenParameterArray();
        String className = "com.epam.test.TestClass";
        Whitebox.setInternalState(underTest, "feeder", feeder);
        Whitebox.setInternalState(underTest, "nextDataFeederLoader", null);
        given(feeder.calculateParameterArray(className, inputParameterArray)).willReturn(expectedArray);
        //WHEN
        DataDrivenParameterArray actualArray = underTest.calculateParameterArray(className, inputParameterArray);
        //THEN
        Assert.assertEquals(expectedArray, actualArray);
    }

    @Test
    public void testCalculateParameterArrayWhenThereIsNextFeederLoader() {
        //GIVEN
        DataDrivenParameterArray expectedArray = new DataDrivenParameterArray();
        String className = "com.epam.test.TestClass";
        Whitebox.setInternalState(underTest, "feeder", feeder);
        Whitebox.setInternalState(underTest, "nextDataFeederLoader", nextFeederLoader);
        given(feeder.calculateParameterArray(className, inputParameterArray)).willReturn(outputParameterArray);
        given(nextFeederLoader.calculateParameterArray(className, outputParameterArray)).willReturn(expectedArray);
        //WHEN
        DataDrivenParameterArray actualArray = underTest.calculateParameterArray(className, inputParameterArray);
        //THEN
        verify(nextFeederLoader).calculateParameterArray(className, outputParameterArray);
        Assert.assertEquals(expectedArray, actualArray);
    }

    @Test(expected = ShutDownException.class)
    public void testCalculateParameterArrayWhenFeederIsNull() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        Whitebox.setInternalState(underTest, "feeder", null);
        //WHEN
        underTest.calculateParameterArray(className, inputParameterArray);
        //THEN
    }

    @Test(expected = ShutDownException.class)
    public void testCalculateParameterArrayWhenFeederCalculateParameterArrayReturnsWithNull() {
        //GIVEN
        String className = "com.epam.test.TestClass";
        Whitebox.setInternalState(underTest, "feeder", feeder);
        given(feeder.calculateParameterArray(className, inputParameterArray)).willReturn(null);
        //WHEN
        underTest.calculateParameterArray(className, inputParameterArray);
        //THEN
    }
}
