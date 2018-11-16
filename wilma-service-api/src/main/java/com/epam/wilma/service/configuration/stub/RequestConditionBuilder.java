package com.epam.wilma.service.configuration.stub;
/*==========================================================================
 Copyright since 2013, EPAM Systems

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

import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameterArray;
import com.epam.wilma.service.configuration.stub.request.RequestCondition;

/**
 * A Request Condition class builder.
 *
 * @author Tamas_Kohegyi
 */
public class RequestConditionBuilder {

    private String configurationString = "";
    private String groupName;
    private boolean needComma;

    /**
     * Initiates a builder class to build up the request condition part of the configuration.
     *
     * @param groupName is the group name of the stub configuration
     */
    public RequestConditionBuilder(String groupName) {
        this.groupName = groupName;
    }

    private void handleComma() {
        if (needComma) {
            configurationString += ",\n  ";
        }
        needComma = false;
    }

    /**
     * Opens an "and" : [ ... ] section, do not forget to call the @see andEnd() method to close it.
     *
     * @return with itself
     */
    public RequestConditionBuilder andStart() {
        handleComma();
        configurationString += "{ \"and\": [\n  ";
        needComma = false;
        return this;
    }

    /**
     * Closes an "and" : [ ... ] section, that was previously opened with @see andStart().
     *
     * @return with itself
     */
    public RequestConditionBuilder andEnd() {
        configurationString += "\n ] }";
        needComma = true;
        return this;
    }

    /**
     * Opens an "or" : [ ... ] section, do not forget to call the @see orEnd() method to close it.
     *
     * @return with itself
     */
    public RequestConditionBuilder orStart() {
        handleComma();
        configurationString += "{ \"or\": [\n  ";
        needComma = false;
        return this;
    }

    /**
     * Closes an "or" : [ ... ] section, that was previously opened with @see orStart().
     *
     * @return with itself
     */
    public RequestConditionBuilder orEnd() {
        configurationString += "\n ] }";
        needComma = true;
        return this;
    }

    /**
     * Opens an "or" : { ... } section, do not forget to call the @see notEnd() method to close it.
     *
     * @return with itself
     */
    public RequestConditionBuilder notStart() {
        handleComma();
        configurationString += "{ \"not\": \n  ";
        needComma = false;
        return this;
    }

    /**
     * Closes an "or" : { ... } section, that was previously opened with @see notStart().
     *
     * @return with itself
     */
    public RequestConditionBuilder notEnd() {
        configurationString += "\n }";
        needComma = true;
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     *
     * @param className                   is the condition class
     * @param configurationParameterArray is the parameter array
     * @param negate                      if true, then the condition should be negated
     * @return with itself
     */
    private RequestConditionBuilder condition(String className, ConfigurationParameterArray configurationParameterArray, boolean negate) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"" + className + "\" ";
        if (negate) {
            conditionString += ", \"negate\": true ";
        }
        if (configurationParameterArray != null) {
            //we have parameters too
            conditionString += ",\n " + configurationParameterArray.toString();
        }
        conditionString += "\n  }\n }";

        configurationString += conditionString;
        needComma = true;
        return this;
    }

    /**
     * General purpose condition class usage, with a parameter array.
     *
     * @param className                   is the condition class
     * @param configurationParameterArray is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder condition(String className, ConfigurationParameterArray configurationParameterArray) {
        return condition(className, configurationParameterArray, false);
    }

    /**
     * General purpose condition class usage, with a parameter array.
     *
     * @param className                   is the condition class
     * @param configurationParameterArray is the parameter array
     * @return with itself
     */
    public RequestConditionBuilder negatedCondition(String className, ConfigurationParameterArray configurationParameterArray) {
        return condition(className, configurationParameterArray, true);
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
     * so searches for existence of "//" + hostName pattern in the URL.
     *
     * @param hostName is the host name
     * @return with itself
     */
    public RequestConditionBuilder comingFrom(String hostName) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"AndUrlPatternChecker\",\n"
                + "    \"parameters\": [{\"name\": \"irrelevant\", \"value\": \"//" + hostName + "\" }]"
                + "}\n}\n";
        configurationString += conditionString;
        needComma = true;
        return this;
    }

    private RequestConditionBuilder withMethod(final String methodName) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"" + methodName + "MethodChecker\" }}";
        configurationString += conditionString;
        needComma = true;
        return this;
    }

    /**
     * Adds a condition that checks if GET method was used in the request, or not.
     *
     * @return with itself
     */
    public RequestConditionBuilder getMethod() {
        return withMethod("Get");
    }

    /**
     * Adds a condition that checks if PUT method was used in the request, or not.
     *
     * @return with itself
     */
    public RequestConditionBuilder putMethod() {
        return withMethod("Put");
    }

    /**
     * Adds a condition that checks if POST method was used in the request, or not.
     *
     * @return with itself
     */
    public RequestConditionBuilder postMethod() {
        return withMethod("Post");
    }

    /**
     * Adds a condition that checks if DELETE method was used in the request, or not.
     *
     * @return with itself
     */
    public RequestConditionBuilder deleteMethod() {
        return withMethod("Delete");
    }

    /**
     * Adds a condition that checks if HEAD method was used in the request, or not.
     *
     * @return with itself
     */
    public RequestConditionBuilder headMethod() {
        return withMethod("Head");
    }

    /**
     * Adds a condition that checks if OPTIONS method was used in the request, or not.
     *
     * @return with itself
     */
    public RequestConditionBuilder optionsMethod() {
        return withMethod("Options");
    }

    /**
     * Adds a condition that checks if a header with the specified value exists or not.
     *
     * @param name  is the name of the header
     * @param value is the value of the header
     * @return with itself
     */
    public RequestConditionBuilder withHeader(String name, String value) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"HeaderParameterChecker\","
                + "   \"parameters\": [{ \"name\": \"" + name + "\", \"value\": \"" + value + "\"}] }}";
        configurationString += conditionString;
        needComma = true;
        return this;
    }

    /**
     * Add a condition that checks if a text pattern exist in the header.
     *
     * @param pattern is the string pattern
     * @return with itself
     */
    public RequestConditionBuilder withTextInHeader(String pattern) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"AndHeaderPatternChecker\","
                + "   \"parameters\": [{ \"name\": \"irrelevant\", \"value\": \"" + pattern + "\"}] }}";
        configurationString += conditionString;
        needComma = true;
        return this;
    }

    /**
     * Add a condition that checks if a text pattern exist in the body.
     *
     * @param pattern is the string pattern
     * @return with itself
     */
    public RequestConditionBuilder withTextInBody(String pattern) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"AndBodyPatternChecker\","
                + "   \"parameters\": [{ \"name\": \"irrelevant\", \"value\": \"" + pattern + "\"}] }}";
        configurationString += conditionString;
        needComma = true;
        return this;
    }

    /**
     * Add a condition that checks if a text pattern exist in the URL.
     *
     * @param textInUrl is the string pattern
     * @return with itself
     */
    public RequestConditionBuilder textInUrl(String textInUrl) {
        handleComma();
        String conditionString = "{ \"condition\": { \"class\": \"AndUrlPatternChecker\","
                + "   \"parameters\": [{ \"name\": \"irrelevant\", \"value\": \"" + textInUrl + "\"}] }}";
        configurationString += conditionString;
        needComma = true;
        return this;
    }

    /**
     * Transponder class to finalize the Request Condition part and start the Response Descriptor.
     *
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
