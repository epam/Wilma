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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.Environment;
import com.epam.gepard.datadriven.DataDrivenParameterArray;
import com.epam.gepard.datadriven.DataFeederLoader;
import com.epam.gepard.datadriven.feeders.labelbasedfeederhelper.LabelBasedFeederDetails;
import com.epam.gepard.datadriven.feeders.labelbasedfeederhelper.LabelBasedFeederFileLoader;
import com.epam.gepard.datadriven.feeders.labelbasedfeederhelper.LabelBasedFeederRelation;

/**
 * The Class LabelBasedDataFeeder.
 * <p/>
 * This is a special type of feeder, where the feeder parameter is used to collect feeders and relations between the feeders.
 * Format is: [FEEDER1:NUMBER_OF_ROWS1:LABEL1:TestParameter.LABEL_TYPE1]+[FEEDER2:NUMBER_OF_ROWS2:LABEL2:TestParameter.LABEL_TYPE2]x[...]
 * A feeder descriptor is between [] brackets and have 4 parameters.
 * String, Feeder name:
 * - can be an empty string or the string 'DEFAULT' - this will select the default test data for the test, same path as the test ended with .csv.
 * - can be a full path on the classpath separated by '/'-es, which describes the location of the feeder
 * - can be any other string, which is a key of a global property, declared in propoerty files
 * Number, rows to be loaded. If empty, then all rows will be used.
 * String, Labels: only rows labeled with this string will be loaded. Labels relations can be AND "&" or OR ";"
 * Examples:
 * - All rows which have L1 or L2 labels: "L1;L2"
 * - All rows which have L1 and L2 labels: "L1&L2"
 * - All rows which have L1 and L2 labels or L3 and L4 labels: "L1&L2;L3&L4"
 * String, Load type. Values are: 'SEQUENTIAL', 'RANDOM', 'MERGE'
 * - SEQUENTIAL - loads feeder rows sequentially, this is the default, in case this part is missing
 * - RANDOM - loads feeder rows randomly (collect all possibilities, and selects randomly from them)
 * - MERGE - loads all the feeder rows that are matching with the defined labels and merge them field by field using a comma separator.
 * <p/>
 * Feeder relations:
 * Two or more feeders for a single test must join with a relational sign. Valid signs are: +, x
 * Signs must be placed among feeders. A feeder will be associated with a sign on its left.
 * The first feeder must not have a sign.
 * The sign '+': joins one row from the right hand feeder sequentially (in RANDOM case rows are shuffled before join) with the next row from the left hand feeder.
 * The sign 'x': multiplies the rows in the left hand feeder then applies '+'.
 * e.g.: []+[feeders.ONE:3:LABEL1]x[feeders.TWO:::RANDOM]
 * <p/>
 * Data files: Use CSV data files.
 * - The first row is the header.
 * - Header's 1. column must be 'LABEL'
 * - CSV must contain at least a LABEL and a value column.
 * - You can insert an optional 'DESCRIPTION' column anywhere after 'LABEL'. Mixing multiple feeders will have a name generation mixing these columns.
 * - Other header column names should reflect test parameter variable names from the test script. E.g. "userName" column for a @TestParam String "userName" variable.
 * Otherwise the variable should be binded to a column with the TestParam's id parameter, like: @TestParam(id = "USERNAME") String userName,
 * if the column name is "USERNAME".
 * - Any other row can be commented with '//' or '#' on the beginning of the line.
 * - All other non commented rows are data rows.
 * - Cell values should be quoted ("value1") and separated with commas ("value1","value2").
 */
public class LabelBasedDataFeeder implements GepardDataFeeder {
    private static final Logger LOG = LoggerFactory.getLogger(LabelBasedDataFeeder.class);
    private static final String NAME_COLUMN = "DESCRIPTION"; // optional column. All these columns from feeders will be merged into one, giving a name for the data row

    private static final int FEEDER_FILE = 0;
    private static final int FEEDER_ROW_NUMBER = 1;
    private static final int FEEDER_LABEL = 2;
    private static final int FEEDER_LABEL_TYPE = 3;

    private static final int ERROR_FEEDER_RELATION_LABEL_TYPE_IS_MISSING = -1;
    private static final int ERROR_FEEDER_RELATION_INCORRECT = -2;
    private static final int ERROR_FEEDER_RELATION_EXCEPTION = -3;
    private static final int ERROR_FEEDER_RELATION_WRONG_FORMAT = -4;
    private static final int ERROR_FEEDER_RELATION_WRONG_ROW_NUMBER = -5;
    private static final int ERROR_FEEDER_RELATION_NO_ROW_LOADED = -6;
    private static final int ERROR_FEEDER_FILE_MISSING = -7;
    private static final int ERROR_FEEDER_FILE_PROPERTY_MISSING = -8;
    private static final int ERROR_FEEDER_DUPLICATED = -9;
    private static final int ERROR_FEEDER_COLUMN_NAMES_DUPLICATED = -10;

    private List<LabelBasedFeederDetails> feeders = new ArrayList<>();
    private List<String> feederRelations = new ArrayList<>();

    private String testClassName;

    private Environment environment;

    public void setTestClassName(final String testClassName) {
        this.testClassName = testClassName;
    }

    @Override
    public int init(final String testClassName, final String parameter, final Environment environment) {
        int result = 0;
        this.environment = environment;
        try {
            setTestClassName(testClassName); //reserve it for future use
            // load feeders
            String[] feederDescriptors = extractFeedersFromParameter(parameter);
            loadFeeders(feederDescriptors);
            // check if all feeders are unique
            if (!isAllFeederUnique()) {
                throw new DataFeederException("Duplicated feeder found.", ERROR_FEEDER_DUPLICATED);
            }

            // load feeder relations
            setFeederRelations(parameter);

            // Load all feeders
            for (LabelBasedFeederDetails feeder : feeders) {
                result = loadFeeder(feeder);
                if (result < 0) {
                    return result;
                }
            }
        } catch (DataFeederException e) {
            result = e.getErrorCode();
            LOG.debug(e.getMessage(), e);
        }
        return result;
    }

    private boolean isAllFeederUnique() {
        Set<Integer> s = new HashSet<>();
        for (LabelBasedFeederDetails feeder : feeders) {
            s.add(feeder.hashCode());
        }
        return s.size() == feeders.size();
    }

    private void setFeederRelations(final String parameter) throws DataFeederException {
        String feederDescriptor = (parameter.lastIndexOf("[") > parameter.indexOf("]")) ? parameter.replaceFirst("\\[.*?\\]", "") : parameter;
        feederRelations = Arrays.asList(feederDescriptor.split("\\[.*?\\]"));
        if (feeders.size() != feederRelations.size() + 1) {
            String errorMessage = "Error occurred during processing FeederDescriptor: "
                    + parameter
                    + "\nFeeder relation number must equal to Feeders - 1."
                    + "\nFormat: [FEEDER1:NUMBER_OF_ROWS1:LABEL1:TestParameter.LABEL_TYPE1]+[FEEDER2:NUMBER_OF_ROWS2:LABEL2:TestParameter.LABEL_TYPE2]x[...]";
            throw new DataFeederException(errorMessage, ERROR_FEEDER_RELATION_INCORRECT);
        }

        // set feederRelation for feeders
        if (feeders.size() > 1) {
            for (int i = 1; i < feeders.size(); i++) {
                LabelBasedFeederRelation relation = LabelBasedFeederRelation.getByValue(feederRelations.get(i - 1));
                if (relation == null) {
                    throw new DataFeederException("Error occurred during processing FeederDescriptor. Possible feeder relations are: +, x",
                            ERROR_FEEDER_RELATION_EXCEPTION);
                }
                feeders.get(i).setFeederRelation(relation);
            }
        }
    }

    private String[] extractFeedersFromParameter(final String parameter) throws DataFeederException {
        //check that we have [..] format of parameter
        if (parameter == null || !parameter.startsWith("[") || !parameter.endsWith("]")) {
            throw new DataFeederException("Error occurred during processing parameter: " + parameter
                    + "\nExpected format: [FEEDER1:NUMBER_OF_ROWS1:LABEL1:TestParameter.LABEL_TYPE1]...", ERROR_FEEDER_RELATION_WRONG_FORMAT);
        }
        String feederDescriptor = parameter.substring(1, parameter.length() - 1);
        String[] feederDescriptors = feederDescriptor.split("\\].\\["); //feeder descriptors are separated
        if (feederDescriptors.length == 0) {
            //this was "].[" most probably
            throw new DataFeederException("Error occurred during processing parameter: " + parameter
                    + ", seems default feeder is duplicated.\nExpected format: [FEEDER1:NUMBER_OF_ROWS1:LABEL1:TestParameter.LABEL_TYPE1]...",
                    ERROR_FEEDER_DUPLICATED);
        }
        return feederDescriptors;
    }

    private void loadFeeders(final String[] feederDescriptors) throws DataFeederException {
        for (String fd : feederDescriptors) {
            LabelBasedFeederDetails feederDetails = new LabelBasedFeederDetails(testClassName.replaceAll("\\.", "/"));
            String[] tokens = fd.split(":");
            if (tokens.length > FEEDER_FILE && tokens[FEEDER_FILE] != null && !"".equals(tokens[FEEDER_FILE])
                    && !"DEFAULT".equals(tokens[FEEDER_FILE])) {
                feederDetails.setFeederFile(tokens[FEEDER_FILE]);
            }
            if (tokens.length > FEEDER_ROW_NUMBER && tokens[FEEDER_ROW_NUMBER] != null) { // rowNumber
                int rowNumber;
                try {
                    if ("".equals(tokens[FEEDER_ROW_NUMBER])) {
                        rowNumber = 0; //empty means 0 means all
                    } else {
                        rowNumber = Integer.parseInt(tokens[FEEDER_ROW_NUMBER]);
                    }
                } catch (NumberFormatException e) {
                    String reportError = "Error in LabelBasedDataFeeder: Second feeder token must be empty or valid number,"
                            + " got: " + tokens[FEEDER_ROW_NUMBER];
                    throw new DataFeederException(reportError, ERROR_FEEDER_RELATION_WRONG_ROW_NUMBER);
                }
                feederDetails.setRowNumber(rowNumber);
            }
            if (tokens.length > FEEDER_LABEL && tokens[FEEDER_LABEL] != null) { // label
                feederDetails.setLabels(tokens[FEEDER_LABEL]);
            }
            if (tokens.length > FEEDER_LABEL_TYPE && tokens[FEEDER_LABEL_TYPE] != null) { // labelType
                try {
                    feederDetails.setLabelType(LabelBasedFeederDetails.LabelType.valueOf(tokens[FEEDER_LABEL_TYPE]));
                } catch (IllegalArgumentException e) {
                    String reportError = "Error in LabelBasedDataFeeder: Fourth feeder token must be a TestParameter.LabelType!"
                            + " Values: SEQUENTIAL, RANDOM, MERGE. Default is SEQUENTIAL.";
                    throw new DataFeederException(reportError, ERROR_FEEDER_RELATION_LABEL_TYPE_IS_MISSING);
                }
            }
            feeders.add(feederDetails);
        }
    }

    @Override
    public int calculateRuns(final String className, final int inputRows) {
        int result = 0;
        for (LabelBasedFeederDetails feeder : feeders) {
            result = feeder.getFeederRelation().calculateRuns(result, feeder.getParameterList().size());
        }
        return result;
    }

    @Override
    public DataDrivenParameterArray calculateParameterArray(final String className, final DataDrivenParameterArray inputParameterArray) {
        // Mix feeders into one parameter table
        DataDrivenParameterArray result = new DataDrivenParameterArray();
        List<String> columns = new ArrayList<>();
        // Create the parameter table from the feeders
        for (LabelBasedFeederDetails feeder : feeders) {
            columns.addAll(feeder.getParameterNames());
            feeder.getFeederRelation().addFeeder(feeder, result);
        }

        // remove Name columns and merge them into one as the first column of the table, get column name indexes
        List<Integer> nameIndexes = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            if (NAME_COLUMN.equals(columns.get(i))) {
                nameIndexes.add(i);
            }
        }

        // set column names
        result.setParameterNames(columns.toArray(new String[columns.size()]));

        // merge name columns
        if (!nameIndexes.isEmpty()) {
            result.setParameterNames(removeFromArrayByIndex(nameIndexes, result.getParameterNames()));
            result.setParameterNames(ArrayUtils.add(result.getParameterNames(), 0, NAME_COLUMN));
            List<String> nameColValue;
            for (Integer rowNum : result.keySet()) {
                nameColValue = selectFromArrayByIndex(nameIndexes, result.get(rowNum));
                result.put(rowNum, removeFromArrayByIndex(nameIndexes, result.get(rowNum)));
                result.put(rowNum, ArrayUtils.add(result.get(rowNum), 0, StringUtils.join(nameColValue, " ")));
            }
        }

        // free up memory after results have been created
        feeders = null;
        feederRelations = null;

        // check if all feeders have unique column names
        try {
            if (result.getParameterNames().length != new HashSet<>(Arrays.asList(result.getParameterNames())).size()) {
                String errorText = "LabelTestParameterFeeder error: more feeders contain the same column name! (other than LABEL and DESCRIPTION)";
                throw new DataFeederException(errorText, ERROR_FEEDER_COLUMN_NAMES_DUPLICATED);
            }
        } catch (DataFeederException e) {
            result = null;
        }
        return result;
    }

    private List<String> selectFromArrayByIndex(final List<Integer> indexes, final String[] array) {
        List<String> result = new ArrayList<>();
        for (Integer i : indexes) {
            if (array.length > i && i >= 0) {
                result.add(array[i]);
            }
        }
        return result;
    }

    private String[] removeFromArrayByIndex(final List<Integer> indexes, final String[] array) {
        String[] result = array;
        Collections.sort(indexes);
        int increment = 0;
        for (Integer i : indexes) {
            if (array.length > i && i >= 0) {
                result = ArrayUtils.remove(result, i - increment++);
            }
        }
        return result;
    }

    private int loadFeeder(final LabelBasedFeederDetails feederDetails) throws DataFeederException {
        int result = 0;
        // convert feeder file parameter into existing path
        LabelBasedFeederFileLoader loaderType = LabelBasedFeederFileLoader.TXT;
        String filePath = detectFilePath(feederDetails);
        if (filePath.endsWith(".csv")) {
            loaderType = LabelBasedFeederFileLoader.CSV;
        }
        feederDetails.setFeederFile(filePath);

        // Select the required value
        try {
            // load feeder
            loaderType.loadFeeder(feederDetails);
        } catch (IOException e) {
            String errorText = "Error reading LabelBasedDataFeeder: " + feederDetails.getFeederFile() + "\nfilePath: " + filePath;
            throw new DataFeederException(errorText, ERROR_FEEDER_FILE_MISSING);
        }

        // if feeder label type is random, then shuffle parameter rows
        if (LabelBasedFeederDetails.LabelType.RANDOM.equals(feederDetails.getLabelType())) {
            Collections.shuffle(feederDetails.getParameterList());
            feederDetails.reduceParameterList();
        }

        // if feeder label type is merge, then append the specified parameters in a row to a string
        if (LabelBasedFeederDetails.LabelType.MERGE.equals(feederDetails.getLabelType())) {
            String[] merged = new String[feederDetails.getParameterList().get(0).length];
            Arrays.fill(merged, "");

            for (int rowNum = 0; rowNum < feederDetails.getParameterList().size(); rowNum++) {
                for (int colNum = 0; colNum < merged.length; colNum++) {
                    merged[colNum] += (rowNum == 0) ? feederDetails.getParameterList().get(rowNum)[colNum] : ","
                            + feederDetails.getParameterList().get(rowNum)[colNum];
                }
            }

            feederDetails.getParameterList().clear();
            feederDetails.getParameterList().add(merged);
        }

        if (feederDetails.getParameterList().isEmpty()) {
            result = ERROR_FEEDER_RELATION_NO_ROW_LOADED;
            DataFeederLoader.reportError("Error reading LabelBasedDataFeeder - No rows loaded by the feeder: " + feederDetails.getFeederFile()
                    + "\nfilePath: " + filePath);
        }

        return result;
    }

    private String detectFilePath(final LabelBasedFeederDetails feederDetails) throws DataFeederException {
        String filePath = feederDetails.getFeederFile();
        if (this.getClass().getClassLoader().getResourceAsStream(filePath) == null) {
            if (this.getClass().getClassLoader().getResourceAsStream(filePath.concat(".txt")) != null) {
                filePath = filePath.concat(".txt");
            } else if (this.getClass().getClassLoader().getResourceAsStream(filePath.concat(".csv")) != null) {
                filePath = filePath.concat(".csv");
            } else {
                String propertyBasedFeederFile = environment.getProperty(feederDetails.getFeederFile());
                filePath = propertyBasedFeederFile;
                if (propertyBasedFeederFile == null) {
                    //property is missing
                    throw new DataFeederException("Feeder related property cannot be identified.", ERROR_FEEDER_FILE_PROPERTY_MISSING);
                } else {
                    if (this.getClass().getClassLoader().getResourceAsStream(propertyBasedFeederFile) == null) {
                        throw new DataFeederException("Feeder file does not exist: " + filePath, ERROR_FEEDER_FILE_MISSING);
                    }
                }
            }
        }
        return filePath;
    }

}
