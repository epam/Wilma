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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorAttributes;
import com.epam.wilma.stubconfig.dom.parser.node.SequenceDescriptorAttributesParser;
import com.epam.wilma.stubconfig.dom.parser.sequence.helper.DialogDescriptorMapper;

/**
 * Used for {@link SequenceDescriptor} instantiation from a stub config file.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class SequenceDescriptorFactory {

    @Autowired
    private SequenceDescriptorAttributesParser sequenceDescriptorAttributesParser;
    @Autowired
    private DialogDescriptorReferenceParser dialogDescriptorReferenceParser;
    @Autowired
    private DialogDescriptorMapper dialogDescriptorMapper;
    @Autowired
    private ConditionDescriptorNodesParser conditionDescriptorNodesParser;

    /**
     * Creates a {@link SequenceDescriptor} instances from the supplied parameters.
     * @param document the document of the sequence descriptor
     * @param sequenceElement the actual sequence descriptor element
     * @param dialogDescriptors the parsed {@link DialogDescriptor}s of the document
     * @return a new {@link SequenceDescriptor} instance.
     */
    public SequenceDescriptor create(final Document document, final Element sequenceElement, final List<DialogDescriptor> dialogDescriptors) {
        List<ConditionDescriptor> conditionDescriptors = conditionDescriptorNodesParser.parseNodesOfElement(document, sequenceElement);
        Map<String, DialogDescriptor> dialogDescriptorMap = dialogDescriptorMapper.groupByName(dialogDescriptors);
        List<DialogDescriptor> referencedDialogDescriptors = dialogDescriptorReferenceParser.parse(sequenceElement, dialogDescriptorMap);
        SequenceDescriptorAttributes attributes = sequenceDescriptorAttributesParser.parseNode(sequenceElement, document);
        return new SequenceDescriptor(conditionDescriptors, referencedDialogDescriptors, attributes);
    }

}
