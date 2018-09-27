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

import com.epam.wilma.service.configuration.stub.helper.common.StubConfigurationException;
import com.epam.wilma.service.configuration.stub.helper.common.StubConfigurationValidator;
import com.epam.wilma.service.configuration.stub.request.RequestCondition;
import com.epam.wilma.service.configuration.stub.response.ResponseDescriptor;

import java.util.Formatter;

/**
 * Class that represents a stubbed request-response pairs.
 * Example configuration:
 * <p>
 *
 * {
 *   "wilmaStubConfiguration": {
 *     "dialogDescriptors": [
 *       {
 *         "name": "dialog-descriptor1",
 *         "usage": "always",
 *         "comment": "random comment",
 *         "conditionDescriptor": {
 *           "condition": {
 *             "class": "OrPatternChecker",
 *             "parameters": [
 *               {
 *                 "name": "MSG_ID",
 *                 "value": "exampleID=\"456\""
 *               }
 *             ]
 *           }
 *         },
 *         "responseDescriptor": {
 *           "code": 200,
 *           "delay": 0,
 *           "mimeType": "application/xml",
 *           "templateName": "exampleTemplate1"
 *         }
 *       }
 *     ],
 *     "templates": [
 *       {
 *         "name": "exampleTemplate1",
 *         "type": "xmlFile",
 *         "resource": "example4.xml"
 *       }
 *     ]
 *   }
 * }
 *
 * @author Tamas_Kohegyi
 */
public class WilmaStub {

    private RequestCondition requestCondition;
    private ResponseDescriptor responseDescriptor;
    private String groupName;

    /**
     * Constructor of a Stub Configuration.
     *
     * @param groupName          is the group name the stub configuration belongs to
     * @param requestCondition   defines the condition(s) for the request
     * @param responseDescriptor defines the response to be sent back
     * @throws StubConfigurationException if the stub configuration structure is invalid
     */
    public WilmaStub(String groupName, RequestCondition requestCondition, ResponseDescriptor responseDescriptor) {
        this.requestCondition = requestCondition;
        this.responseDescriptor = responseDescriptor;
        this.groupName = groupName;
        //need to validate both the request condition, and the response descriptor
        validateConfiguration();
    }

    /**
     * Produces a WilmaStub configuration XML.
     *
     * @return xml content
     */
    @Override
    public String toString() {
        String generatedName = "generated name";
        String conditionContent = requestCondition.toString();
        String responseContent = responseDescriptor.responseDescriptorToString();
        String usedTemplate = responseDescriptor.templateToString();
        String interceptorDescriptor = responseDescriptor.interceptorsToString();
        StringBuilder sb = new StringBuilder();

        try (Formatter formatter = new Formatter(sb)) {
            String stubConfigurationFormatterString = "{ \"wilmaStubConfiguration\": { \n"
                    + " \"groupName\": \"%5$s\", \n"
                    + " \"dialogDescriptors\": [{ \"name\": \"%1$s\", \"usage\": \"always\", \"comment\": \"%1$s\","
                    + "\"conditionDescriptor\": %2$s, \"responseDescriptor\": %3$s"
                    + "}]," //dialogDescriptors end
                    + "\n \"templates\": [ %4$s ],"
                    + " %6$s \n"
                    + "} }";
            formatter.format(stubConfigurationFormatterString, generatedName, conditionContent, responseContent,
                    usedTemplate, groupName, interceptorDescriptor);
        }
        return sb.toString();
    }

    /**
     * Validates the actual Stub Configuration against Stub Configuration XSD.
     * If everything is ok (the structure is ok), simply returns.
     * Beware that it cannot check if the referenced (condition checker, template formatter, etc) classes
     * or the used template files are available or not.
     *
     * @throws StubConfigurationException if the structure is not acceptable
     */
    public void validateConfiguration() throws StubConfigurationException {
        StubConfigurationValidator validator = new StubConfigurationValidator();
        validator.validate(toString());
    }

    /**
     * Gets the actual value of the stub configuration group.
     *
     * @return with its configuration group name
     */
    public String getGroupName() {
        return groupName;
    }


}
