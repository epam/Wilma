package com.epam.wilma.stubconfig.dom.parser.sequence;
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.stubconfig.dom.parser.node.ConditionDescriptorParser;

/**
 * Parses the condition descriptors of an element.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ConditionDescriptorNodesParser {

    @Autowired
    private ConditionDescriptorParser conditionDescriptorParser;

    /**
     * Parses the condition descriptors of the given XML element.
     * @param document the document of the element
     * @param element the given XML element
     * @return a list of the parsed {@link ConditionDescriptor}s
     */
    public List<ConditionDescriptor> parseNodesOfElement(final Document document, final Element element) {
        List<ConditionDescriptor> result = new ArrayList<>();
        NodeList conditionDescriptorNodes = element.getElementsByTagName(ConditionDescriptor.TAG_NAME);
        for (int i = 0; i < conditionDescriptorNodes.getLength(); i++) {
            ConditionDescriptor conditionDescriptor = conditionDescriptorParser.parseNode(conditionDescriptorNodes.item(i), document);
            result.add(conditionDescriptor);
        }
        return result;
    }
}
