package com.epam.wilma.gepard.test.helper;

/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.NATestCaseException;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.generic.CommonTestCase;
import com.epam.gepard.util.Util;
import junit.framework.AssertionFailedError;
import junit.framework.TestResult;

import java.util.Properties;

/**
 * This class represents a TestCase, which supports HTML logs, and beforeTestCaseSet
 * and afterTestCaseSet event.
 *
 * @author Tamas Kohegyi
 */
public abstract class WilmaTestCaseBase extends CommonTestCase {

    /**
     * Constructor, use this for Wilma tests.
     */
    public WilmaTestCaseBase() {
        super("dummy-name");
    }

    /**
     * We override the run method in order to support HTML logs.
     *
     * @param result Test result
     */
    protected void runSuper(final TestResult result) {
        super.run(result);
    }

    /**
     * We override the run method in order to support HTML logs.
     *
     * @param result Test result
     */
    @Override
    public void run(final TestResult result) {
        TestClassExecutionData o = getClassData();

        setTcase(Environment.createTestCase(getName(), o.getTestCaseSet()));

        setUpLogger();
        Properties props = new Properties();
        props.setProperty("ID", getTestID());
        props.setProperty("Name", getTestName());
        props.setProperty("TestCase", getName());
        props.setProperty("ScriptNameRow", getClassData().getID());
        getMainTestLogger().insertBlock("Header", props);
        try {
            runSuper(result);
        } finally {
            getMainTestLogger().insertBlock("Footer", null);
            getMainTestLogger().close();
            setMainTestLogger(null);
            getTcase().updateStatus();
            setTcase(null);
        }
    }

    /**
     * We re-declared the runTest method in order to throw only AssertionFailedErrors and
     * to create the Failure logs.
     */
    @Override
    protected void runTest() {
        Util util = new Util();
        try {
            super.runTest();
            logEvent("<font color=\"#00AA00\"><b>Test passed.</b></font>");
            systemOutPrintLn("Test passed.");
        } catch (AssertionFailedError e) { //we rethrow the AssertionFailedError
            String stackTrace = getFullStackTrace(e);
            systemOutPrintLn("ERROR: " + e.getMessage());
            logResult("<font color=\"#AA0000\"><b>Test failed.</b></font><br>\nMessage: " + util.escapeHTML(e.getMessage()), "<code><small><br><pre>"
                    + stackTrace + "</pre></small></code>");
            throw e;
        } catch (NATestCaseException e) { //we rethrow the Exception as AssertionFailedError
            logEvent("<font color=\"#0000AA\"><b>N/A</b></font><br>\nMessage: " + util.escapeHTML(e.getMessage()));
            systemOutPrintLn("Test is N/A: " + e.getMessage());
        } catch (Throwable e) { //we rethrow the Exception as AssertionFailedError
            systemOutPrintLn("ERROR: " + e.getMessage());
            logResult("<font color=\"#AA0000\"><b>Test failed.</b></font><br>\nMessage: " + util.escapeHTML(e.getMessage()), "<code><small><br><pre>"
                    + getFullStackTrace(e) + "</pre></small></code>");
            throw new AssertionFailedError(e.getMessage());
        }
    }

}
