package com.epam.wilma.service.unit;
/*==========================================================================
 Copyright 2013-2016 EPAM Systems

 This file is part of Wilma.

 Wilma is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Wilma is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
 ===========================================================================*/

import com.epam.wilma.service.unit.helper.ConfigurationParameter;
import com.epam.wilma.service.unit.request.RequestCondition;

/**
 * A Request Condition class builder.
 *
 * @author Tamas_Kohegyi
 *
 */
public class RequestConditionBuilder {

    private String configurationString = "";

    public RequestConditionBuilder andStart() {
        configurationString += "<and>\n";
        return this;
    }

    public RequestConditionBuilder andEnd() {
        configurationString += "</and>\n";
        return this;
    }

    public RequestConditionBuilder orStart() {
        configurationString += "<or>\n";
        return this;
    }

    public RequestConditionBuilder orEnd() {
        configurationString += "</or>\n";
        return this;
    }

    public RequestConditionBuilder notStart() {
        configurationString += "<not>\n";
        return this;
    }

    public RequestConditionBuilder notEnd() {
        configurationString += "</not>\n";
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     * @param className is the condition class
     * @param configurationParameters is the parameter array
     * @param negate if true, then the condition should be negated
     * @return with itself
     */
    private RequestConditionBuilder condition(String className, ConfigurationParameter[] configurationParameters, boolean negate) {
        String conditionString = "<condition class=\"" + className + "\" ";
        if (negate) {
            conditionString += "negate=true ";
        }
        conditionString += ">\n";
        if (configurationParameters != null) {
            //we have parameters too
            for (ConfigurationParameter configurationParameter : configurationParameters) {
                conditionString += configurationParameter.toString() + "\n";
            }
        }
        conditionString += "</condition>\n";

        configurationString += conditionString;
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     * @param className is the condition class
     * @param configurationParameters is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder condition(String className, ConfigurationParameter[] configurationParameters) {
        return condition(className, configurationParameters, false);
    }

    /**
     * General purpose condition class usage, with a parameter array.
     * @param className is the condition class
     * @param configurationParameters is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder negatedCondition(String className, ConfigurationParameter[] configurationParameters) {
        return condition(className, configurationParameters, true);
    }

    /**
     * General purpose condition class usage, without parameter array.
     * @param className is the condition class
     * @return with itself
     */
    public RequestConditionBuilder condition(String className) {
        return condition(className, null, false);
    }

    /**
     * General purpose condition class usage, without parameter array, and the result is negated.
     * @param className is the condition class
     * @return with itself
     */
    public RequestConditionBuilder negatedCondition(String className) {
        return condition(className, null, true);
    }

    public RequestConditionBuilder comingFrom(String localhost) {
        return this;
    }

    public ResponseDescriptorBuilder willResponseWith() {
        return new ResponseDescriptorBuilder(build());
    }

    public RequestCondition build() {
        return new RequestCondition(configurationString);
    }

    public RequestConditionBuilder withHeader(String name, String value) {
        String conditionString = "<condition class=\"HeaderParameterChecker\">\n" +
                "    <param name=\"" + name + "\" value=\"" + value + "\" />\n" +
                "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    public RequestConditionBuilder withHeader(String pattern) {
        String conditionString = "<condition class=\"AndHeaderPatternChecker\">\n" +
                "    <param name=\"dummy\" value=\"" + pattern + "\" />\n" +
                "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    public RequestConditionBuilder textInUrl(String textInUrl) {
        return this;
    }

}
