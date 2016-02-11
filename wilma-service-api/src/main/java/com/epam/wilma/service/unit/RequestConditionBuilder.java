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

import com.epam.wilma.service.unit.helper.common.ConfigurationParameter;
import com.epam.wilma.service.unit.request.RequestCondition;

/**
 * A Request Condition class builder.
 *
 * @author Tamas_Kohegyi
 */
public class RequestConditionBuilder {

    private String configurationString = "";
    private String groupName;

    /**
     * Initiates a builder class to build up the request condition part of the configuration.
     * @param groupName is the group name of the stub configuration
     */
    public RequestConditionBuilder(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Opens an &lt;and&gt; ... &lt;/and&gt; section, do not forget to call the @see andEnd() method to close it.
     * @return with itself
     */
    public RequestConditionBuilder andStart() {
        configurationString += "<and>\n";
        return this;
    }

    /**
     * Closes an &lt;and&gt; ... &lt;/and&gt; section, that was previously opened with @see andStart().
     * @return with itself
     */
    public RequestConditionBuilder andEnd() {
        configurationString += "</and>\n";
        return this;
    }

    /**
     * Opens an &lt;or&gt; ... &lt;/or&gt; section, do not forget to call the @see orEnd() method to close it.
     * @return with itself
     */
    public RequestConditionBuilder orStart() {
        configurationString += "<or>\n";
        return this;
    }

    /**
     * Closes an &lt;or&gt; ... &lt;/or&gt; section, that was previously opened with @see orStart().
     * @return with itself
     */
    public RequestConditionBuilder orEnd() {
        configurationString += "</or>\n";
        return this;
    }

    /**
     * Opens an &lt;not&gt; ... &lt;/not&gt; section, do not forget to call the @see notEnd() method to close it.
     * @return with itself
     */
    public RequestConditionBuilder notStart() {
        configurationString += "<not>\n";
        return this;
    }

    /**
     * Closes an &lt;not&gt; ... &lt;/not&gt; section, that was previously opened with @see notStart().
     * @return with itself
     */
    public RequestConditionBuilder notEnd() {
        configurationString += "</not>\n";
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     *
     * @param className               is the condition class
     * @param configurationParameters is the parameter array
     * @param negate                  if true, then the condition should be negated
     * @return with itself
     */
    private RequestConditionBuilder condition(String className, ConfigurationParameter[] configurationParameters, boolean negate) {
        String conditionString = "<condition class=\"" + className + "\" ";
        if (negate) {
            conditionString += "negate=\"true\" ";
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
     *
     * @param className               is the condition class
     * @param configurationParameters is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder condition(String className, ConfigurationParameter[] configurationParameters) {
        return condition(className, configurationParameters, false);
    }

    /**
     * General purpose condition class usage, with a parameter array.
     *
     * @param className               is the condition class
     * @param configurationParameters is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder negatedCondition(String className, ConfigurationParameter[] configurationParameters) {
        return condition(className, configurationParameters, true);
    }

    /**
     * General purpose condition class usage, without parameter array.
     *
     * @param className is the condition class
     * @return with itself
     */
    public RequestConditionBuilder condition(String className) {
        return condition(className, null, false);
    }

    /**
     * General purpose condition class usage, without parameter array, and the result is negated.
     *
     * @param className is the condition class
     * @return with itself
     */
    public RequestConditionBuilder negatedCondition(String className) {
        return condition(className, null, true);
    }

    /**
     * Adds a condition that checks if the request is coming from a specific host.
     * Actually it is a pattern checker in the URL, and adds "//" to the from of the hostName,
     * so serarches for existence of "//" + hostName pattern in the URL.
     *
     * @param hostName is the host name
     * @return with itself
     */
    public RequestConditionBuilder comingFrom(String hostName) {
        String conditionString = "<condition class=\"AndUrlPatternChecker\">\n"
                + "    <param name=\"irrelevant\" value=\"//" + hostName + "\" />\n"
                + "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    /**
     * Adds a condition that checks if a header with the specified value exists or not.
     * @param name is the name of the header
     * @param value is the value of the header
     * @return with itself
     */
    public RequestConditionBuilder withHeader(String name, String value) {
        String conditionString = "<condition class=\"HeaderParameterChecker\">\n"
                + "    <param name=\"" + name + "\" value=\"" + value + "\" />\n"
                + "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    /**
     * Add a condition that checks if a text pattern exist in the header.
     *
     * @param pattern is the string pattern
     * @return with itself
     */
    public RequestConditionBuilder withTextInHeader(String pattern) {
        String conditionString = "<condition class=\"AndHeaderPatternChecker\">\n"
                + "    <param name=\"irrelevant\" value=\"" + pattern + "\" />\n"
                + "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    /**
     * Add a condition that checks if a text pattern exist in the body.
     *
     * @param pattern is the string pattern
     * @return with itself
     */
    public RequestConditionBuilder withTextInBody(String pattern) {
        String conditionString = "<condition class=\"AndBodyPatternChecker\">\n"
                + "    <param name=\"irrelevant\" value=\"" + pattern + "\" />\n"
                + "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    /**
     * Add a condition that checks if a text pattern exist in the URL.
     *
     * @param textInUrl is the string pattern
     * @return with itself
     */
    public RequestConditionBuilder textInUrl(String textInUrl) {
        String conditionString = "<condition class=\"AndUrlPatternChecker\">\n"
                + "    <param name=\"irrelevant\" value=\"" + textInUrl + "\" />\n"
                + "</condition>\n";
        configurationString += conditionString;
        return this;
    }

    /**
     * Transponder class to finalize the Request Condition part and start the Response Descriptor.
     * @return with a class that used to build the response descriptor
     */
    public ResponseDescriptorBuilder willRespondWith() {
        return new ResponseDescriptorBuilder(groupName, build());
    }

    /**
     * Builds the Request Condition class, with the already specified request descriptor.
     *
     * @return with the created Request Condition class.
     */
    public RequestCondition build() {
        return new RequestCondition(configurationString);
    }

}
