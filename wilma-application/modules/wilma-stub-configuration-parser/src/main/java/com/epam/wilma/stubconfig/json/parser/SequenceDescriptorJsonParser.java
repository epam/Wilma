package com.epam.wilma.stubconfig.json.parser;
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
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.stubconfig.json.parser.helper.SequenceDescriptorJsonFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to parse the sequence descriptors of the stub config.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class SequenceDescriptorJsonParser {

    @Autowired
    private SequenceDescriptorJsonFactory sequenceDescriptorJsonFactory;

    /**
     * Parses the sequence descriptors of the stub config.
     * @param root the main JSON object to be parsed
     * @param dialogDescriptors list of parsed {@link DialogDescriptor}s
     * @return a list of sequence descriptors, an empty list if none found
     */
    public List<SequenceDescriptor> parse(final JSONObject root, final List<DialogDescriptor> dialogDescriptors) {
        List<SequenceDescriptor> sequenceDescriptors = new ArrayList<>();
        if (root.has(SequenceDescriptor.TAG_NAME_JSON)) {
            JSONArray sequenceDescriptorArray = root.getJSONArray(SequenceDescriptor.TAG_NAME_JSON);
            for (int i = 0; i < sequenceDescriptorArray.length(); i++) {
                JSONObject sequence = sequenceDescriptorArray.getJSONObject(i);
                SequenceDescriptor sequenceDescriptor = sequenceDescriptorJsonFactory.create(root, sequence, dialogDescriptors);
                sequenceDescriptors.add(sequenceDescriptor);
            }

        }
        return sequenceDescriptors;
    }

}
