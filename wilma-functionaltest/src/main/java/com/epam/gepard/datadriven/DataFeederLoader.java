package com.epam.gepard.datadriven;

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

import java.lang.reflect.InvocationTargetException;

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.common.Environment;
import com.epam.gepard.datadriven.feeders.GepardDataFeeder;
import com.epam.gepard.datadriven.feeders.SimpleMultiplierDataFeeder;
import com.epam.gepard.exception.ShutDownException;
import com.epam.gepard.util.ExitCode;

/**
 * This class main class for data driven tests.
 * Loads the data feeder class(es), and can multiply the execution of a test class
 * by using and evaluating the complete feeder descriptor string.
 */
public class DataFeederLoader {

    private GepardDataFeeder feeder; //default
    private String feederClassName; //default
    private String feederClassParameter; //default
    private String nextFeederDescriptor; //default
    private DataFeederLoader nextDataFeederLoader; //default
    private DataDrivenParameterArray parameterArray; //only the main loader class has this value
    private Environment environment;

    /**
     * Data Feeder Loader class.
     * Gets the feeder descriptor, and evaluates it.
     * A feeder descriptor can be:
     * - empty: end of the feeder descriptor chain
     * - number: fallbacks to SimpleMultiplierFeeder
     * - feederclass
     * - feederclass:param
     * - feederclass:param@feederdescriptor
     * - feederclass@feederdescriptor
     *
     * @param className is the name of the class.
     * @param feederDescriptor is the feeder descriptor string.
     * @param environment holds the properties for the application.
     */
    public DataFeederLoader(final String className, final String feederDescriptor, final Environment environment) {
        this.environment = environment;
        if (feederDescriptor.length() == 0) {
            return; //nothing to do
        }
        //there is something in the feeder descriptor
        try {
            tryToInterpretFeederDescriptorAsNumber(feederDescriptor);
        } catch (NumberFormatException e) {
            //must be a feeder class or just a parameter for the default filter
            //this should be then one of:
            //* - feederclass
            //* - feederclass:param
            //* - feederclass:param@feederdescriptor
            //* - feederclass@feederdescriptor
            //* - :param   (this uses the default feeder)
            //* - :param@feederdescriptor (this uses the default feeder)
            interpretAsFeederClassOrDefaultFilterParameter(feederDescriptor);
        }
        //now feederClassName, feederClassParameter and nextFeederDescriptor is ready for use
        checkFeederClassValidity(className);
        //try to load the loader class with the given parameter
        feeder = loadDataLoaderClass(className, feederClassName); //if load fails, Gepard exits
        // CALL THE FEEDER INIT METHOD
        int initValue = feeder.init(className, feederClassParameter, environment);
        checkFeederClassInitialization(className, initValue);
        //we are ready, now load the next feeder in the feeder chain, if we have the next feeder descriptor available
        if (nextFeederDescriptor != null) {
            //load the next part of the chain
            nextDataFeederLoader = new DataFeederLoader(className, nextFeederDescriptor, environment);
        }
    }

    private void tryToInterpretFeederDescriptorAsNumber(final String feederdescriptor) {
        Integer.valueOf(feederdescriptor); //try to interpret it as a number
        feederClassName = SimpleMultiplierDataFeeder.class.getName();
        feederClassParameter = feederdescriptor;
        nextFeederDescriptor = null;
    }

    private void interpretAsFeederClassOrDefaultFilterParameter(final String feederdescriptor) {
        int nextParameterIndex = feederdescriptor.indexOf(":");
        int nextFeederDescriptorIndex = feederdescriptor.indexOf("@");
        if ((nextParameterIndex == 0) && (nextFeederDescriptorIndex > 0)) {
            // :param@feederdescriptor
            feederClassName = environment.getProperty(Environment.GEPARD_DATA_DRIVEN_FEEDER_CLASS);
            feederClassParameter = feederdescriptor.substring(nextParameterIndex + 1, nextFeederDescriptorIndex);
            nextFeederDescriptor = feederdescriptor.substring(nextFeederDescriptorIndex + 1);
        } else {
            if (nextParameterIndex == 0) {
                // :param
                feederClassName = environment.getProperty(Environment.GEPARD_DATA_DRIVEN_FEEDER_CLASS);
                feederClassParameter = feederdescriptor.substring(nextParameterIndex + 1);
                nextFeederDescriptor = null;
            } else {
                if ((nextParameterIndex > 0) && (nextFeederDescriptorIndex < 0)) {
                    //- feederclass:param
                    feederClassName = feederdescriptor.substring(0, nextParameterIndex);
                    feederClassParameter = feederdescriptor.substring(nextParameterIndex + 1);
                    nextFeederDescriptor = null;
                } else {
                    if ((nextParameterIndex > 0) && (nextFeederDescriptorIndex > 0) && (nextParameterIndex < nextFeederDescriptorIndex)) {
                        //* - feederclass:param@feederdescriptor(withorwithout:and@)
                        feederClassName = feederdescriptor.substring(0, nextParameterIndex);
                        feederClassParameter = feederdescriptor.substring(nextParameterIndex + 1, nextFeederDescriptorIndex);
                        nextFeederDescriptor = feederdescriptor.substring(nextFeederDescriptorIndex + 1);
                    } else {
                        if (nextFeederDescriptorIndex > 0) {
                            //* - feederclass@feederdescriptor(withorwithout:and@)
                            feederClassName = feederdescriptor.substring(0, nextFeederDescriptorIndex);
                            feederClassParameter = null;
                            nextFeederDescriptor = feederdescriptor.substring(nextFeederDescriptorIndex + 1);
                        } else {
                            //* - feederclass
                            feederClassName = feederdescriptor;
                            feederClassParameter = null;
                            nextFeederDescriptor = null;
                        }
                    }
                }
            }
        }
    }

    private void checkFeederClassInitialization(final String className, final int initValue) {
        if (initValue < 0) { //initialize the the feeder class
            //init failed
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Data Feeder Class: " + feederClassName + " init() method with parameter: "
                    + feederClassParameter + " was failed with error code: " + initValue + " at Class in testlist: " + className
                    + "\nPlease fix it!\nNow exiting...");
            throw new ShutDownException(ExitCode.EXIT_CODE_DATA_FEEDER_INIT_FAILED);
        }
    }

    private void checkFeederClassValidity(final String className) {
        if ((feederClassName == null) || (feederClassName.length() == 0)) {
            //it cannot be a valid feeder class, exiting...
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Bad Data Feeder Descriptor found at Class in testlist: " + className
                    + "\nPlease fix the Data Feeder Descriptor in the test list!\nNow exiting...");
            throw new ShutDownException(ExitCode.EXIT_CODE_DATA_FEEDER_BAD_DESCRIPTOR);
        }
    }

    /**
     * Loads the Data Feeder Class and returns with the loaded data feeder class.
     *
     * @param className       test class name that tries to load the data feeder.
     * @param loaderClassName name of the class that loads the data
     * @return with the actual data feeder class
     */
    private GepardDataFeeder loadDataLoaderClass(final String className, final String loaderClassName) {
        Object[] constructorInputParam = new Object[]{};
        GepardDataFeeder feeder = null;
        String feederClassName = loaderClassName;
        try {
            //opportunity of recognising built-in feeder class names
            if (!feederClassName.contains(".")) { // if there is no dot in the class name, add a default package name
                feederClassName = "com.epam.gepard.datadriven.feeders." + feederClassName;
            }
            feeder = (GepardDataFeeder) Class.forName(feederClassName).getConstructors()[0].newInstance(constructorInputParam);
        } catch (IllegalArgumentException | SecurityException | InstantiationException | ClassNotFoundException | IllegalAccessException
                | InvocationTargetException e) {
            AllTestRunner.exitFromGepardWithCriticalException("\nERROR: Load of Data Feeder Class: " + feederClassName
                    + " failed at Class in testlist: " + className + "\nPlease fix it!\nNow exiting...\n", e, true,
                    ExitCode.EXIT_CODE_DATA_FEEDER_CLASS_LOAD_FAILED);
        }
        return feeder;
    }

    /**
     * Counts the total test class runs in the actual data feeder chain.
     *
     * @param className is the name of the class.
     * @param count is the input rows the feeder can work with and calculate the new runs.
     * @return with the total number of runs.
     */
    public int calculateRuns(final String className, final int count) {
        if (feeder == null) {
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Data Feeder Class: " + feederClassName
                    + " getTotalRuns() method was failed, because the feeder class is missing" + " at Class in testlist: " + className
                    + "\nPlease fix it!\nNow exiting...");
            throw new ShutDownException(ExitCode.EXIT_CODE_DATA_FEEDER_CLASS_MISSING);
        }
        int newCount = feeder.calculateRuns(className, count);
        if (newCount <= 0) {
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Data Feeder Class" + feederClassName + " getTotalRuns() method"
                    + " was failed with error code: " + newCount + " at Class in testlist: " + className + "\nPlease fix it!\nNow exiting...");
            throw new ShutDownException(ExitCode.EXIT_CODE_DATA_FEEDER_CLASS_ERROR);
        }
        if (nextDataFeederLoader != null) {
            //load the next part of the chain
            newCount = nextDataFeederLoader.calculateRuns(className, newCount);
        }
        return newCount;
    }

    /**
     * Get the Nth row from the parameterArray, and build up the DataDrivenParameters.
     * Used at collecting the tests before test execution.
     *
     * @param className is the name of the class.
     * @param drivenDataRowNo is the row number.
     * @return with the parameters for a test class run
     */
    public DataDrivenParameters getParameterRow(final String className, final int drivenDataRowNo) {
        //first get the row
        String[] p = parameterArray.get(drivenDataRowNo);
        // If parameterNames is null => use parameterArray.parameterNames. This is the loaded header from the data file. Otherwise use the given header.
        DataDrivenParameters paramRow = new DataDrivenParameters(p, parameterArray.getParameterNames());
        AllTestRunner.CONSOLE_LOG.info("Loaded: " + className + ", Data-driven test, loaded row:" + drivenDataRowNo + ", number of params:"
                + paramRow.getParameters().length);
        return paramRow;
    }

    /**
     * Calculates the data array for the class. Calculation is started on the input data array,
     * and calculation is performed by the feeder.
     * @param className is the actual test class.
     * @param inputParameterArray data array to be used as starting data array for the calculation.
     * @return with the calculated data array.
     */
    public DataDrivenParameterArray calculateParameterArray(final String className, final DataDrivenParameterArray inputParameterArray) {
        if (feeder == null) {
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Data Feeder Class: " + feederClassName
                    + " calculateParameterArray() method was failed, because the feeder class is missing" + " at Class in testlist: " + className
                    + "\nPlease fix it!\nNow exiting...");
            throw new ShutDownException(ExitCode.EXIT_CODE_DATA_FEEDER_CLASS_MISSING);
        }
        DataDrivenParameterArray newArray = feeder.calculateParameterArray(className, inputParameterArray);
        if (newArray == null) {
            throw new ShutDownException(ExitCode.EXIT_CODE_DATA_FEEDER_CLASS_DATA_CALCULATION_ERROR);
        }
        if (nextDataFeederLoader != null) {
            //load the next part of the chain
            newArray = nextDataFeederLoader.calculateParameterArray(className, newArray);
        }
        return newArray;
    }

    /**
     * Stores a parameter data array for this class.
     * @param parameterArray is the parameter data array to be stored.
     */
    public void reserveParameterArray(final DataDrivenParameterArray parameterArray) {
        this.parameterArray = parameterArray;
    }

    /**
     * Use this method to report Feeder problems to the user.
     * Suggested message format:
     * <p/>
     * String message = "PARAMETER ERROR: During the load of: " + className + " not enough rows were loaded."
     * + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...";
     * DataFeederLoader.reportError(message);
     *
     * @param message is the error message.
     */
    public static void reportError(final String message) {
        AllTestRunner.CONSOLE_LOG.info(message);
    }

    /**
     * Use this method to report an exception in a Feeder method to the user.
     * It shows the stack trace, too.
     * <p/>
     * Suggested message format:
     * <p/>
     * String message = "ERROR: During the load of: " + className
     * + ", param file caused EX.\nReason:" + e.getMessage()
     * + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...\n";
     * DataFeederLoader.reportException(message);
     *
     * @param message is the error message.
     * @param e       is the exception.
     */
    public static void reportException(final String message, final Exception e) {
        AllTestRunner.exitFromGepardWithCriticalException(message, e, false, 0);
    }

}
