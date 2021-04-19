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

import java.io.File;
import java.io.IOException;

import com.epam.gepard.common.Environment;
import com.epam.gepard.datadriven.DataDrivenParameterArray;
import com.epam.gepard.datadriven.DataFeederLoader;
import com.epam.gepard.util.FileProvider;

/**
 * This feeder loads the data from the TXT and CSV data files, and use the first N rows. N is given as feeder parameter.
 * <p/>
 * This is the most common usage of the feeders. Because of this, its parameter (int) can be used in the testlist file
 * without specifying the feeder class, i.e. this is the default feeder for "int" type parameters.
 *
 * @author tkohegyi
 */
public class SimpleMultiplierDataFeeder implements GepardDataFeeder {

    private int multiplier;
    private SimpleMultiplierDataFeederFileLoader arrayLoader;
    private Environment environment;

    @Override
    public int init(final String testClassName, final String parameter, final Environment environment) {
        this.environment = environment;
        int returnValue = 0;
        try {
            multiplier = Integer.valueOf(parameter);
            if (multiplier <= 0) {
                returnValue = INIT_FAILED_WITH_PARAMETER_ERROR; //init was failed, parameter value is not acceptable
            }
            arrayLoader = new SimpleMultiplierDataFeederFileLoader();
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
     * This Data Feeder handles the txt and cvs files.
     * @param className is the name of the caller test class
     * @param inputParameterArray is the original data parameter array
     * @return with the updated data parameter array
     */
    @Override
    public DataDrivenParameterArray calculateParameterArray(final String className, final DataDrivenParameterArray inputParameterArray) {
        //first fill the parameter array of this class
        DataDrivenParameterArray myArray = null;
        FileProvider fileProvider = new FileProvider();
        try {
            //now load test parameters.
            myArray = loadTestParameters(className, fileProvider, arrayLoader);
        } catch (IOException | IllegalArgumentException e) {
            String error = "ERROR: During the load of: " + className + ", param file caused EX.\nReason:" + e.getMessage()
                    + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...\n";
            DataFeederLoader.reportException(error, e);
            myArray = null;
        } catch (DataFeederException e) {
            //the exception itself already notifies the DataFeederLoader class
            myArray = null;
        }
        //my array is loaded, or null in case of error
        DataDrivenParameterArray newArray = null;
        if (myArray != null) {
            //now recalculate the parent array
            newArray = recalculateTheParentArray(inputParameterArray, myArray);
        }
        return newArray;
    }

    private DataDrivenParameterArray loadTestParameters(final String className, final FileProvider fileProvider,
            final SimpleMultiplierDataFeederFileLoader arrayLoader) throws IOException, DataFeederException {
        DataDrivenParameterArray myArray = null;
        String configFilenameBase = environment.getProperty(Environment.GEPARD_DATA_DRIVEN_PATH_BASE).concat("/").concat(className.replace(".", "/"));
        ConfigFileInfo fileInfo = new ConfigFileInfo(configFilenameBase);
        //now load the file and fill my data array
        if (fileProvider.isFileAvailable(fileInfo.getConfigFilename())) { //must exist
            myArray = arrayLoader.loadFile(fileInfo, multiplier);
            //check if we got the parameter string at all
            checkIfWeGotParameterString(className, myArray);
            //check if we have enough parameter rows
            checkIfWeHaveEnoughParameterRows(className, myArray);
        } else {
            //no configuration file found for the specific class
            String error = "Loading: " + className + " failed, no (txt) or (csv) data file found.";
            throw new DataFeederException(error); //wrong file content, cannot continue

        }
        return myArray;
    }

    private void checkIfWeHaveEnoughParameterRows(final String className, final DataDrivenParameterArray myArray) throws DataFeederException {
        if (myArray.size() < multiplier) {
            //no, we did not get enough info
            String error = "PARAMETER ERROR: During the load of: " + className + " not enough rows were loaded."
                    + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...";
            throw new DataFeederException(error); //wrong file content, cannot continue
        }
    }

    private void checkIfWeGotParameterString(final String className, final DataDrivenParameterArray myArray) throws DataFeederException {
        if (myArray.isEmpty()) {
            //no, we did not get it
            String error = "PARAMETER ERROR: During the load of: " + className + " no param info was found."
                    + "\nCheck both testlist.txt and class.txt/csv file, Now exiting...";
            throw new DataFeederException(error); //wrong file content, cannot continue
        }
    }

    private DataDrivenParameterArray recalculateTheParentArray(final DataDrivenParameterArray inputParameterArray,
            final DataDrivenParameterArray myArray) {
        DataDrivenParameterArray newArray;
        if (inputParameterArray == null) {
            //we are the first in the feeder chain
            newArray = myArray;
        } else {
            //we are not the first array in the chain, so need to recreate the array
            newArray = recreateTheArray(inputParameterArray, myArray);
            //plus we should recreate the parameter names
            newArray.setParameterNames(DataDrivenParameterArray.concat(inputParameterArray.getParameterNames(), myArray.getParameterNames()));
        }
        return newArray;
    }

    private DataDrivenParameterArray recreateTheArray(final DataDrivenParameterArray inputParameterArray, final DataDrivenParameterArray myArray) {
        DataDrivenParameterArray newArray;
        newArray = new DataDrivenParameterArray();
        Integer arrayKey = 0; //this will be our new key
        for (int i = 0; i < multiplier; i++) { //as this feeder is a multiplier
            String[] addition = myArray.get(Integer.valueOf(i));
            //iterate through to old array and add the same value to every row
            for (String[] old : inputParameterArray.values()) {
                String[] brandNew = DataDrivenParameterArray.concat(old, addition);
                newArray.put(arrayKey, brandNew); //put the new row into the new array
                arrayKey++; //prepare the next key
            }
        }
        return newArray;
    }

    /**
     * This inner class is used to hold configuration file specific information.
     * Like: is it a CSV file or not, the filename itself, and the splitter filed used in the file.
     */
    class ConfigFileInfo {
        private boolean isCSV;
        private String configFilename;
        private String splitter;

        protected ConfigFileInfo(final String configFilenameBase) {
            //select between txt and csv file
            configFilename = configFilenameBase.concat(".txt");
            if (!(new File(configFilename)).exists()) { //must be csv
                configFilename = configFilenameBase.concat(".csv");
                isCSV = true;
            }
            //select the separator char
            splitter = environment.getProperty(Environment.GEPARD_DATA_DRIVEN_COLUMN_SPLITTER);
            if (isCSV) {
                splitter = ",";
            }

        }

        public boolean isCSV() {
            return isCSV;
        }

        public String getSplitter() {
            return splitter;
        }

        public String getConfigFilename() {
            return configFilename;
        }

    }

}
