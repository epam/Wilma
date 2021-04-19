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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.GepardConstants;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.helper.AllTestResults;
import com.epam.gepard.logger.LogFileWriter;

/**
 * Collects the results of all tests.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class ResultCollector {
    private static final Logger CONSOLE_LOG = LoggerFactory.getLogger("console");

    private boolean odd;

    /**
     * Goes through the current test list and waits till each test finished then collects the results to the given {@link AllTestResults} object.
     * @param allTestResults is the holder of test results.
     * @param htmlLog is writer of html log file.
     * @param csvLog is writer of csv log file.
     * @throws InterruptedException because of the Thread.sleep()
     */
    public void waitForExecutionEndAndCollectResults(final AllTestResults allTestResults, final LogFileWriter htmlLog, final LogFileWriter csvLog)
        throws InterruptedException {
        odd = false; //a marker to detect odd and even rows in the html test result
        for (String s1 : GenericListTestSuite.getTestClassIds()) {
            TestClassExecutionData d = GenericListTestSuite.getTestClassExecutionData(s1);
            boolean needToWait = true;
            while (needToWait) {
                needToWait = hasToWait(allTestResults, d, htmlLog, csvLog);
            }
        }
    }

    private boolean hasToWait(final AllTestResults allTestResults, final TestClassExecutionData executionData, final LogFileWriter htmlLog,
            final LogFileWriter csvLog) throws InterruptedException {
        boolean result = true;
        if (executionData.getLock() >= 0) { //need wait
            String s = executionData.getSysOut();
            if (s.length() > 0) { // if there is a meaning to write it out...
                CONSOLE_LOG.info(s);
            }
            Thread.sleep(GepardConstants.ONE_SECOND_LENGTH.getConstant()); //wait for a sec
        } else { //lock is -1, Test Class is ready, flush the result
            flushTestResult(executionData, odd, htmlLog, csvLog);
            //collect suite level results
            allTestResults.increaseRunned(executionData.getRunned());
            allTestResults.increasePassed(executionData.getCountPassed());
            allTestResults.increaseFailed(executionData.getCountFailed());
            allTestResults.increaseNotApplicable(executionData.getCountNA());
            allTestResults.increaseDummy(executionData.getCountDummy());
            //finalize the row
            odd = !odd; //change the odd/even html output marker
            result = false;
        }
        return result;
    }

    private void flushTestResult(final TestClassExecutionData d, final boolean odd, final LogFileWriter htmlLog, final LogFileWriter csvLog) {
        String s = d.getSysOut();
        CONSOLE_LOG.info(s);
        //write the Test Class result row
        Properties props2 = new Properties();
        props2.setProperty("Number", d.getTestScriptId().replace(',', ';').replace('{', '(').replace('}', ')'));
        props2.setProperty("Name", d.getTestScriptName().replace(',', ';').replace('{', '(').replace('}', ')'));
        props2.setProperty("URL", d.getTestURL());
        props2.setProperty("Passed", String.valueOf(d.getCountPassed()));
        props2.setProperty("Failed", String.valueOf(d.getCountFailed()));
        props2.setProperty("Implemented", String.valueOf(d.getRunned()));
        props2.setProperty("RealPerDummy", String.valueOf(d.getRunned() - d.getCountDummy()) + "/" + String.valueOf(d.getCountDummy()));
        props2.setProperty("NotApplicable", String.valueOf(d.getCountNA()));
        props2.setProperty("Color", (d.getRunned() == 0) ? "#C0C0C0" : (d.getCountDummy() > 0 ? "#707070" : "#000000"));
        props2.setProperty("ResultColor", (d.getCountFailed() > 0) ? "#F0D0D0" : ((d.getCountNA() > 0) ? "#D0D0F0" : "#D0F0D0"));
        if (d.isProblematic()) {
            props2.setProperty("ResultColor", "#F0F0C0");
        }
        props2.setProperty("RowColor", odd ? "#F0F0F0" : "#E4E4E4");
        htmlLog.insertBlock("TestRow", props2);
        csvLog.insertBlock("TestRow", props2);
        //redefine RealPerDummy as xml does not allow '&nbsp;'
        props2.setProperty("RealPerDummy", String.valueOf(d.getRunned() - d.getCountDummy()) + "/" + String.valueOf(d.getCountDummy()));
    }
}
