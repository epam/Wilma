package com.epam.gepard;

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

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.helper.ReportFinalizer;
import com.epam.gepard.common.helper.ResultCollector;
import com.epam.gepard.common.helper.TestFailureReporter;
import com.epam.gepard.common.threads.ExecutorThreadManager;
import com.epam.gepard.logger.LogFileWriter;
import com.epam.gepard.logger.LogFinalizer;
import com.epam.gepard.logger.LogFolderCreator;
import com.epam.gepard.logger.helper.LogFileWriterFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Properties;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AllTestRunner}.
 *
 * @author Tibor_Kovacs
 */
public class AllTestRunnerTest {

    @Mock
    private LogFolderCreator logFolderCreator;
    @Mock
    private ExecutorThreadManager executorThreadManager;
    @Mock
    private LogFileWriterFactory logFileWriterFactory;
    @Mock
    private LogFileWriter htmlLog;
    @Mock
    private LogFileWriter csvLog;
    @Mock
    private LogFileWriter quickLog;
    @Mock
    private ResultCollector resultCollector;
    @Mock
    private ReportFinalizer reportFinalizer;
    @Mock
    private LogFinalizer logFinalizer;
    @Mock
    private TestFailureReporter failureReporter;

    private Environment environment;

    @InjectMocks
    private AllTestRunner underTest;

    @Before
    public void setup() {
        environment = new Environment();
        underTest = new AllTestRunner(environment, reportFinalizer, failureReporter, logFolderCreator);
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logFileWriterFactory", logFileWriterFactory);
        given(
                logFileWriterFactory.createSpecificLogWriter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(Environment.class))).willReturn(htmlLog).willReturn(csvLog).willReturn(quickLog);
        environment.setProperty(Environment.GEPARD_FILTER_CLASS, "com.epam.gepard.filter.DefaultTestFilter");
        environment.setProperty(Environment.GEPARD_FILTER_EXPRESSION, "?");
    }

    @Test
    public void testRunAllWithEmptyTestlist() throws Exception {
        //GIVEN
        String testListFile = "src/test/resources/testlist_empty.txt";
        Whitebox.setInternalState(underTest, "executorThreadManager", executorThreadManager);
        environment.setProperty(Environment.GEPARD_LOAD_AND_EXIT, "false");
        //WHEN
        underTest.runAll(testListFile);
        //THEN
        verify(logFolderCreator).prepareOutputFolders();
        verify(executorThreadManager).initiateAndStartExecutorThreads(environment.getProperty(Environment.GEPARD_THREADS),
                environment.getProperty(Environment.GEPARD_XML_RESULT_PATH));
        verify(htmlLog).insertBlock(eq("Header"), Mockito.any(Properties.class));
        verify(csvLog).insertBlock(eq("Header"), Mockito.any(Properties.class));
        verify(quickLog).insertBlock(eq("Header"), Mockito.any(Properties.class));
        verify(executorThreadManager).closeRunningThreads();
        verify(failureReporter).generateTestlistFailure();
    }

    //    @Test(expected = ShutDownException.class)
    public void testRunAllWhenGepardLoadAndExitFalse() throws Exception {
        //GIVEN
        String testListFile = "src/test/resources/testlist.txt";
        environment.setProperty(Environment.GEPARD_LOAD_AND_EXIT, "true");
        //WHEN
        underTest.runAll(testListFile);
        //THEN expected Exception
    }

    //    @Test(expected = ShutDownException.class)
    public void testRunAllWhenTestListNull() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.runAll(null);
        //THEN expected Exception
    }

}
