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

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import com.epam.gepard.helper.DateHelper;
import junit.textui.TestRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.GepardConstants;
import com.epam.gepard.common.helper.ConsoleWriter;
import com.epam.gepard.common.helper.ReportFinalizer;
import com.epam.gepard.common.helper.ResultCollector;
import com.epam.gepard.common.helper.TestFailureReporter;
import com.epam.gepard.common.threads.ExecutorThreadManager;
import com.epam.gepard.common.threads.RemoteControlHandlerThread;
import com.epam.gepard.common.threads.handler.RemoteControlHandler;
import com.epam.gepard.common.threads.helper.ServerSocketFactory;
import com.epam.gepard.exception.ComplexGepardException;
import com.epam.gepard.exception.ShutDownException;
import com.epam.gepard.filter.ExpressionTestFilter;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.helper.AllTestResults;
import com.epam.gepard.logger.LogFileWriter;
import com.epam.gepard.logger.LogFinalizer;
import com.epam.gepard.logger.LogFolderCreator;
import com.epam.gepard.logger.helper.LogFileWriterFactory;
import com.epam.gepard.util.ExitCode;

/**
 * Runs all the functional tests associated with the project. This is the
 * main class.
 * <p/>
 * The functional test environment had two aims in its early 1.0 version:
 * 1) To be JUnit compatible
 * 2) To support HTML logging
 * <p/>
 * To reach both goals several methods of the JUnit environment
 * had to be overriden. These classes are named as Generic*.java.
 * <p/>
 * <p>Title: AllTestRunner</p>
 * <p>Description: Runs the functional tests recursively</p>
 * <p>Copyright: Copyright (c) EPAM Systems 2002-2015</p>
 * <p>Company: EPAM Systems </p>
 *
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs, Adam_Csaba_Kiraly
 */
public class AllTestRunner extends TestRunner {

    public static final Logger CONSOLE_LOG = LoggerFactory.getLogger("console");
    private static final Logger LOG = LoggerFactory.getLogger(AllTestRunner.class);

    /**
     * Program exit code. See @class ExitCode for the predefined values.
     */
    private static int exitCode; //java exit code

    private static String systemUnderTestVersion;

    private static RemoteControlHandlerThread gepardRemote;

    private static ExecutorThreadManager executorThreadManager = new ExecutorThreadManager();

    private final RemoteControlHandler remoteControlHandler = new RemoteControlHandler();

    private ReportFinalizer reportFinalizer;
    private final LogFinalizer logFinalizer = new LogFinalizer();
    private final ResultCollector resultCollector = new ResultCollector();
    private TestFailureReporter failureReporter;
    private LogFolderCreator logFolderCreator;
    private final LogFileWriterFactory logFileWriterFactory = new LogFileWriterFactory();
    private final Environment environment;

    /**
     * Constructs a new instance of {@link AllTestRunner}.
     * @param environment the environment which will hold the properties
     * @param reportFinalizer used for finalizing the information
     * @param failureReporter used for generating a file report in case of test failure
     * @param logFolderCreator handles the creation and deletion of the log folders
     */
    public AllTestRunner(final Environment environment, final ReportFinalizer reportFinalizer, final TestFailureReporter failureReporter,
            final LogFolderCreator logFolderCreator) {
        this.environment = environment;
        this.reportFinalizer = reportFinalizer;
        this.failureReporter = failureReporter;
        this.logFolderCreator = logFolderCreator;
    }

    /**
     * Sets the Application under test version to be reported in the logs.
     *
     * @param verString is the version string (build number).
     */
    public static void setSystemUnderTestVersion(final String verString) {
        systemUnderTestVersion = verString;
    }

    /**
     * Takes two command line parameters. The first one is the path holding
     * the implemented test cases. The second one is the path to the property
     * file.
     * @param args Command line parameters.
     */
    public static void main(final String[] args) {
        Thread.currentThread().setName("GEPARD Main");
        Environment environment = new Environment();
        ReportFinalizer reportFinalizer = new ReportFinalizer(environment);
        TestFailureReporter failureReporter = new TestFailureReporter(environment);
        LogFolderCreator logFolderCreator = new LogFolderCreator(environment);
        AllTestRunner runner = new AllTestRunner(environment, reportFinalizer, failureReporter, logFolderCreator);
        runner.execute(args);
    }

    private void execute(final String[] args) {
        ConsoleWriter consoleWriter = new ConsoleWriter(environment);
        String propFileList;
        String testListFile = null;
        try {
            consoleWriter.printApplicationInfoBlock();
            if ((args.length < 1) || "/?".equals(args[0]) || "--help".equalsIgnoreCase(args[0]) || "help".equalsIgnoreCase(args[0])) {
                consoleWriter.printStartFailureBlock();
                exitFromGepard(ExitCode.EXIT_CODE_WRONG_NUMBER_OF_PARAMETERS_OR_HELP_REQUEST);
            }

            propFileList = args[0];
            initProperties(environment, propFileList);

            consoleWriter.printParameterInfoBlock(propFileList);
            testListFile = environment.getProperty(Environment.GEPARD_TESTLIST_FILE);

            //initiate the remote control if requested
            if (environment.getBooleanProperty(Environment.GEPARD_REMOTE_ENABLED)) {
                int gepardPort = Integer.parseInt(environment.getProperty(Environment.GEPARD_REMOTE_PORT));
                AllTestRunner.gepardRemote = new RemoteControlHandlerThread(getRemoteControlHandler(), new ServerSocketFactory(), gepardPort);
                AllTestRunner.gepardRemote.setName("GEPARD Remote Control"); //set its name
                AllTestRunner.gepardRemote.start(); //start remote control
            }

            //if full remote control, then wait for the kill sign
            if (environment.getBooleanProperty(Environment.GEPARD_REMOTE_FULL_CONTROL)) {
                initiateGepardRemoteControl();
            }
            //not remote control driven, so run the tests
        } catch (Throwable exc) {
            exitFromGepardWithCriticalException("\n--- GEPARD EXCEPTION, PLEASE CHECK THE CONFIGURATION! ---", exc, true,
                    ExitCode.EXIT_CODE_BAD_SETUP);
        }
        try {
            runAll(testListFile);
        } catch (ShutDownException e) {
            exitFromGepard(e.getExitCode());
        } catch (ComplexGepardException e) {
            exitFromGepardWithCriticalException(e.getMessage(), e.getCause(), e.isShouldExit(), e.getExitCode());
        } catch (Throwable exc) {
            exitFromGepardWithCriticalException("\n--- GEPARD EXCEPTION, PLEASE REPORT IT TO THE MAINTAINERS! ---", exc, true,
                    ExitCode.EXIT_CODE_UNKNOWN_ERROR);
        }
        exitFromGepard();
    }

    private void initProperties(final Environment environment, final String propFileList) {
        if (!environment.setUp(propFileList)) {
            exitFromGepard(ExitCode.EXIT_CODE_WRONG_PROPERTY_FILE);
        }
    }

    private static void initiateGepardRemoteControl() {
        CONSOLE_LOG.info("Using GEPARD Remote Control on port: " + Environment.GEPARD_REMOTE_PORT);
        /* PLEASE NOTE THAT THIS PART IS PARTIALLY IMPLEMENTED, DON'T USE NOW */
        //noinspection InfiniteLoopStatement
        while (true) { //total remote control driven
            try {
                Thread.sleep(GepardConstants.ONE_SECOND_LENGTH.getConstant()); //sleep for a sec, then restart the loop
            } catch (InterruptedException e) {
                //this was not expected, but if happens, then time to exit
                LOG.debug("Thread: MAIN is exiting, as got InterruptedException!");
            }
        }
    }

    /**
     * Setter for the exit code.
     *
     * @param exitCode should have the exit code value.
     */
    public static void setExitCode(final int exitCode) {
        AllTestRunner.exitCode = exitCode;
    }

    public static RemoteControlHandlerThread getGepardRemote() {
        return gepardRemote;
    }

    /**
     * Collects the test classes and runs the tests.
     *
     * @param testListFile is the config file of tests.
     * @throws Exception in case of tc failure
     */
    void runAll(final String testListFile) throws Exception {
        logFolderCreator.prepareOutputFolders();
        //---------------
        GenericListTestSuite gSuite = tryToCreateTestSuiteList(testListFile);

        handleTestListLoadTestCase();
        systemUnderTestVersion = environment.getProperty(Environment.SYSTEM_UNDER_TEST_VERSION);

        //variable for multi thread results
        AllTestResults allTestResults = new AllTestResults();

        //now remember when we started the test
        long startTime = System.currentTimeMillis();
        //take care about the threads
        initiateAndStartExecutorThreads();

        //now the test is running, we have nothing else to do just prepare the summary result, first the header
        Properties props = new Properties();
        Calendar cal = Calendar.getInstance();
        DateHelper dateHelper = new DateHelper();
        props.setProperty("Date", dateHelper.getShortStringFromDate(cal));
        //set up Loggers
        LogFileWriter htmlLog = logFileWriterFactory.createSpecificLogWriter("index.html", "html", Environment.GEPARD_HTML_RESULT_PATH, environment);
        LogFileWriter csvLog = logFileWriterFactory.createSpecificLogWriter("results.csv", "csv", Environment.GEPARD_CSV_RESULT_PATH, environment);
        LogFileWriter quickLog = logFileWriterFactory.createSpecificLogWriter("results.plain", "plain", Environment.GEPARD_RESULT_PATH, environment);
        prepareHeaders(props, htmlLog, csvLog, quickLog);

        resultCollector.waitForExecutionEndAndCollectResults(allTestResults, htmlLog, csvLog);
        //Test Execution is ended
        long endTime = System.currentTimeMillis();

        //close threads, if any
        executorThreadManager.closeRunningThreads();
        //After running the tests, finalize the report
        reportFinalizer.finalizeTheReport(gSuite, allTestResults, systemUnderTestVersion, endTime - startTime, props);

        logFinalizer.finalizeLogs(props, htmlLog, csvLog, quickLog, executorThreadManager.getThreadCount());
        CONSOLE_LOG.info("\n");

        failureReporter.generateTestlistFailure(); // generate the testlist-failure.txt file to help re-execution
        CONSOLE_LOG.info("Gepard Test Done.");
    }

    private void initiateAndStartExecutorThreads() {
        String threads = environment.getProperty(Environment.GEPARD_THREADS);
        String xmlResultPath = environment.getProperty(Environment.GEPARD_XML_RESULT_PATH);
        executorThreadManager.initiateAndStartExecutorThreads(threads, xmlResultPath);
    }

    private void prepareHeaders(final Properties props, final LogFileWriter htmlLog, final LogFileWriter csvLog, final LogFileWriter quickLog) {
        htmlLog.insertBlock("Header", props);
        csvLog.insertBlock("Header", props);
        quickLog.insertBlock("Header", props);
    }

    private void handleTestListLoadTestCase() {
        if (environment.getBooleanProperty(Environment.GEPARD_LOAD_AND_EXIT)) {
            CONSOLE_LOG.info("\nLoad and Exit is requested...");
            throw new ShutDownException(ExitCode.EXIT_CODE_OK);
        }
    }

    private GenericListTestSuite tryToCreateTestSuiteList(final String testListFile) throws IOException, ClassNotFoundException {
        String filterClass = environment.getProperty(Environment.GEPARD_FILTER_CLASS);
        String filterExpr = environment.getProperty(Environment.GEPARD_FILTER_EXPRESSION);
        ExpressionTestFilter filter = tryToCreateTestFilter(filterClass, filterExpr);
        GenericListTestSuite gSuite;
        if (testListFile != null) {
            gSuite = new GenericListTestSuite(testListFile, filter, environment);
        } else {
            CONSOLE_LOG.info("No test list file is available.");
            throw new ShutDownException(ExitCode.EXIT_CODE_BAD_SETUP);
        }
        CONSOLE_LOG.info("\n");
        return gSuite;
    }

    private ExpressionTestFilter tryToCreateTestFilter(final String filterClass, final String filterExpr) {
        ExpressionTestFilter filter = null;
        try {
            Class filterClazz = Class.forName(filterClass);
            filter = (ExpressionTestFilter) (filterClazz.newInstance());
            filter.setFilterExpression(filterExpr);
        } catch (Exception e) {
            exitFromGepardWithCriticalException("\nCould not create test filter '" + filterClass + "'", e, true, ExitCode.EXIT_CODE_BAD_SETUP);
        }
        return filter;
    }

    /*================================================================================================================================*/

    /**
     * Exit from Gepard with exitCode set in main AllTestRunner class.
     */
    public static void exitFromGepard() {
        exitFromGepard(exitCode);
    }

    /**
     * Exit from Gepard.
     *
     * @param exitCode - exit happens with the this exit code.
     */
    public static void exitFromGepard(final int exitCode) {
        //CHECKSTYLE.OFF
        System.exit(exitCode);
        //CHECKSTYLE.ON
    }

    /**
     * Exit from Gepard, but write Stack Trace first.
     * In case it is not really necessary to exit (or rather, should not exit), set shouldExit parameter to false.
     *
     * @param introText  is the first message to be printed, before the stack trace text.
     * @param th         the source os the stack-trace.
     * @param shouldExit will exit Gepard if it is true, otherwise not.
     * @param exitCode   - exit happens with the this exit code.
     */
    public static void exitFromGepardWithCriticalException(final String introText, final Throwable th, final boolean shouldExit, final int exitCode) {
        CONSOLE_LOG.info(introText, th);
        if (shouldExit) {
            exitFromGepard(exitCode);
        }
    }

    public RemoteControlHandler getRemoteControlHandler() {
        return remoteControlHandler;
    }

    public static ExecutorThreadManager getExecutorThreadManager() {
        return executorThreadManager;
    }
}
