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

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.epam.gepard.common.Environment;
import com.epam.gepard.datadriven.DataDrivenParameterArray;
import com.epam.gepard.datadriven.feeders.SimpleMultiplierDataFeeder.ConfigFileInfo;

/**
 * Unit tests for {@link SimpleMultiplierDataFeeder}.
 * @author tkohegyi, Tibor_Kovacs
 */
public class SimpleMultiplierDataFeederTest {

    @Mock
    private DataDrivenParameterArray parameterArray;
    @Mock
    private SimpleMultiplierDataFeederFileLoader arrayLoader;
    @InjectMocks
    private SimpleMultiplierDataFeeder underTest;
    private Environment environment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        environment = new Environment();
    }

    @Test
    public void testInitGood() throws Exception {
        //GIVEN
        String classname = "classname";
        String parameter = "2";
        //WHEN
        int response = underTest.init(classname, parameter, environment);
        //THEN
        Assert.assertTrue(response == 0);
    }

    @Test
    public void testInitBadParameterNotInteger() throws Exception {
        //GIVEN
        String classname = "classname";
        String parameter = "blah";
        //WHEN
        int response = underTest.init(classname, parameter, environment);
        //THEN
        Assert.assertTrue(response == -1);
    }

    @Test
    public void testInitBadParameterNegative() throws Exception {
        //GIVEN
        String classname = "classname";
        String parameter = "-2";
        //WHEN
        int response = underTest.init(classname, parameter, environment);
        //THEN
        Assert.assertTrue(response == -2);
    }

    @Test
    public void testCalculateRuns() throws Exception {
        //GIVEN
        String classname = "classname";
        int parameter = 2;
        underTest.init(classname, String.valueOf(parameter), environment);
        //WHEN
        int response = underTest.calculateRuns(classname, parameter);
        //THEN
        Assert.assertTrue(response == parameter * parameter);
    }

    @Test
    public void testCalculateParameterArray() throws IOException {
        //GIVEN
        Whitebox.setInternalState(underTest, "multiplier", 2);
        Whitebox.setInternalState(underTest, "arrayLoader", arrayLoader);
        Whitebox.setInternalState(underTest, "environment", environment);
        environment.setProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE, new File("src/test/resources").getAbsolutePath());
        given(arrayLoader.loadFile(Mockito.any(ConfigFileInfo.class), eq(2))).willReturn(parameterArray);
        given(parameterArray.isEmpty()).willReturn(false);
        given(parameterArray.size()).willReturn(2);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("TestClass", null);
        //THEN
        Assert.assertEquals(parameterArray, actual);
    }

    @Test
    public void testCalculateParameterArrayWhenArrayIsEmpty() throws IOException {
        //GIVEN
        Whitebox.setInternalState(underTest, "multiplier", 2);
        Whitebox.setInternalState(underTest, "arrayLoader", arrayLoader);
        Whitebox.setInternalState(underTest, "environment", environment);
        environment.setProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE, new File("src/test/resources").getAbsolutePath());
        given(arrayLoader.loadFile(Mockito.any(ConfigFileInfo.class), eq(2))).willReturn(parameterArray);
        given(parameterArray.isEmpty()).willReturn(true);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("TestClass", null);
        //THEN
        Assert.assertNull(actual);
    }

    @Test
    public void testCalculateParameterArrayWhenArrayNotContainsEnoughParameter() throws IOException {
        //GIVEN
        Whitebox.setInternalState(underTest, "multiplier", 2);
        Whitebox.setInternalState(underTest, "arrayLoader", arrayLoader);
        Whitebox.setInternalState(underTest, "environment", environment);
        environment.setProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE, new File("src/test/resources").getAbsolutePath());
        given(arrayLoader.loadFile(Mockito.any(ConfigFileInfo.class), eq(2))).willReturn(parameterArray);
        given(parameterArray.isEmpty()).willReturn(false);
        given(parameterArray.size()).willReturn(1);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("TestClass", null);
        //THEN
        Assert.assertNull(actual);
    }

    @Test
    public void testCalculateParameterArrayWhenSourceFileNotExists() throws IOException {
        //GIVEN
        Whitebox.setInternalState(underTest, "multiplier", 2);
        Whitebox.setInternalState(underTest, "arrayLoader", arrayLoader);
        Whitebox.setInternalState(underTest, "environment", environment);
        environment.setProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE, new File("src/test/resources").getAbsolutePath());
        given(arrayLoader.loadFile(Mockito.any(ConfigFileInfo.class), eq(2))).willReturn(parameterArray);
        given(parameterArray.isEmpty()).willReturn(false);
        given(parameterArray.size()).willReturn(1);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("something", null);
        //THEN
        Assert.assertNull(actual);
    }

    @Test
    public void testCalculateParameterArrayWhenInputArrayIsNotNull() throws IOException {
        //GIVEN
        String[] firstRow = new String[]{"first"};
        String[] secondRow = new String[]{"second"};
        Whitebox.setInternalState(underTest, "multiplier", 2);
        Whitebox.setInternalState(underTest, "arrayLoader", arrayLoader);
        Whitebox.setInternalState(underTest, "environment", environment);
        environment.setProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE, new File("src/test/resources").getAbsolutePath());
        given(arrayLoader.loadFile(Mockito.any(ConfigFileInfo.class), eq(2))).willReturn(parameterArray);
        given(parameterArray.isEmpty()).willReturn(false);
        given(parameterArray.size()).willReturn(2);
        given(parameterArray.get(0)).willReturn(firstRow);
        given(parameterArray.get(1)).willReturn(secondRow);
        given(parameterArray.getParameterNames()).willReturn(secondRow);
        DataDrivenParameterArray inputArray = new DataDrivenParameterArray();
        inputArray.setParameterNames(firstRow);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("TestClass", inputArray);
        //THEN
        Assert.assertEquals(firstRow[0], actual.getParameterNames()[0]);
        Assert.assertEquals(secondRow[0], actual.getParameterNames()[1]);
    }

    @Test
    public void testCalculateParameterArrayWhenInputArrayIsNotNullAndContainsRealDatas() throws IOException {
        //GIVEN
        String[] firstRow = new String[]{"first"};
        String[] secondRow = new String[]{"second"};
        Whitebox.setInternalState(underTest, "multiplier", 2);
        Whitebox.setInternalState(underTest, "arrayLoader", arrayLoader);
        Whitebox.setInternalState(underTest, "environment", environment);
        environment.setProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE, new File("src/test/resources").getAbsolutePath());
        given(arrayLoader.loadFile(Mockito.any(ConfigFileInfo.class), eq(2))).willReturn(parameterArray);
        given(parameterArray.isEmpty()).willReturn(false);
        given(parameterArray.size()).willReturn(2);
        given(parameterArray.get(0)).willReturn(firstRow);
        given(parameterArray.get(1)).willReturn(secondRow);
        given(parameterArray.getParameterNames()).willReturn(secondRow);
        DataDrivenParameterArray inputArray = new DataDrivenParameterArray();
        inputArray.setParameterNames(firstRow);
        inputArray.put(1, firstRow);
        inputArray.put(2, secondRow);
        //WHEN
        DataDrivenParameterArray actual = underTest.calculateParameterArray("TestClass", inputArray);
        //THEN
        Object[] actualValues = actual.values().toArray();
        Object[] expectedValues = new Object[]{new String[]{"first", "first"}, new String[]{"second", "first"}, new String[]{"first", "second"},
            new String[]{"second", "second"}};
        Assert.assertEquals(firstRow[0], actual.getParameterNames()[0]);
        Assert.assertEquals(secondRow[0], actual.getParameterNames()[1]);
        for (int i = 0; i < actualValues.length; i++) {
            Assert.assertArrayEquals(expectedValues, actualValues);
        }
    }
}
