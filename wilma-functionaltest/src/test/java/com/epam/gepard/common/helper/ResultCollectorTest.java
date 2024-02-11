package com.epam.gepard.common.helper;

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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.helper.AllTestResults;
import com.epam.gepard.logger.LogFileWriter;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for {@link ResultCollector}.
 * @author Tibor_Kovacs
 */
public class ResultCollectorTest {

    @Mock
    private AllTestResults allTestResults;
    @Mock
    private LogFileWriter htmlLog;
    @Mock
    private LogFileWriter csvLog;

    private Environment environment;

    private ResultCollector underTest;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        environment = new Environment();
        underTest = new ResultCollector();
    }

    @Test
    public void testWaitForExecutionEndAndCollectResults() throws InterruptedException {
        //GIVEN
        Map<String, TestClassExecutionData> testClassMap = new LinkedHashMap<>();
        TestClassExecutionData executionData = new TestClassExecutionData("testID", environment);
        ReflectionTestUtils.setField(executionData, "lock", -1);
        ReflectionTestUtils.setField(executionData, "testURL", "testURL");
        ReflectionTestUtils.setField(executionData, "countDummy", 0);
        ReflectionTestUtils.setField(executionData, "countPassed", 0);
        ReflectionTestUtils.setField(executionData, "countFailed", 0);
        ReflectionTestUtils.setField(executionData, "countNA", 0);

        testClassMap.put("test.class", executionData);
        GenericListTestSuite.setTestClassMap(testClassMap);
        //WHEN
        underTest.waitForExecutionEndAndCollectResults(allTestResults, htmlLog, csvLog);
        //THEN
        verify(htmlLog).insertBlock(eq("TestRow"), Mockito.any(Properties.class));
        verify(csvLog).insertBlock(eq("TestRow"), Mockito.any(Properties.class));
        boolean resultOdd = (boolean) ReflectionTestUtils.getField(underTest, "odd");
        Assert.assertTrue(resultOdd);
    }

    @Test
    public void testWaitForExecutionEndAndCollectResultsWithTwoDatas() throws InterruptedException {
        //GIVEN
        Map<String, TestClassExecutionData> testClassMap = new LinkedHashMap<>();
        TestClassExecutionData executionData = new TestClassExecutionData("testID", environment);
        ReflectionTestUtils.setField(executionData, "lock", -1);
        ReflectionTestUtils.setField(executionData, "testURL", "testURL");
        ReflectionTestUtils.setField(executionData, "countDummy", 0);
        ReflectionTestUtils.setField(executionData, "countPassed", 0);
        ReflectionTestUtils.setField(executionData, "countFailed", 0);
        ReflectionTestUtils.setField(executionData, "countNA", 0);

        testClassMap.put("test.class.first", executionData);
        testClassMap.put("test.class.second", executionData);
        GenericListTestSuite.setTestClassMap(testClassMap);
        //WHEN
        underTest.waitForExecutionEndAndCollectResults(allTestResults, htmlLog, csvLog);
        //THEN
        verify(htmlLog, times(2)).insertBlock(eq("TestRow"), Mockito.any(Properties.class));
        verify(csvLog, times(2)).insertBlock(eq("TestRow"), Mockito.any(Properties.class));
        boolean resultOdd = (boolean) ReflectionTestUtils.getField(underTest, "odd");
        Assert.assertFalse(resultOdd);
    }
}
