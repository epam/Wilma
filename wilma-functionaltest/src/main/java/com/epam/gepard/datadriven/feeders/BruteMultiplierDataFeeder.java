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
 * This feeder executes the input class several times, do nothing else.
 * Be aware that ads " - #n" to the end of the first parameter, to identify the multiplication of the class.
 *
 * @author tkohegyi
 */
public class BruteMultiplierDataFeeder implements GepardDataFeeder {

    private int multiplier;

    @Override
    public int init(final String testClassName, final String parameter, final Environment environment) {
        int returnValue = 0;
        try {
            multiplier = Integer.valueOf(parameter);
            if (multiplier <= 0) {
                returnValue = INIT_FAILED_WITH_PARAMETER_ERROR; //init was failed, parameter value is not acceptable
            }
        } catch (NumberFormatException e) {
            //maybe log4j can be used here
            returnValue = INIT_FAILED_WITH_INVALID_PARAMETER; //init was failed, it is not a valid parameter
        }
        return returnValue;
    }

    @Override
    public int calculateRuns(final String className, final int inputRows) {
        return inputRows * multiplier; // this is a simple multiplier class
    }

    /**
     * This Data Feeder just multiplies the data as many times as requested in the parameter.
     * @param className is the name of the caller test class
     * @param inputParameterArray is the original data parameter array
     * @return with the updated data parameter array
     */
    @Override
    public DataDrivenParameterArray calculateParameterArray(final String className, final DataDrivenParameterArray inputParameterArray) {
        //need to have input array
        DataDrivenParameterArray transferArray = inputParameterArray; //need to change it, as we cannot touch the input array
        if (transferArray == null) { // we do not have, so then we have to simulate the data driven approach
            transferArray = new DataDrivenParameterArray();
            String[] parameterNames = new String[1];
            parameterNames[0] = "MULTIPLIER";
            transferArray.setParameterNames(parameterNames);
            String[] newRow = new String[1];
            newRow[0] = "";
            transferArray.put(0, newRow); //put the only row into the input array
        } // now we have the transfer parameter array, for sure

        //recalculate the parent array
        DataDrivenParameterArray newArray = new DataDrivenParameterArray();
        Integer arrayKey = 0; //this will be our new key
        for (int i = 1; i <= multiplier; i++) { //as this feeder is a multiplier
            //iterate through to old array and add the same value to every row
            for (String[] original : transferArray.values()) {
                String[] newRow = new String[original.length];
                System.arraycopy(original, 0, newRow, 0, original.length);
                newRow[0] = original[0] + " - #" + i;
                newArray.put(arrayKey, newRow); //put the new row into the new array
                arrayKey++; //prepare the next key
            }
        }

        //plus we should recreate the parameter names
        newArray.setParameterNames(transferArray.getParameterNames());
        return newArray;
    }

}
