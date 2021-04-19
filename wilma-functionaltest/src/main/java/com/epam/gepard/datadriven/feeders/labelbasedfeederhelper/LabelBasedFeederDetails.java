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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for LabelBasedDataFeeder class. Contains information about a feeder that is in use
 * in the parameter part of the LabelBasedDataFeeder.
 */
public class LabelBasedFeederDetails {
    private static final int INITIAL_HASH_VALUE = 1;
    private static final int HASH_MULTIPLIER = 31;

    private String feederFile;
    private int rowNumber; // = 0; // if 0, then load all rows
    private List<String> labels = new ArrayList<>();
    private LabelType labelType = LabelType.SEQUENTIAL;
    private LabelBasedFeederRelation feederRelation = LabelBasedFeederRelation.MASTER;

    private List<String> parameterNames = new ArrayList<>(); //names of the parameter columns
    private List<String[]> parameterList = new ArrayList<>();

    /**
     * Default constructor, sets the feeder file.
     *
     * @param feederFile the first parameter in the feeder descriptor.
     */
    public LabelBasedFeederDetails(String feederFile) {
        this.feederFile = feederFile;
    }

    public String getFeederFile() {
        return feederFile;
    }

    public void setFeederFile(String feederFile) {
        this.feederFile = feederFile;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Sets the number of rows to be loaded by the feeder.
     *
     * @param rowNumber is the number of rows.
     */
    public void setRowNumber(int rowNumber) {
        if (rowNumber >= 1) {
            this.rowNumber = rowNumber;
        }
    }

    public List<String> getLabels() {
        return labels;
    }

    /**
     * Sets the labels to be used by the specific label.
     *
     * @param labels is the label info, a ";" separated list of labels
     */
    public void setLabels(String labels) {
        if (StringUtils.isNotBlank(labels)) {
            this.labels.addAll(Arrays.asList(labels.split(";")));
        }
    }

    public LabelType getLabelType() {
        return labelType;
    }

    public void setLabelType(LabelType labelType) {
        this.labelType = labelType;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public List<String[]> getParameterList() {
        return parameterList;
    }

    /**
     * Add a new row to the data array.
     *
     * @param parameterList is the row to be added.
     */
    public void addParameterList(String[] parameterList) {
        this.parameterList.add(parameterList);
    }

    public LabelBasedFeederRelation getFeederRelation() {
        return feederRelation;
    }

    /**
     * Sets the feeder relation between the feeders.
     * @param feederRelation is the detected relation to be set.
     */
    public void setFeederRelation(LabelBasedFeederRelation feederRelation) {
        this.feederRelation = feederRelation;
    }

    /**
     * Reduce parameter list size if greater than rowNumber.
     */
    public void reduceParameterList() {
        if (rowNumber > 0 && parameterList.size() > rowNumber) {
            parameterList = parameterList.subList(0, rowNumber);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INITIAL_HASH_VALUE, HASH_MULTIPLIER).append(feederFile).append(rowNumber).
                append(labelType).append(feederRelation).append(parameterNames).append(parameterList).toHashCode();
    }


    /**
     * Defines the way to load labeled data rows.
     * SEQUENTIAL: sequentially load one (or custom number) from the matching rows.
     * RANDOM: one (or custom number) random row from the matching rows.
     * MERGE: load all from matching rows.
     */
    public static enum LabelType {
        SEQUENTIAL,
        RANDOM,
        MERGE
    }

}
