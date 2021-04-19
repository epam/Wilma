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
import com.epam.gepard.generic.GenericListTestSuite;

/**
 * This feeder just offers to give a single parameter value to the test class, no change in data array at all.
 * If you use subsequent FakeDataFeeder, then you will get the latest parameter only.
 * You can get the parameter value as String by using:
 *    String parameter = GenericListTestSuite.globalDataStorage.get("XFakeDataFeeder-" + className) call.
 *
 * @author tkohegyi
 *
 */
public class FakeDataFeeder implements GepardDataFeeder {

    private String parameter;

    @Override
    public int init(final String testClassName, final String parameter, final Environment environment) {
        this.parameter = parameter;
        return 0;
    }

    @Override
    public int calculateRuns(final String className, final int inputRows) {
        return inputRows; // do nothing with the array
    }

    /**
     * This Data Feeder handles the txt and cvs files.
     * @param className is the name of the caller test class
     * @param inputParameterArray is the original data parameter array
     * @return with the updated data parameter array
     */
    @Override
    public DataDrivenParameterArray calculateParameterArray(final String className, final DataDrivenParameterArray inputParameterArray) {
        //put the param in the global data storage
        GenericListTestSuite.getGlobalDataStorage().put("XFakeDataFeeder-" + className, parameter);
        //do nothing
        return inputParameterArray;
    }

}
