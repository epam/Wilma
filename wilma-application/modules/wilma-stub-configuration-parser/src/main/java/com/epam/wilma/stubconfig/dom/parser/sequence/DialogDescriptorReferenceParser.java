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
import java.util.Map;

import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;

/**
 * Used to parse the {@link DialogDescriptor} reference elements of a stub config.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class DialogDescriptorReferenceParser {

    private static final String DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME = "dialog-descriptor-ref";

    /**
     * Parses the dialog descriptor references in the sequence descriptor and resolves them.
     * @param sequenceElement the element representing the sequence descriptor
     * @param nameToDialogDescriptor the parsed {@link DialogDescriptor}s paired to their name
     * @return a list of {@link DialogDescriptor}s that were referenced in the sequenceElement
     */
    public List<DialogDescriptor> parse(final Element sequenceElement, final Map<String, DialogDescriptor> nameToDialogDescriptor) {
        List<DialogDescriptor> result = new ArrayList<>();
        NodeList dialogDescriptorReferences = sequenceElement.getElementsByTagName(DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME);
        for (int i = 0; i < dialogDescriptorReferences.getLength(); i++) {
            Element dialogDescriptorReferenceElement = (Element) dialogDescriptorReferences.item(i);
            String referencedName = dialogDescriptorReferenceElement.getAttribute("ref");
            DialogDescriptor dialogDescriptor = nameToDialogDescriptor.get(referencedName);
            if (dialogDescriptor != null) {
                result.add(dialogDescriptor);
            }
        }
        return result;
    }

}
