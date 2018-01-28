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

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;

/**
 * Used to parse the sequence descriptors of the stub config.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class SequenceDescriptorParser {

    @Autowired
    private SequenceDescriptorFactory sequenceDescriptorFactory;

    /**
     * Parses the sequence descriptors of the stub config.
     * @param document the document to be parsed
     * @param root the root element of the document
     * @param dialogDescriptors list of parsed {@link DialogDescriptor}s
     * @return a list of sequence descriptors, an empty list if none found
     */
    public List<SequenceDescriptor> parse(final Document document, final Element root, final List<DialogDescriptor> dialogDescriptors) {
        NodeList sequenceDescriptorNodes = root.getElementsByTagName(SequenceDescriptor.TAG_NAME);
        List<SequenceDescriptor> sequenceDescriptors = new ArrayList<>();
        for (int i = 0; i < sequenceDescriptorNodes.getLength(); i++) {
            Element sequenceElement = (Element) sequenceDescriptorNodes.item(i);
            SequenceDescriptor sequenceDescriptor = sequenceDescriptorFactory.create(document, sequenceElement, dialogDescriptors);
            sequenceDescriptors.add(sequenceDescriptor);
        }
        return sequenceDescriptors;
    }

}
