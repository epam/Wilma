package com.epam.gepard.generic;

import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.common.threads.TestClassExecutionThread;
import com.epam.gepard.datadriven.DataDrivenParameters;
import com.epam.gepard.exception.SimpleGepardException;

/**
 * General interface class for Test classes using Gepard.
 * No ned to implement any method, all methods have default body, so just use it as it is.
 *
 * @author Tamas_Kohegyi
 */
public interface GepardTestClass {

    /**
     * Detects the Test Class Execution Data object for the running Test Class.
     *
     * @return with Test Class Execution Data object or null, if it was not properly set.
     */
    default TestClassExecutionData getTestClassExecutionData() {
        if (TestClassExecutionThread.CLASS_DATA_IN_CONTEXT != null) {
            return TestClassExecutionThread.CLASS_DATA_IN_CONTEXT.get();
        }
        return null;
    }

    /**
     * Writes a comment row to the log.
     *
     * @param comment to be logged
     */
    default void logComment(final String comment) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().logComment(comment);
        }
    }

    /**
     * Write a comment message to the log, with a description in details link.
     * Can be used to dump stack trace for example.
     *
     * @param comment     Comment message
     * @param description is a multi-row string description for the comment.
     */
    default void logComment(final String comment, final String description) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().logComment(comment, description);
        }
    }

    /**
     * Write a test step message to the log, and increase the step number.
     *
     * @param comment Comment message
     */
    default void logStep(final String comment) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().logStep(comment);
        }
    }

    /**
     * Write an event message to the log.
     *
     * @param comment Event message
     */
    default void logEvent(final String comment) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().logEvent(comment);
        }
    }

    /**
     * Write a warning message to the log, without the step number.
     *
     * @param comment Comment message
     */
    default void logWarning(final String comment) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().logWarning(comment);
        }
    }

    /**
     * Write an event message to the log.
     *
     * @param comment     Event message
     * @param description Event description/info
     */
    default void logResult(final String comment, final String description) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().logResult(comment, description);
        }
    }

    /**
     * Get step information for div tags in html log.
     *
     * @return with the actual div step.
     */
    default int getDivStep() {
        if (getTestClassExecutionData() != null) {
            return getTestClassExecutionData().getHtmlRunReporter().getDivStep();
        }
        throw new SimpleGepardException("Gepard Environment is missing.");
    }

    /**
     * Increase the div step counter.
     */
    default void increaseDivStep() {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().increaseDivStep();
        }
    }

    /**
     * Sets the testcase. N/A test cases are tests with Not Applicable results.
     *
     * @param reason The reason why this TC is N/A
     */
    default void naTestCase(final String reason) {
        if (getTestClassExecutionData() != null) {
            getTestClassExecutionData().getHtmlRunReporter().naTestCase(reason);
        } else {
            throw new SimpleGepardException("N/A Test case is expected.");
        }
    }

    /**
     * Makes a test method as Dummy.
     */
    default void dummyTestCase() {
        getTestClassExecutionData().getHtmlRunReporter().dummyTestCase();
    }

    /**
     * Gets the data driven parameter value for this running class, by its position in the array.
     * Use this to fill data driven parameters in the tests.
     *
     * @param byPosition in the array
     * @return with the parameter value
     */
    default String getDataDrivenTestParameter(final int byPosition) {
        String value;
        DataDrivenParameters parameters = getTestClassExecutionData().getDrivenData();
        if (parameters != null) {
            value = parameters.getTestParameter(parameters.getParameterName(byPosition));
        } else {
            throw new SimpleGepardException("Try to access to a data driven parameter is failed, as it is not data driven test class.");
        }
        return value;
    }

    /**
     * Gets the data driven parameter value for this running class, by its position in the array.
     * Use this to fill data driven parameters in the tests.
     *
     * @param byParameterName in the array
     * @return with the parameter value
     */
    default String getDataDrivenTestParameter(final String byParameterName) {
        String value;
        DataDrivenParameters parameters = getTestClassExecutionData().getDrivenData();
        if (parameters != null) {
            value = parameters.getTestParameter(byParameterName);
        } else {
            throw new SimpleGepardException("Try to access to a data driven parameter is failed, as it is not data driven test class.");
        }
        return value;
    }

}
