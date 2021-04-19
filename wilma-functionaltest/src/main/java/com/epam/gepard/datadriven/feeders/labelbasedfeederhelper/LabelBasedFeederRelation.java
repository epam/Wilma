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

import com.epam.gepard.datadriven.DataDrivenParameterArray;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum FeederRelation. Describes relationship among feeders, how they connect to each other.
 */
public enum LabelBasedFeederRelation {
    MASTER("0") {
        @Override
        public void addFeeder(LabelBasedFeederDetails feeder, DataDrivenParameterArray parameters) {
            for (int i = 0; i < feeder.getParameterList().size(); i++) {
                parameters.put(i, feeder.getParameterList().get(i));
            }
        }

        @Override
        public int calculateRuns(int currentNumber, int addedByFeeder) {
            return addedByFeeder;
        }
    },
    PLUS("+") {
        @Override
        public void addFeeder(LabelBasedFeederDetails feeder, DataDrivenParameterArray parameters) {
            String[] rowToAppend;
            for (Map.Entry<Integer, String[]> entry : parameters.entrySet()) {
                rowToAppend = feeder.getParameterList().get(entry.getKey() % feeder.getParameterList().size());
                entry.setValue(ArrayUtils.addAll(entry.getValue(), rowToAppend));
            }
        }

        @Override
        public int calculateRuns(int currentNumber, int addedByFeeder) {
            return currentNumber;
        }
    },
    MULTIPLY("x") {
        @Override
        public void addFeeder(LabelBasedFeederDetails feeder, DataDrivenParameterArray parameters) {
            // Multiply rows first
            Map<Integer, String[]> paramMap = new HashMap<>();
            for (int i = 0; i < parameters.keySet().size(); i++) {
                for (int j = 0; j < feeder.getParameterList().size(); j++) {
                    paramMap.put((i * feeder.getParameterList().size()) + j, parameters.get(i));
                }
            }
            parameters.setParamMap(paramMap);
            // Add parameters
            PLUS.addFeeder(feeder, parameters);
        }

        @Override
        public int calculateRuns(int currentNumber, int addedByFeeder) {
            return currentNumber * addedByFeeder;
        }
    };

    private static final Map<String, LabelBasedFeederRelation> STRING_TO_ENUM = new HashMap<>();
    private String sign;

    static {
        for (LabelBasedFeederRelation r : values()) {
            STRING_TO_ENUM.put(r.toString(), r);
        }
    }

    LabelBasedFeederRelation(String sign) {
        this.sign = sign;
    }

    /**
     * Transfer string value to feeder relation enumerator.
     *
     * @param value to be transferred.
     * @return with the enumerated value.
     */
    public static LabelBasedFeederRelation getByValue(String value) {
        return STRING_TO_ENUM.get(value);
    }

    @Override
    public String toString() {
        return sign;
    }

    /**
     * Adds the feeder data to the DataDrivenParameterArray.
     *
     * @param feeder     the feeder
     * @param parameters the DataDrivenParameterArray which will be returned to the framework and determinate test parameters
     */
    public abstract void addFeeder(LabelBasedFeederDetails feeder, DataDrivenParameterArray parameters);

    /**
     * Calculate run number from the current number and the number of rows given by the feeder.
     *
     * @param currentNumber the current number
     * @param addedByFeeder the added by feeder
     * @return the int
     */
    public abstract int calculateRuns(int currentNumber, int addedByFeeder);

}

