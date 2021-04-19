package com.epam.gepard.common;

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

import com.epam.gepard.logger.HtmlRunReporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.datadriven.DataDrivenParameters;
import com.epam.gepard.datadriven.DataFeederLoader;

/**
 * This class holds Test Class execution information.
 * The purpose is:
 * - timeout value for the class
 * - TS ID
 * - TS Name
 * - collect passed/failed/notapplicable information
 * - locking information for the class
 * - group blocking information for the class
 *
 * @author Tamas_Kohegyi
 */
public final class TestClassExecutionData {

    @SuppressWarnings("rawtypes")
    private static final Logger LOGGER = LoggerFactory.getLogger(TestClassExecutionData.class);

    private String testURL;

    private int countPassed;
    private int countFailed;
    private int countNA;
    private int countDummy;
    private boolean isProblematic;
    private int countOfRuns;
    /**
     * To store the data driven parameters for this test class run.
     */
    private DataDrivenParameters drivenData;
    /**
     * Stores the test class name. May be used for BLOCKERSTRING* approach.
     */
    private String className;
    private int drivenDataRowNo;
    private DataFeederLoader dataFeederLoader;

    private int lock; //Thread lock handling. 0 = not locked, waiting for lock.

    private final String id;
    /**
     * Store the Blocker Test Group info.
     */
    private String blockerString;
    private boolean selfEnabledBlocker;

    /**
     * The test name that will appear in the log.
     */
    private String testScriptName = "Unnamed test";
    /**
     * The TS ID that will appear in the log.
     */
    private String testScriptId = "0.000";

    private StringBuilder systemOut;
    private Class<?> testClass;

    private String originalLine;
    private Environment environment;
    private HtmlRunReporter htmlRunReporter;

    /**
     * Test execution data for a Test Class (inherited TestCase class).
     * @param id is the Test Class identifier (test class + rownumber)
     * @param environment holds the properties of the application
     */
    public TestClassExecutionData(final String id, final Environment environment) {
        this.environment = environment;
        systemOut = new StringBuilder();
        this.id = id;
        isProblematic = false;
    }

    public Class getTestClass() {
        return testClass;
    }

    public void setTestClass(final Class<?> testClass) {
        this.testClass = testClass;
    }

    public String getID() {
        return id;
    }

    public DataFeederLoader getDataFeederLoader() {
        return dataFeederLoader;
    }

    /**
     * Set the class as problematic (probably issue in @BeforeClass or @AfterClass methods.
     */
    public void setItAsProblematic() {
        isProblematic = true;
    }

    /**
     * Get info if the test class execution was problematic or not.
     * @return with the boolean info
     */
    public boolean isProblematic() {
        return isProblematic;
    }

    // THREAD LOCK HANDLING

    /**
     * Store the locking info.
     * 0 - not locked.
     * >0 locking or locked.
     * -1 test class is executed.
     * @return with the value of the locking info.
     */
    public int getLock() {
        return lock;
    }

    /**
     * Locks the test class for execution.
     */
    public void lock() {
        lock = 1;
    }

    /**
     * Release the lock by noting that it is executed already.
     */
    public void setLockExecuted() {
        lock = -1;
    }

    public String getBlockerString() {
        return blockerString;
    }

    public void setBlockerString(final String blockerString) {
        this.blockerString = blockerString;
    }

    public void setSelfEnabledBlocker(final boolean selfEnabledBlocker) {
        this.selfEnabledBlocker = selfEnabledBlocker;
    }

    public boolean isSelfEnabledBlocker() {
        return selfEnabledBlocker;
    }

    /**
     * The test name that will appear in the log.
     * @return with the annotated name of the test class.
     */
    public String getTestScriptName() {
        return testScriptName;
    }

    /**
     * Sets the test class name from annotation.
     * @param testScriptName is the given name.
     */
    public void setTestScriptName(final String testScriptName) {
        this.testScriptName = testScriptName;
    }

    /**
     * The TS ID that will appear in the log.
     * @return with the annotated ID value.
     */
    public String getTestScriptId() {
        return testScriptId;
    }

    public void setTestScriptId(final String testScriptId) {
        this.testScriptId = testScriptId;
    }

    public int getRunned() {
        return countPassed + countFailed + countNA;
    }

    /**
     * Replacement of the direct Console log method call.
     * Calls Console log if only single thread is used, otherwise put it into the string buffer,
     * which will be flushed later by the executor.
     *
     * @param message is the message part that should be stored.
     */
    public void addSysOut(final String message) {
        LOGGER.debug(message);
        synchronized (systemOut) {
            systemOut.append(message).append("\n");
        }
    }

    /**
     * Get what is actually stored in the systemOut buffer.
     * This reading clears the buffer.
     * @return with the content of the systemOut buffer.
     */
    public String getSysOut() {
        String s = "";
        synchronized (systemOut) {
            if (systemOut.length() > 0) {
                s = systemOut.toString();
                systemOut = new StringBuilder(); //clear the buffer
            }
        }
        return s;
    }

    /**
     * To store the data driven parameters for this test class run.
     * @param parameterObject is the object holds data driven parameters.
     */
    public void setParameters(final DataDrivenParameters parameterObject) {
        drivenData = parameterObject;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(final String name) {
        className = name;
    }

    public void setDataFeederLoader(final DataFeederLoader dataFeederLoader) {
        this.dataFeederLoader = dataFeederLoader;
    }

    /**
     * Load data driven parameters for the specific run (row) of the test class.
     */
    public void loadParameters() {
        if (dataFeederLoader != null) {
            setParameters(dataFeederLoader.getParameterRow(className, drivenDataRowNo));
        } else {
            AllTestRunner.CONSOLE_LOG.info("Loaded: " + className + ", Non-Data-driven test");
        }
    }

    public void setDataRow(final int rowNo) {
        drivenDataRowNo = rowNo;
    }

    public void setOriginalLine(final String originalLine) {
        this.originalLine = originalLine;
    }

    public String getOriginalLine() {
        return originalLine;
    }

    public int getDrivenDataRowNo() {
        return drivenDataRowNo;
    }

    public void setDrivenDataRowNo(final int drivenDataRowNo) {
        this.drivenDataRowNo = drivenDataRowNo;
    }

    public DataDrivenParameters getDrivenData() {
        return drivenData;
    }

    public void setDrivenData(final DataDrivenParameters drivenData) {
        this.drivenData = drivenData;
    }

    public int getCountDummy() {
        return countDummy;
    }

    public int getCountPassed() {
        return countPassed;
    }

    public int getCountFailed() {
        return countFailed;
    }

    public int getCountNA() {
        return countNA;
    }

    /**
     * Increase the counter of dummy TCs by one.
     */
    public void increaseCountDummy() {
        countDummy++;
    }

    /**
     * Increase the number of NA test cases within this test class by one.
     */
    public void increaseCountNA() {
        countNA++;
    }

    /**
     * Increase the number of Passed test cases within this test class by one.
     */
    public void increaseCountPassed() {
        countPassed++;
    }

    /**
     * Increase the number of Failed test cases within this test class by one.
     */
    public void increaseCountFailed() {
        countFailed++;
    }

    public String getTestURL() {
        return testURL;
    }

    public void setTestURL(final String testURL) {
        this.testURL = testURL;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setHtmlRunReporter(HtmlRunReporter htmlRunReporter) {
        this.htmlRunReporter = htmlRunReporter;
    }

    public HtmlRunReporter getHtmlRunReporter() {
        return htmlRunReporter;
    }

    public int getCountOfRuns() {
        return countOfRuns;
    }

    public void setCountOfRuns(int countOfRuns) {
        this.countOfRuns = countOfRuns;
    }
}
