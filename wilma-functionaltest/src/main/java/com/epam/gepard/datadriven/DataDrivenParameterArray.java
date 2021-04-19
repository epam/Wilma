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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class contains a full data array for a Test Class that is executed many times with different data.
 * Contains a String[] that holds the names of the data columns (default value: "PARx", x = 0,1...
 * Also contains a @LinkedHashMap where the key is the row number, value is the String[] of data parameters.
 */
public class DataDrivenParameterArray {
    private String[] parameterNames; //names of the parameter columns
    private Map<Integer, String[]> paramMap = new LinkedHashMap<>();

    /**
     * Generates the parameter column names, in case no names are available.
     * The generated names will be PAR0, PAR1, ...  PARn
     *
     * @param mapHolder is the DataDrivenParameterArray class waiting for newly generated parameter names
     * @return with the generated and suggested parameter names.
     */
    public static String[] generateNames(final DataDrivenParameterArray mapHolder) {
        int length = 0;
        if (mapHolder.paramMap.values().size() > 0) {
            length = mapHolder.paramMap.values().iterator().next().length;
        }
        String[] parameterNames = new String[length];
        for (int i = 0; i < length; i++) {
            parameterNames[i] = "PAR" + i;
        }
        return parameterNames;
    }

    /**
     * This helper method can be used to concatenate two string array.
     * You may use it in Data Feeders, do not use elsewhere in Gepard.
     * (not thread safe, and Gepard knows it)
     *
     * @param arrayA is one string array
     * @param arrayB is another string array
     * @return with the concatenated string array
     */
    public static String[] concat(final String[] arrayA, final String[] arrayB) {
        String[] arrayC = new String[arrayA.length + arrayB.length];
        System.arraycopy(arrayA, 0, arrayC, 0, arrayA.length);
        System.arraycopy(arrayB, 0, arrayC, arrayA.length, arrayB.length);
        return arrayC;
    }

    /**
     * Gets the parameter names (= column names).
     *
     * @return with the parameter names.
     */
    public String[] getParameterNames() {
        return parameterNames;
    }

    /**
     * Set the parameter names (= column names).
     *
     * @param parameterNames are the parameter names.
     */
    public void setParameterNames(final String[] parameterNames) {
        this.parameterNames = parameterNames;
    }

    /**
     * Gets a row from the parameter array.
     *
     * @param key specifies the row (as key).
     * @return with String[] of parameters.
     */
    public String[] get(final Integer key) {
        return paramMap.get(key);
    }

    /**
     * Adds a new row to the parameter array, with a given key.
     * @param i is the key to be used.
     * @param newRow is the String[] of data values.
     */
    public void put(final Integer i, final String[] newRow) {
        paramMap.put(i, newRow);
    }

    /**
     * Get collection of values of the data array.
     *
     * @return with the collection.
     */
    public Collection<String[]> values() {
        return paramMap.values();
    }

    /**
     * Get collection of keys of the data array.
     *
     * @return with the collection.
     */
    public Set<Integer> keySet() {
        return paramMap.keySet();
    }

    /**
     * Is the parameter array empty?
     *
     * @return with true if it is empty, otherwise false
     */
    public boolean isEmpty() {
        return paramMap.isEmpty();
    }

    /**
     * Get the size (= number of rows) of the parameter array.
     *
     * @return with its size.
     */
    public int size() {
        return paramMap.size();
    }

    /**
     * Get full entry set (= whole data array).
     *
     * @return with the set of entries.
     */
    public Set<Map.Entry<Integer, String[]>> entrySet() {
        return paramMap.entrySet();
    }

    public void setParamMap(final Map<Integer, String[]> paramMap) {
        this.paramMap = paramMap;
    }

}
