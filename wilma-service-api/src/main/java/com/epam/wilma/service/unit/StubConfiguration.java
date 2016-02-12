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

import com.epam.wilma.service.unit.helper.common.StubConfigurationException;
import com.epam.wilma.service.unit.helper.common.StubConfigurationValidator;
import com.epam.wilma.service.unit.request.RequestCondition;
import com.epam.wilma.service.unit.response.ResponseDescriptor;

import java.util.Formatter;

/**
 * Class that represents a stubbed request-response pairs.
 * Example configuration:
 * <p>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;wilma-stub xmlns="http://epam.github.io/Wilma/xsd/StubConfig" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xsi:schemaLocation="http://epam.github.io/Wilma/xsd/StubConfig http://epam.github.io/Wilma/xsd/StubConfig.xsd"&gt;
 * &lt;dialog-descriptor name="dummy-descriptor" usage="always" comment="random comment"&gt;
 * &lt;condition-descriptor&gt;
 * &lt;condition class="AlwaysFalseChecker" /&gt;
 * &lt;/condition-descriptor>
 * &lt;response-descriptor code="502" delay="0" mimetype="text/plain" template="errorResponse" /&gt;
 * &lt;/dialog-descriptor&gt;
 * &lt;template-descriptor name="template-descriptor_1"&gt;
 * &lt;template name="errorResponse" type="text" resource="Bad Gateway" /&gt;
 * &lt;/template-descriptor&gt;
 * &lt;/wilma-stub&gt;
 *
 * @author Tamas_Kohegyi
 */
public class StubConfiguration {

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
    public StubConfiguration(String groupName, RequestCondition requestCondition, ResponseDescriptor responseDescriptor) throws StubConfigurationException {
        this.requestCondition = requestCondition;
        this.responseDescriptor = responseDescriptor;
        this.groupName = groupName;
        //need to validate both the request condition, and the response descriptor
        validateConfiguration();
    }

    /**
     * Produces a StubConfiguration configuration XML.
     *
     * @return xml content
     */
    public String toString() {
        String generatedName = "generated name";
        String conditionContent = requestCondition.toString();
        String responseContent = responseDescriptor.responseDescriptorToString();
        String usedTemplateAndFormatter = responseDescriptor.templateToString();
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        String stubConfigurationFormatterString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<wilma-stub xmlns=\"http://epam.github.io/Wilma/xsd/StubConfig\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" groupname=\"%5$s\" "
                + "xsi:schemaLocation=\"http://epam.github.io/Wilma/xsd/StubConfig http://epam.github.io/Wilma/xsd/StubConfig.xsd\">\n"
                + "<dialog-descriptor name=\"%1$s\" usage=\"always\" comment=\"%1$s\">\n"
                + "<condition-descriptor>\n%2$s</condition-descriptor>\n"
                + "%3$s"
                + "</dialog-descriptor>\n"
                + "<template-descriptor name=\"%1$s\">\n%4$s</template-descriptor>\n"
                + "</wilma-stub>";
        formatter.format(stubConfigurationFormatterString, generatedName, conditionContent, responseContent, usedTemplateAndFormatter, groupName);
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

}
