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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.gepard.datadriven.DataDrivenParameterArray;
import com.epam.gepard.datadriven.feeders.SimpleMultiplierDataFeeder.ConfigFileInfo;

/**
 * Unit tests for {@link SimpleMultiplierDataFeederFileLoader}.
 * @author Tibor_Kovacs
 */
public class SimpleMultiplierDataFeederFileLoaderTest {

    @Mock
    private ConfigFileInfo fileInfo;

    private SimpleMultiplierDataFeederFileLoader underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new SimpleMultiplierDataFeederFileLoader();
    }

    @Test
    public void testLoadFileWithEmptyFile() throws IOException {
        //GIVEN
        given(fileInfo.getConfigFilename()).willReturn("src/test/resources/TestClass.txt");
        //WHEN
        DataDrivenParameterArray actual = underTest.loadFile(fileInfo, 0);
        //THEN
        Assert.assertNotNull(actual);
    }

    @Test
    public void testLoadFileWithRealFile() throws IOException {
        //GIVEN
        given(fileInfo.getConfigFilename()).willReturn("src/test/resources/DDParameterLoaderTestlist.txt");
        given(fileInfo.isCSV()).willReturn(false);
        given(fileInfo.getSplitter()).willReturn(",");
        //WHEN
        DataDrivenParameterArray actual = underTest.loadFile(fileInfo, 3);
        //THEN
        String[] actualDatas = (String[]) actual.values().toArray()[0];
        String[] expectedDatas = new String[]{"gepard.test.TestClass", "2"};
        Assert.assertArrayEquals(expectedDatas, actualDatas);
    }
}
