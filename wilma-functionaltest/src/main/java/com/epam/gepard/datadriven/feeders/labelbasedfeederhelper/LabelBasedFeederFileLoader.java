package com.epam.gepard.datadriven.feeders.labelbasedfeederhelper;
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

import au.com.bytecode.opencsv.CSVReader;
import com.epam.gepard.datadriven.feeders.DataFeederException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;

/**
 * File Loader for Label Based Data Feeder.
 * Handles TXT and CSV files.
 */
public enum LabelBasedFeederFileLoader {

    TXT {
        @Override
        public void loadFeeder(LabelBasedFeederDetails feederDetails) throws DataFeederException, IOException {
            String line;
            String[] columnNames = null; // column names
            int lineNum = 0; // number of lines in the file

            LineNumberReader listReader = new LineNumberReader(new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream(feederDetails.getFeederFile())));
            while (feederDetails.getRowNumber() == 0 || LabelBasedFeederDetails.LabelType.RANDOM.equals(feederDetails.getLabelType())
                    || feederDetails.getParameterList().size() < feederDetails.getRowNumber()) {
                line = listReader.readLine();
                if (line == null) {
                    break; //exit from the loop
                }
                lineNum++;
                line = line.trim();
                if ("".equals(line)) {
                    continue;
                }

                // 1. not empty row is the column names row
                if (columnNames == null) {
                    columnNames = getColumnNamesFromHeaderRow(line, lineNum, feederDetails);
                    // set parameter names to the feeder
                    feederDetails.setParameterNames(Arrays.asList(Arrays.copyOfRange(columnNames, 1, columnNames.length)));
                    continue;
                }

                // skip commented lines or lines that does not contain one of our labels
                if (line.startsWith("//") || line.startsWith("#")
                        || (!feederDetails.getLabels().isEmpty() && !isLabelPresent(line, feederDetails.getLabels(), SEPARATOR_TXT))) {
                    continue;
                }

                //check row for errors and preserve it
                checkRowForErrorsAndPreserveIt(line, columnNames, feederDetails, lineNum);
            }
        }

        private String[] getColumnNamesFromHeaderRow(final String line, final int lineNum, final LabelBasedFeederDetails feederDetails) throws DataFeederException {
            String innerLine = line;
            if (innerLine.startsWith("#")) {
                innerLine = innerLine.substring(1);
            }
            if (innerLine.startsWith("//")) {
                innerLine = innerLine.substring(2);
            }
            String[] columnNames = innerLine.split(SEPARATOR_TXT);
            if (columnNames == null || columnNames.length <= 1) {
                String errorText = "A row must contain at least a label and a value. Line: " + lineNum
                        + ", File:" + feederDetails.getFeederFile();
                throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
            }
            if (!LABEL_COLUMN.equals(columnNames[0])) {
                String errorText = "First row's first column value must match: LABEL. Line: " + lineNum
                        + ", File:" + feederDetails.getFeederFile();
                throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
            }
            return columnNames;
        }

        private void checkRowForErrorsAndPreserveIt(final String line, final String[] columnNames,
                                                    final LabelBasedFeederDetails feederDetails, final int lineNum) throws DataFeederException {
            String[] lineArray; // line separated into columns
            // Check row for errors
            lineArray = line.split(SEPARATOR_TXT);
            if (lineArray == null || lineArray.length <= 1) {
                String errorText = "A row must contain at least a label and a value. Line: " + lineNum
                        + ", File:" + feederDetails.getFeederFile();
                throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
            }
            if (columnNames.length != lineArray.length) {
                String errorText = "Column number does not match in line: " + lineNum
                        + ", File:" + feederDetails.getFeederFile();
                throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
            }

            // set parameter row to the feeder
            feederDetails.addParameterList(Arrays.copyOfRange(lineArray, 1, lineArray.length));
        }

        private boolean isLabelPresent(String line, List<String> labels, String separator) {
            boolean result = false;
            for (String label : labels) {
                if (line.contains(label) && line.indexOf(label) < line.indexOf(separator)) {
                    result = true;
                    break;
                }
            }
            return result;
        }
    },
    CSV {
        @Override
        public void loadFeeder(LabelBasedFeederDetails feederDetails) throws DataFeederException, IOException {
            String[] columnNames = null; // column names
            String[] lineArray; // line in read
            int lineNum = 0; // number of lines in the file

            CSVReader csvReader = new CSVReader(new InputStreamReader(this.getClass().getClassLoader()
                    .getResourceAsStream(feederDetails.getFeederFile())));
            while (feederDetails.getRowNumber() == 0 || LabelBasedFeederDetails.LabelType.RANDOM.equals(feederDetails.getLabelType()) || feederDetails
                    .getParameterList().size() < feederDetails.getRowNumber()) {
                lineArray = csvReader.readNext();
                if (lineArray == null) {
                    break; //exit from the loop
                }
                lineNum++;
                if ((lineArray.length == 1 && lineArray[0].trim().isEmpty())
                        || (lineArray.length >= 1 && (lineArray[0].trim().startsWith("#") || lineArray[0].trim().startsWith("//")))) {
                    continue;
                }

                // 1. not empty row is the column names row
                if (columnNames == null) {
                    columnNames = getColumnNamesFromHeaderRow(lineArray, lineNum, feederDetails);
                    // set parameter names to the feeder
                    feederDetails.setParameterNames(Arrays.asList(Arrays.copyOfRange(columnNames, 1, columnNames.length)));
                    continue;
                }

                // skip commented lines or lines that does not contain one of our labels
                if (!feederDetails.getLabels().isEmpty() && !isLabelPresent(getLabels(lineArray[0]), feederDetails.getLabels())) {
                    continue;
                }

                // Check row for errors
                if (lineArray.length <= 1) {
                    String errorText = "A row must contain at least a label and a value. Line: " + lineNum
                            + ", File:" + feederDetails.getFeederFile();
                    throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
                }
                if (columnNames.length != lineArray.length) {
                    String errorText = "Column number does not match in line: " + lineNum
                            + ", File:" + feederDetails.getFeederFile();
                    throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
                }

                // set parameter row to the feeder
                feederDetails.addParameterList(Arrays.copyOfRange(lineArray, 1, lineArray.length));
            }
        }

        private String[] getColumnNamesFromHeaderRow(final String[] lineArray, final int lineNum, final LabelBasedFeederDetails feederDetails) throws DataFeederException {
            if (lineArray.length <= 1) {
                String errorText = "A row must contain at least a label and a value. Line: " + lineNum
                        + ", File:" + feederDetails.getFeederFile();
                throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
            }
            if (!LABEL_COLUMN.equals(lineArray[0])) {
                String errorText = "First row's first column value must match: LABEL. Line: " + lineNum
                        + ", File:" + feederDetails.getFeederFile();
                throw new DataFeederException(errorText, ERROR_FEEDER_FILE_CONTENT_ERROR);
            }
            return lineArray;
        }

        private boolean isLabelPresent(List<String> lineLabels, List<String> appliedLabels) {
            boolean retval = false;

            for (String label : appliedLabels) {
                if (lineLabels.containsAll(Arrays.asList(label.split("&")))) {
                    retval = true;
                    break;
                }
            }

            return retval;
        }

        private List<String> getLabels(String labelValue) {
            return Arrays.asList(labelValue.split(LABEL_SEPARATOR));
        }
    };

    private static final String LABEL_COLUMN = "LABEL"; // every feeder must contain a column named LABEL, plus at least one data columns
    private static final String LABEL_SEPARATOR = ";";
    private static final String SEPARATOR_TXT = "%";
    private static final int ERROR_FEEDER_FILE_CONTENT_ERROR = -20;


    /**
     * Load feeder.
     *
     * @param feederDetails the feeder
     * @throws DataFeederException when there is a problem with the loaded data
     * @throws IOException         when there is a problem with reading info from the disk
     */
    public abstract void loadFeeder(LabelBasedFeederDetails feederDetails) throws DataFeederException, IOException;

}
