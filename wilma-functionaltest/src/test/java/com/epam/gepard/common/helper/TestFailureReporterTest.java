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

import java.io.File;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

import com.epam.gepard.common.Environment;
import com.epam.gepard.exception.ComplexGepardException;

/**
 * Unit tests for {@link TestFailureReporter}.
 * @author Tibor_Kovacs
 */
public class TestFailureReporterTest {

    private Environment environment;

    private TestFailureReporter underTest;

    @Before
    public void setup() {
        environment = new Environment();
        underTest = new TestFailureReporter(environment);
    }

    @Test
    public void testGenerateTestlistFailure() {
        //GIVEN
        String folder = "build";
        String filePath = "GeneratedFailure.txt";
        environment.setProperty(Environment.GEPARD_TESTLIST_FAILURE_PATH, folder);
        environment.setProperty(Environment.GEPARD_TESTLIST_FAILURE_FILE, filePath);
        //WHEN
        underTest.generateTestlistFailure();
        //THEN
        File f = new File(folder + "/" + filePath);
        if (f.exists()) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    @Test(expected = ComplexGepardException.class)
    public void testGenerateTestlistFailureWhenExceptionOccuresDuringFileWriting() {
        //GIVEN
        String folder = "build";
        String filePath = "";
        environment.setProperty(Environment.GEPARD_TESTLIST_FAILURE_PATH, folder);
        environment.setProperty(Environment.GEPARD_TESTLIST_FAILURE_FILE, filePath);
        //WHEN
        underTest.generateTestlistFailure();
        //THEN expected EXCEPTION
    }
}
