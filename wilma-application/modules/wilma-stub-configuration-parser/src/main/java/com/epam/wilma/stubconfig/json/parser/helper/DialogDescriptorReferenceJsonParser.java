package com.epam.wilma.stubconfig.json.parser.helper;

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

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to parse the {@link DialogDescriptor} reference objects of a stub config.
 *
 * @author Tamas Kohegyo
 */
@Component
public class DialogDescriptorReferenceJsonParser {

    private static final String DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME = "dialogDescriptorNames";

    /**
     * Parses the dialog descriptor references in the sequence descriptor and resolves them.
     *
     * @param object                 the object representing the sequence descriptor
     * @param nameToDialogDescriptor the parsed {@link DialogDescriptor}s paired to their name
     * @return a list of {@link DialogDescriptor}s that were referenced in the sequenceElement
     */
    public List<DialogDescriptor> parse(final JSONObject object, final Map<String, DialogDescriptor> nameToDialogDescriptor) {
        List<DialogDescriptor> result = new ArrayList<>();
        if (object.has(DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME)) {
            JSONArray nameArray = object.getJSONArray(DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME);
            for (int i = 0; i < nameArray.length(); i++) {
                JSONObject dialogDescriptorReferenceElement = nameArray.getJSONObject(i);
                String referencedName = dialogDescriptorReferenceElement.getString("name");
                DialogDescriptor dialogDescriptor = nameToDialogDescriptor.get(referencedName);
                if (dialogDescriptor != null) {
                    result.add(dialogDescriptor);
                }
            }
        }
        return result;
    }

}
