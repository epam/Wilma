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

import com.epam.wilma.service.unit.helper.ConditionParameter;
import com.epam.wilma.service.unit.request.RequestCondition;

/**
 * A Request Condition class builder.
 *
 * @author Tamas_Kohegyi
 *
 */
public class RequestConditionBuilder {

    private String configuartionString = "";

    public RequestConditionBuilder andStart() {
        return this;
    }

    public RequestConditionBuilder andEnd() {
        return this;
    }

    public RequestConditionBuilder orStart() {
        return this;
    }

    public RequestConditionBuilder orEnd() {
        return this;
    }

    public RequestConditionBuilder not() {
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     * @param className is the condition class
     * @param conditionParameters is the parameter array
     * @param negate if true, then the condition should be negated
     * @return with itself
     */
    private RequestConditionBuilder condition(String className, ConditionParameter[] conditionParameters, boolean negate) {
        String conditionString = "<condition class=\"" + className + "\" ";
        if (negate) {
            conditionString += "negate=true ";
        }
        conditionString += ">\n";
        if (conditionParameters != null) {
            //we have parameters too
            for (ConditionParameter conditionParameter : conditionParameters) {
                conditionString += conditionParameter.toString() + "\n";
            }
        }
        conditionString += "</condition>";

        configuartionString += conditionString;
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     * @param className is the condition class
     * @param conditionParameters is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder condition(String className, ConditionParameter[] conditionParameters) {
        return condition(className, conditionParameters, false);
    }

    /**
     * General purpose condition class usage, with a parameter array.
     * @param className is the condition class
     * @param conditionParameters is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder negatedCondition(String className, ConditionParameter[] conditionParameters) {
        return condition(className, conditionParameters, true);
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
        return new RequestCondition(configuartionString);
    }

    public RequestConditionBuilder withHeader(String blah) {
        return this;
    }

    public RequestConditionBuilder textInUrl(String textInUrl) {
        return this;
    }

}
