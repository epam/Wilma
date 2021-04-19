package com.epam.gepard.logger;

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

import static org.mockito.Mockito.verify;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link LogFinalizer}.
 * @author Tibor_Kovacs
 */
public class LogFinalizerTest {

    @Mock
    private Properties props;
    @Mock
    private LogFileWriter htmlLog;
    @Mock
    private LogFileWriter csvLog;
    @Mock
    private LogFileWriter quickLog;

    private LogFinalizer underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new LogFinalizer();
    }

    @Test
    public void testFinalizeLogs() {
        //GIVEN
        int threadCount = 3;
        //WHEN
        underTest.finalizeLogs(props, htmlLog, csvLog, quickLog, threadCount);
        //THEN
        verify(props).setProperty("GEPARDVERSION", "unknown (no manifest file) with " + threadCount + " thread(s).");
        verify(htmlLog).insertBlock("Footer", props);
        verify(htmlLog).close();
        verify(csvLog).insertBlock("Footer", props);
        verify(csvLog).close();
        verify(quickLog).insertBlock("Footer", props);
        verify(quickLog).close();
    }
}
