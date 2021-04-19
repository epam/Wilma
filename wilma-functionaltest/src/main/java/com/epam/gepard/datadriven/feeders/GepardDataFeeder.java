package com.epam.gepard.datadriven.feeders;

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

import com.epam.gepard.common.Environment;
import com.epam.gepard.datadriven.DataDrivenParameterArray;

/**
 * Gepard Data Feeder interface.
 * In order to use a dynamic data feeder, you must create a class, implement this interface,
 * and then that class can be used in the testlist as a feeder class.
 *
 * @author tkohegyi
 */
public interface GepardDataFeeder {

    /** Use this error code in case init() method fails with parameter error, so parameter exists, but has bad value. */
    int INIT_FAILED_WITH_PARAMETER_ERROR = -2;
    /** Use this error code in case init() method fails with invalid parameter, like its value cannot be determined. */
    int INIT_FAILED_WITH_INVALID_PARAMETER = -1;

    /**
     * Initiator of the dynamic data feeder class. This method is called with a parameter.
     * The parameter may be null, as well.
     * <p/>
     * Sample:
     * if testrow in testlist: a.b.c.testClassName,com.blah.MyFeeder:56,...
     * then the parameter is "56"
     * <p/>
     * WARNING: do NOT throw exception, handle all of them.
     * If you think something is wrong, then
     * you may use
     * - DataFeederLoader.reportError(String message) OR
     * - DataFeederLoader.reportException(String message, Exception e) methods before you return,
     * and after it, just return with a negative error code. OR
     * - just throw new DataFeederException(String message, int errorCode);
     * Gepard will exit if the return value is less than 0, and log the error code for you.
     *
     * @param testClassName is the name of the actual test class
     * @param parameter coming from the testlist file
     * @param environment holds the properties of the application
     * @return with 0 if everything was OK, or <code>&lt;</code>0 if something is wrong.
     */
    int init(String testClassName, String parameter, Environment environment);

    /**
     * Get info about the total number of test class executions. I.e. how many times the test class should be executed.
     * <p/>
     * The return value must be <code>&gt;</code>=1, otherwise Gepard does not accept it end exits.
     * You may use
     * - DataFeederLoader.reportError(String message) OR
     * - DataFeederLoader.reportException(String message, Exception e) methods before you return.
     *
     * @param className is the name of the actual test class
     * @param inputRows is the number of rows/runs this feeder gets
     * @return with the number of rows/runs after the feeder.
     */
    int calculateRuns(String className, int inputRows);

    /**
     * Apply the feeder and loads a full array of data driven parameters,
     * based on the input data driven parameters. You must take care about the column names, too.
     * <p/>
     * The return value must not be null, otherwise Gepard does not accept it end exits.
     * You may use
     * - DataFeederLoader.reportError(String message) OR
     * - DataFeederLoader.reportException(String message, Exception e) methods before you return.
     *
     * @param className           is the name of the actual test class
     * @param inputParameterArray is the input parameter array to work with.
     * @return with a recalculated DataDrivenParameterArray class, filled and ready for use, or null if something is wrong.
     */
    DataDrivenParameterArray calculateParameterArray(String className, DataDrivenParameterArray inputParameterArray);

}
