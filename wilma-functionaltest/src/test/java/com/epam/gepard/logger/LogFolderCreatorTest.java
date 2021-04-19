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

import java.io.File;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

import com.epam.gepard.common.Environment;

/**
 * Unit tests for {@link LogFolderCreator}.
 * @author Tibor_Kovacs
 */
public class LogFolderCreatorTest {

    private Environment environment;

    private LogFolderCreator underTest;

    @Before
    public void setup() {
        environment = new Environment();
        underTest = new LogFolderCreator(environment);
    }

    @Test
    public void testPrepareOutputFolders() {
        //GIVEN
        String[] resultPaths = new String[]{"build/tmp/logFolderCreatorTest/xml", "build/tmp/logFolderCreatorTest/csv",
            "build/tmp/logFolderCreatorTest/html"};
        environment.setProperty(Environment.GEPARD_RESULT_PATH, "build/tmp/logFolderCreatorTest");
        environment.setProperty(Environment.GEPARD_XML_RESULT_PATH, resultPaths[0]);
        environment.setProperty(Environment.GEPARD_CSV_RESULT_PATH, resultPaths[1]);
        environment.setProperty(Environment.GEPARD_HTML_RESULT_PATH, resultPaths[2]);
        //WHEN
        underTest.prepareOutputFolders();
        //THEN
        for (int i = 0; i < 3; i++) {
            File f = new File(resultPaths[i]);
            if (f.exists()) {
                Assert.assertTrue(true);
            } else {
                Assert.assertTrue(false);
            }
        }
    }
}
