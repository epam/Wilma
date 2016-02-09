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

import com.epam.wilma.service.unit.request.RequestCondition;
import com.epam.wilma.service.unit.response.ResponseDescriptor;

import java.util.Formatter;

/**
 * Class that represents a stubbed request-response pairs.
 * Example configuration:
 * <p/>
 * <?xml version="1.0" encoding="UTF-8"?>
 * <wilma-stub xmlns="http://epam.github.io/Wilma/xsd/StubConfig" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xsi:schemaLocation="http://epam.github.io/Wilma/xsd/StubConfig http://epam.github.io/Wilma/xsd/StubConfig.xsd">
 * <dialog-descriptor name="dummy-descriptor" usage="always" comment="random comment">
 * <condition-descriptor>
 * <condition class="AlwaysFalseChecker" />
 * </condition-descriptor>
 * <response-descriptor code="502" delay="0" mimetype="text/plain" template="errorResponse" />
 * </dialog-descriptor>
 * <p/>
 * <template-descriptor name="template-descriptor_1">
 * <template name="errorResponse" type="text" resource="Bad Gateway" />
 * </template-descriptor>
 * </wilma-stub>
 *
 * @author Tamas_Kohegyi
 */
public class Stub {

    private static String STUB_CONFIGURATION_FORMATTER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<wilma-stub xmlns=\"http://epam.github.io/Wilma/xsd/StubConfig\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xsi:schemaLocation=\"http://epam.github.io/Wilma/xsd/StubConfig http://epam.github.io/Wilma/xsd/StubConfig.xsd\">\n"
                    + "<dialog-descriptor name=\"%1$s\" usage=\"always\" comment=\"%1$s\">\n"
                    + "<condition-descriptor>\n%2$s</condition-descriptor>\n"
                    + "%3$s"
                    + "</dialog-descriptor>\n"
                    + "<template-descriptor name=\"%1$s\"\n>\n%4$s</template-descriptor>\n"
                    + "</wilma-stub>";

    private RequestCondition requestCondition;
    private ResponseDescriptor responseDescriptor;

    public Stub(RequestCondition requestCondition, ResponseDescriptor responseDescriptor) {
        this.requestCondition = requestCondition;
        this.responseDescriptor = responseDescriptor;
    }

    /**
     * Produces a Stub configuration XML.
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
        formatter.format(STUB_CONFIGURATION_FORMATTER, generatedName, conditionContent, responseContent, usedTemplateAndFormatter);
        return sb.toString();
    }

    public void start() {
    }

    public void stop() {
    }

    public void disable() {
    }

    public void enable() {
    }

    public void drop() {
    }

}
