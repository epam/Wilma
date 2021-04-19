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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.epam.gepard.datadriven.DataDrivenParameterArray;
import com.epam.gepard.datadriven.feeders.SimpleMultiplierDataFeeder.ConfigFileInfo;


/**
 * Provides the ability of loading a data driven source file.
 * @author Tibor_Kovacs
 */
public class SimpleMultiplierDataFeederFileLoader {

    /**
     * Creates a {@link DataDrivenParameterArray} object from a data driven source file.
     * @param fileInfo is the information container about configuration file.
     * @param multiplier to shows how much rows needed.
     * @return with a new {@link DataDrivenParameterArray}
     * @throws IOException because of file reading
     */
    public DataDrivenParameterArray loadFile(final ConfigFileInfo fileInfo, final int multiplier) throws IOException {
        DataDrivenParameterArray myArray = new DataDrivenParameterArray();
        //have configuration file for the specific class
        LineNumberReader listReader = new LineNumberReader(new InputStreamReader(new FileInputStream(fileInfo.getConfigFilename()), "UTF-8"));
        String headerLine = null; //to hold header line in case of CSV file
        String line; //= null, to hold the line actually loaded
        Integer counter = 0; //this is the key in the map
        String[] rowData; //will hold a data row
        String[] columnNames = null;
        while ((line = listReader.readLine()) != null) {
            line = line.trim();
            if (("".equals(line) || line.startsWith("//") || line.startsWith("#")) && !fileInfo.isCSV()) {
                continue;
            }
            //handling csv header, "if" ensures that it will be called for the first line only...
            if (fileInfo.isCSV() && (headerLine == null)) {
                //ignore first line in CSV files
                headerLine = line;
                columnNames = headerLine.split(fileInfo.getSplitter());
            } else {
                rowData = line.split(fileInfo.getSplitter());
                myArray.put(counter, rowData);
                counter++;
            }
            if (counter == multiplier) {
                break; //enough row is loaded, do not continue the load of the file
            }
        } //file is loaded
        listReader.close();
        //columnNames is either filled, or empty and waiting for generation
        if (columnNames != null) {
            myArray.setParameterNames(columnNames);
        } else {
            myArray.setParameterNames(DataDrivenParameterArray.generateNames(myArray));
        }
        return myArray;
    }
}
