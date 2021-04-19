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

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.common.Environment;
import com.epam.gepard.common.NATestCaseException;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.logger.helper.LogFileWriterFactory;
import com.epam.gepard.util.FileUtil;
import com.epam.gepard.util.Util;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This reporter generates HML page for the class.
 *
 * @author Taams Kohegyi
 */
public final class HtmlRunReporter extends RunListener {

    private TestClassExecutionData classData;
    private String classDir = "";
    private Environment environment;
    private LogFileWriter testClassHtmlLog; //per test class
    private LogFileWriter testMethodHtmlLog; //per test case
    private final LogFileWriterFactory logFileWriterFactory = new LogFileWriterFactory();
    private int step; //test step, restarts from 1 in every executed test method
    private int divStep = 1;
    private Properties props = new Properties();
    private Boolean testFailed = false;
    private Boolean testNA = false;
    private Boolean testDummy = false;
    private FileUtil fileUtil = new FileUtil();
    private String testNAMessage = "";
    private List<Failure> testFailure;


    /**
     * Set-up the HTML logger.
     *
     * @param classData is the Test class object within Gepard.
     */
    public HtmlRunReporter(final TestClassExecutionData classData) {
        this.classData = classData;
        this.environment = classData.getEnvironment();
        testFailure = new ArrayList<>();
        Class<?> clazz = classData.getTestClass();
        unPackClassNameAndDir(clazz);
        fileUtil.createDirectory(environment.getProperty(Environment.GEPARD_HTML_RESULT_PATH) + "/" + classDir);
        classData.setTestURL(getTestURL());

        if (clazz.isAnnotationPresent(TestClass.class)) {
            classData.setTestScriptId(clazz.getAnnotation(TestClass.class).id());
            classData.setTestScriptName(clazz.getAnnotation(TestClass.class).name());
            classData.setHtmlRunReporter(this);
        }
    }

    /**
     * Called when a Test Case Set = Test Class is started by the executor.
     *
     * @param description specified the just started test class
     * @throws Exception in case of problem
     */
    @Override
    public void testRunStarted(final Description description) throws Exception {
    }

    /**
     * Called when a Test method is started by the executor.
     *
     * @param description specifies the just started test method
     * @throws Exception in case of problem
     */
    @Override
    public void testStarted(final Description description) throws Exception {
        fileUtil.createDirectory(environment.getProperty(Environment.GEPARD_HTML_RESULT_PATH) + "/" + readDirectory());
        String methodName = description.getMethodName();
        testMethodHtmlLog = logFileWriterFactory.createCustomWriter(environment.getProperty(Environment.GEPARD_RESULT_TEMPLATE_PATH) + "/"
                        + "temp_generictestcase.html", environment.getProperty(Environment.GEPARD_HTML_RESULT_PATH) + "/"
                        + readDirectory() + "/" + methodName + classData.getDrivenDataRowNo() + ".html",
                classData.getEnvironment());
        step = 1;
        classData.addSysOut("\nRunning test: " + classData.getClassName() + "." + methodName + "\nName: " + classData.getTestScriptName());
        testFailed = false;
        testNA = false;
        testDummy = false;
        Properties props = new Properties();
        props.setProperty("ID", classData.getTestScriptId());
        props.setProperty("Name", classData.getTestScriptName());
        props.setProperty("TestCase", methodName);
        props.setProperty("ScriptNameRow", getDataDrivenFullClassName());
        testMethodHtmlLog.insertBlock("Header", props);
        initDataDrivenLog();
    }

    /**
     * Called when a Test method is failed.
     *
     * @param failure holds the problem
     * @throws Exception in case of problem
     */
    @Override
    public void testFailure(final Failure failure) throws Exception {
        if (!testNA) {
            if (failure.getException() instanceof NATestCaseException) {
                testNA = true;
                testNAMessage = failure.getMessage();
            } else {
                testFailed = true;
                testFailure.add(failure);
            }
        }
    }

    /**
     * Called when a Test method is ignored/not executed.
     *
     * @param description specifies the just ignored test method
     * @throws Exception in case of problem
     */
    @Override
    public void testIgnored(final Description description) throws Exception {
        AllTestRunner.CONSOLE_LOG.info("testIgnored" + description.getTestClass().getCanonicalName() + " IGNORED");
    }

    /**
     * Called when a Test method execution is finished.
     *
     * @param description specifies the just executed test method
     * @throws Exception in case of problem
     */
    @Override
    public void testFinished(final Description description) throws Exception {
        //After running the test case
        Util u = new Util();
        boolean isDummy = testDummy;
        if (isDummy) {
            classData.increaseCountDummy();
        }
        boolean isNA = testNA;
        String dataDrivenName = getDataDrivenSimpleClassName() + "/" + description.getMethodName() + classData.getDrivenDataRowNo();
        Properties props;
        if (isNA) { //testNA test case
            PropertiesData data = createPropertiesData(isDummy, true);
            props = createProperties(classData, description.getMethodName(), u, data, dataDrivenName);
            logEvent("<font color=\"#0000AA\"><b>N/A</b></font><br>\nMessage: " + testNAMessage);
            systemOutPrintLn("Test is N/A: " + testNAMessage);
            classData.increaseCountNA();
        } else if (testFailed) {  // failed test case
            //The test failed (note that tr contains only ONE failure or error).
            classData.increaseCountFailed();
            String errorMsg = "---No error message---";
            PropertiesData data = createFailurePropertiesData(isDummy, false, errorMsg);
            props = createProperties(classData, description.getMethodName(), u, data, dataDrivenName);
            String stackTrace = testFailure.get(0).getTrace();
            String failureMessage = testFailure.get(0).getMessage();
            systemOutPrintLn("Test failed: " + failureMessage);
            logResult("<font color=\"#AA0000\"><b>Test failed.</b></font><br>\nMessage: " + u.escapeHTML(failureMessage), "<code><small><br><pre>" + u.escapeHTML(stackTrace)
                    + "</pre></small></code>");
            if (testFailure.size() > 1) {
                //multiple failures we have
                for (int i = 1; i < testFailure.size(); i++) {
                    stackTrace = testFailure.get(i).getTrace();
                    failureMessage = testFailure.get(i).getMessage();
                    systemOutPrintLn("Additional failure: " + failureMessage);
                    logResult("<font color=\"#AA0000\"><b>Additional Failure.</b></font><br>\nMessage: " + u.escapeHTML(failureMessage),
                            "<code><small><br><pre>" + u.escapeHTML(stackTrace) + "</pre></small></code>");
                }
            }
        } else { // passed test case
            PropertiesData data = createPropertiesData(isDummy, false);
            props = createProperties(classData, description.getMethodName(), u, data, dataDrivenName);
            classData.increaseCountPassed();
            logEvent("<font color=\"#00AA00\"><b>Test passed.</b></font>");
            systemOutPrintLn("Test passed.");
        }
        testClassHtmlLog.insertBlock("TestRow", props);
        testMethodHtmlLog.insertBlock("Footer", null);
        testMethodHtmlLog.close();
        testMethodHtmlLog = null;
    }

    /**
     * Called when a Test Case Sat = TestClass execution is finished by the executor.
     *
     * @param result gives info on the test class execution
     * @throws Exception in case of problem
     */
    @Override
    public void testRunFinished(final Result result) throws Exception {
        Util u = new Util();
        if (result.getRunCount() == 0) {
            if (result.getFailureCount() > 0) {
                for (Failure failure : result.getFailures()) {
                    String message = failure.getMessage();
                    if (message == null) {
                        message = "";
                    }
                    classData.setItAsProblematic();
                    afterClassLogComment("Failure occurred: " + message + "<br/><br/><pre>" + u.escapeHTML(failure.getTrace()) + "</pre>");
                }
            } else {
                testClassHtmlLog.insertBlock("NoTestCases", null);
            }
        }
    }

    /**
     * This extra method is necessary in order to handle special error cases, like failures in Cucumber.
     *
     * @param problem is the exception.
     */
    public void setTestFailed(final Throwable problem) {
        testFailed = true;
        testFailure.add(new Failure(Description.EMPTY, problem));
    }

    /**
     * Need to call it well before any test class execution is requested.
     */
    public void hiddenBeforeTestClassExecution() {
        testClassHtmlLog = new LogFileWriter(environment.getProperty(Environment.GEPARD_RESULT_TEMPLATE_PATH) + "/" + "temp_generictestsuite.html",
                environment.getProperty(Environment.GEPARD_HTML_RESULT_PATH) + "/" + classDir + "/" + getDataDrivenSimpleClassName() + ".html", environment);
        props.setProperty("ID", classData.getTestScriptId());
        props.setProperty("Name", classData.getTestScriptName());
        testClassHtmlLog.insertBlock("Header", props);
        testClassHtmlLog.insertBlock("TableHead", props);
    }

    /**
     * Need to call it after test class execution finished.
     */
    public void hiddenAfterTestClassExecution() {
        testClassHtmlLog.insertBlock("TableEnd", props);
        testClassHtmlLog.insertBlock("Footer", null);
    }

    /**
     * Can be used in methods annotated with @BeforeClass annotation.
     * @param comment that should be logged.
     */
    public void beforeClassLogComment(final String comment) {
        props.setProperty("BeforeAfterClassMessage", comment);
        testClassHtmlLog.insertBlock("BeforeAfterClassMessage", props);
    }

    /**
     * Can be used in methods annotated with @AfterClass annotation.
     * @param comment that should be logged.
     */
    public void afterClassLogComment(final String comment) {
        beforeClassLogComment(comment);
    }

    private void initDataDrivenLog() {
        Util util = new Util();
        if ((classData.getDrivenData() != null) && (classData.getDrivenData().getParameters() != null)) {
            //we have parameters to be logged, so build the text
            String s = "<table border=\"1\"><tr><td><b>Parameter Name</b></td><td><b>Parameter Value</b></td></tr>";
            for (int i = 0; i < classData.getDrivenData().getParameters().length; i++) {
                String name = classData.getDrivenData().getParameterName(i);
                String parValue = util.escapeHTML(classData.getDrivenData().getTestParameter(name));
                parValue = parValue.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
                s = s + "<tr><td>" + util.escapeHTML(name) + "</td><td>" + parValue + "</td></tr>";
            }
            s = s + "</table>";
            logComment("Data Driven Test, with " + classData.getDrivenData().getParameters().length
                    + " parameters. Data row:" + classData.getDrivenDataRowNo(), s);
        }
    }

    /**
     * Writes a message to the future console stream. Can be used for parallel exec.
     *
     * @param message is the string to be printed out
     */
    public void systemOutPrintLn(final String message) {
        classData.addSysOut(message);
    }

    private Properties createProperties(final TestClassExecutionData testData, final String methodName, final Util u, final PropertiesData data, final String dataDrivenName) {
        Properties props = new Properties();
        props.setProperty("TestCase", methodName);
        props.setProperty("TestResult", data.getTestResult());
        props.setProperty("ErrorMsg", u.escapeHTML(data.getErrorMsg()));
        props.setProperty("TestResultColor", data.getExtColor());
        props.setProperty("DummyText", u.escapeHTML(data.getExtText()));
        props.setProperty("TCURL", dataDrivenName + ".html");
        props.setProperty("TCMethod", testData.getClassName() + "." + methodName + "()");
        return props;
    }

    private PropertiesData createPropertiesData(final boolean isDummy, final boolean isNA) {
        String extColor = determineExtColor(isDummy, isNA);
        String testResult = determineTestResult(isNA);
        String extText = determineExtText(isDummy);
        String errorMsg = "";
        return new PropertiesData(extText, extColor, testResult, errorMsg);
    }

    private PropertiesData createFailurePropertiesData(final boolean isDummy, final boolean isNA, final String errorMsg) {
        String finalErrorMessage = errorMsg;
        if (finalErrorMessage == null) {
            finalErrorMessage = "---No error message---";
        }
        String extColor = determineFailureExtColor(isDummy, isNA);
        String extText = determineFailureExtText(isDummy, isNA);
        String testResult = "Failed";
        return new PropertiesData(extText, extColor, testResult, finalErrorMessage);
    }

    /**
     * Converts class name to directory path.
     *
     * @return directory path
     */
    public String readDirectory() {
        String pname = classData.getClassName();
        String[] st = pname.split("\\.");
        String name = "";
        if ((st[0] != null) && !("test".equals(st[0]))) {
            name = name + st[0] + "/";
        }
        int i = 1;
        while (i < st.length) {
            name = name + st[i];
            if (i < st.length - 1) {
                name = name + "/";
            } else {
                name = name + classData.getDrivenDataRowNo() + "/";
            }
            i++;
        }
        return name;
    }

    private String determineExtText(final boolean isDummy) {
        String extText = "";
        if (isDummy) {
            extText = "(Dummy test)";
        }
        return extText;
    }

    private String determineTestResult(final boolean isNA) {
        String result;
        if (isNA) {
            result = "N/A";
        } else {
            result = "Passed";
        }
        return result;
    }

    private String determineExtColor(final boolean isDummy, final boolean isNA) {
        String extColor = "#00AA00";
        if (isNA) {
            extColor = "#0000AA";
        }
        if (isDummy) {
            extColor = "#999999";
        }
        return extColor;
    }

    private String determineFailureExtText(final boolean isDummy, final boolean isNA) {
        String extText = "";
        if (isNA) {
            extText = "N/A";
        }
        if (isDummy) {
            extText = "(Dummy test)";
        }
        return extText;
    }

    private String determineFailureExtColor(final boolean isDummy, final boolean isNA) {
        String extColor = "#AA0000";
        if (isNA) {
            extColor = "#AA0000";
        }
        if (isDummy) {
            extColor = "#999999";
        }
        return extColor;
    }

    /**
     * Makes a test method as Not Applicable (N/A).
     * @param reason is the reason why it is N/A.
     */
    public void naTestCase(String reason) {
        testNA = true;
        String comment = "This test case is N/A";
        if (reason != null) {
            comment = reason;
        }
        testNAMessage = comment;
        throw new NATestCaseException(comment);
    }

    /**
     * Makes a test method as Dummy.
     */
    public void dummyTestCase() {
        testDummy = true;
        logComment("This is a dummy test case");
    }

    public LogFileWriter getTestMethodHtmlLog() {
        return testMethodHtmlLog;
    }

    private class PropertiesData {
        private final String extText;
        private final String extColor;
        private final String testResult;
        private final String errorMsg;

        public PropertiesData(final String extText, final String extColor, final String testResult, final String errorMsg) {
            this.extText = extText;
            this.extColor = extColor;
            this.testResult = testResult;
            this.errorMsg = errorMsg;
        }

        public String getExtText() {
            return extText;
        }

        public String getExtColor() {
            return extColor;
        }

        public String getTestResult() {
            return testResult;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

    }

    /**
     * Builds up the so called Data Driven ClassName, which is used at report generation.
     * In case the test is data driven (and therefore called several times), to ensure that the result shows all the runs individually,
     * the ClassName that is used to build up the result path should be driven by the number of the data rows currently used.
     *
     * @return the Data driven class name, i.e. returns with the classname + the actual data row.
     */
    private String getDataDrivenFullClassName() {
        return classData.getClassName() + "/" + classData.getDrivenDataRowNo();
    }

    /**
     * Builds up the so called Data Driven ClassName, which is used at report generation.
     * In case the test is data driven (and therefore called several times), to ensure that the result shows all the runs individually,
     * the ClassName that is used to build up the result path should be driven by the number of the data rows currently used.
     *
     * @return the Data driven class name, i.e. returns with the classname + the actual data row.
     */
    private String getDataDrivenSimpleClassName() {
        return classData.getTestClass().getSimpleName() + classData.getDrivenDataRowNo();
    }

    /**
     * Sets classDir attribute to the proper path of the given class.
     *
     * @param theClass Class to use
     */
    private void unPackClassNameAndDir(final Class<?> theClass) {
        String className = "";
        String pname = theClass.getName();

        String[] st = pname.split("\\.");

        if (st[0] != null) {
            className = st[0];
        }
        int i = 1;
        while (i < st.length) {
            if (className.length() > 0) {
                classDir = classDir + className + "/";
            }
            className = st[i];
            i++;
        }
        if (classDir.endsWith("/")) {
            classDir = classDir.substring(0, classDir.length() - 1);
        }

    }

    /**
     * Returns test HTML log file path.
     *
     * @return path
     */
    public String getTestURL() {
        String dataDrivenName = getDataDrivenSimpleClassName();
        return classDir + ("".equals(classDir) ? "" : "/") + dataDrivenName + ".html";
    }

    /**
     * Re-formats a path so that it contains only forward slashes,
     * and also removes double slashes.
     *
     * @param pathName is the input path to be formatted correctly.
     * @return with the formatted path.
     */
    public String formatPathName(final String pathName) {
        String inPathName = pathName.replace('\\', '/');
        int index = 0;
        int pos;
        while ((pos = inPathName.indexOf("//", index)) != -1) {
            inPathName = inPathName.substring(0, pos) + "/" + inPathName.substring(pos + 2);
            index = pos;
        }
        return inPathName;
    }

    /**
     * Write a test step message to the log, and increase the step number.
     *
     * @param comment Comment message
     */
    public void logStep(final String comment) {
        String consoleComment = comment.replace('\uFF5F', '(').replace('\uFF60', ')'); //Unicode to Console (partial transfer)
        systemOutPrintLn(step + ". " + consoleComment);
        if (testMethodHtmlLog != null) {
            testMethodHtmlLog.insertText("<tr><td align=\"center\">&nbsp;&nbsp;" + step + ".&nbsp;&nbsp;</td><td bgcolor=\"#E0E0F0\">" + comment
                    + "</td></tr>\n");
        }
        step++;
    }

    /**
     * Write a warning message to the log, without the step number.
     *
     * @param warning Comment message
     */
    public void logWarning(final String warning) {
        systemOutPrintLn("WARNING:" + warning);
        if (testMethodHtmlLog != null) {
            testMethodHtmlLog.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0D0D0\">" + warning + "</td></tr>");
        }
    }

    /**
     * Write a comment message to the log, without the step number, but with a description.
     * Can be used to dump stack trace for example.
     *
     * @param comment     Comment message
     * @param description is a multi-row string description for the comment.
     */
    public void logComment(final String comment, final String description) {
        systemOutPrintLn(comment);

        String addStr = " <small>[<a href=\"javascript:showhide('div_" + getDivStep() + "');\">details</a>]</small>";
        if (testMethodHtmlLog != null) {
            testMethodHtmlLog.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0F0E0\">" + comment + addStr + "<div id=\"div_" + getDivStep()
                    + "\" style=\"display:none\"><br>\n" + description + "</div></td></tr>\n");
        }
        increaseDivStep();
    }

    /**
     * Write a comment message to the log, to the html output (different text can be used), without the step number, but with a description.
     *
     * @param comment     Comment message, to put to console
     * @param htmlComment same as the Comment message, but HTML formatted text
     * @param description is a multi-row string description for the comment.
     */
    public void logComment(final String comment, final String htmlComment, final String description) {
        systemOutPrintLn(comment);

        String addStr = " <small>[<a href=\"javascript:showhide('div_" + getDivStep() + "');\">details</a>]</small>";
        if (testMethodHtmlLog != null) {
            testMethodHtmlLog.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0F0E0\">" + htmlComment + addStr + "<div id=\"div_" + getDivStep()
                    + "\" style=\"display:none\"><br>\n" + description + "</div></td></tr>\n");
        }
        increaseDivStep();
    }

    /**
     * Write a comment message to the log, without the step number.
     *
     * @param comment Comment message
     */
    public void logComment(final String comment) {
        systemOutPrintLn(comment);
        if (testMethodHtmlLog != null) {
            testMethodHtmlLog.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0F0E0\">" + comment + "</td></tr>");
        }
    }

    /**
     * Write an event message to the log.
     *
     * @param text Event message
     */
    public void logEvent(final String text) {
        if (!text.startsWith("<font")) {
            systemOutPrintLn(text);
        }
        if (testMethodHtmlLog != null) {
            testMethodHtmlLog.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0F0F0\">" + text + "</td></tr>\n");
        }
    }

    /**
     * Write an event message to the log.
     *
     * @param text        Event message
     * @param description Event description/info
     */
    public void logResult(final String text, final String description) {
        if (testMethodHtmlLog != null) {
            String addStr = " <small>[<a href=\"javascript:showhide('div_" + getDivStep() + "');\">details</a>]</small>";
            testMethodHtmlLog.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0F0F0\">" + text + addStr + "<div id=\"div_" + getDivStep()
                    + "\" style=\"display:none\"><br>\n" + description + "</div></td></tr>\n");
        }
        increaseDivStep();
    }

    /**
     * Put stack trace into the log.
     *
     * @param comment Comment message
     * @param t       is the exception object
     */
    public void logStackTrace(final String comment, final Throwable t) {
        Util u = new Util();
        String trace = u.escapeHTML(Util.getStackTrace(t));
        String description = "<code><small><br><pre>" + trace + "</pre></small></code>";
        logComment(comment + " (dump stack trace)", description);
    }

    /**
     * Get step information for div tags in html log.
     * @return with the actual div step.
     */
    public int getDivStep() {
        return divStep;
    }

    /**
     * Increase the div step counter.
     */
    public void increaseDivStep() {
        divStep++;
    }

    /**
     * Get test case step information in html log.
     * @return with the actual div step.
     */
    public int getStep() {
        return step;
    }

    /**
     * Increase the test case step counter.
     */
    public void increaseStep() {
        step++;
    }
}
