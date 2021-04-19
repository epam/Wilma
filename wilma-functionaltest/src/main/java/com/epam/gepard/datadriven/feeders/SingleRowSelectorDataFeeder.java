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
import com.epam.gepard.datadriven.DataFeederLoader;

/**
 * Select the Nth row only, from an existing array. The First row is 1.
 * This does not add new parameters, just reduces it to a single row,
 * therefore parent feeder in the feeder chain is a must.
 *
 * @author tkohegyi
 */
public class SingleRowSelectorDataFeeder implements GepardDataFeeder {

    private int selectedRow;

    @Override
    public int init(final String testClassName, final String parameter, final Environment environment) {
        int returnValue = 0;
        try {
            selectedRow = Integer.valueOf(parameter);
            if (selectedRow <= 0) {
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
        return 1;
    }

    @Override
    public DataDrivenParameterArray calculateParameterArray(final String className, final DataDrivenParameterArray inputParameterArray) {
        DataDrivenParameterArray myArray = new DataDrivenParameterArray();
        //need to have input array
        if (inputParameterArray == null) {
            String error = "PARAMETER ERROR: During the load of: " + className + " no input received to SingleRowSelectorDataFeeder class."
                    + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...";
            DataFeederLoader.reportError(error);
            myArray = null; //we should not continue as it is a wrong chain
        }

        //the new parameter array is one of the rows of the existing array
        if ((myArray != null) && (selectedRow > inputParameterArray.size())) {
            //ups, cannot select this row
            String error = "PARAMETER ERROR: During the load of: " + className + " cannot select the requested row." + "\nRequested: " + selectedRow
                    + ", available: " + inputParameterArray.size() + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...";
            DataFeederLoader.reportError(error);
            myArray = null; //we should not continue as it is a wrong chain
        }

        if (myArray != null) {
            //fill the parameter array of this class
            String[] selectedRowArray = inputParameterArray.get(Integer.valueOf(selectedRow - 1));
            myArray.put(0, selectedRowArray);
            myArray.setParameterNames(inputParameterArray.getParameterNames()); //take care about the parameter names, too
        }
        return myArray;
    }

}
