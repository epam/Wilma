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
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorAttributes;
import com.epam.wilma.stubconfig.dom.parser.sequence.helper.DialogDescriptorMapper;
import com.epam.wilma.stubconfig.json.parser.ConditionDescriptorJsonParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used for {@link SequenceDescriptor} instantiation from a stub config file.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class SequenceDescriptorJsonFactory {

    @Autowired
    private SequenceDescriptorAttributesJsonParser sequenceDescriptorAttributesJsonParser;
    @Autowired
    private DialogDescriptorReferenceJsonParser dialogDescriptorReferenceJsonParser;
    @Autowired
    private DialogDescriptorMapper dialogDescriptorMapper;
    @Autowired
    private ConditionDescriptorJsonParser conditionDescriptorJsonParser;

    /**
     * Creates a {@link SequenceDescriptor} instances from the supplied parameters.
     *
     * @param root              the object that holds the sequence descriptor
     * @param sequenceElement   the actual sequence descriptor element
     * @param dialogDescriptors the parsed {@link DialogDescriptor}s of the document
     * @return a new {@link SequenceDescriptor} instance.
     */
    public SequenceDescriptor create(final JSONObject root, final JSONObject sequenceElement, final List<DialogDescriptor> dialogDescriptors) {
        //no array seems to be necessary here, so in case XML config is removed, can be transferred into a single value
        //at json config we accept single conditionDescriptor already
        List<ConditionDescriptor> conditionDescriptors = new ArrayList<>();
        if (sequenceElement.has(ConditionDescriptor.TAG_NAME_JSON)) {
            JSONObject conditionDescriptorObject = sequenceElement.getJSONObject(ConditionDescriptor.TAG_NAME_JSON);
            ConditionDescriptor conditionDescriptor = conditionDescriptorJsonParser.parseObject(conditionDescriptorObject, root);
            conditionDescriptors.add(conditionDescriptor);
        }
        Map<String, DialogDescriptor> dialogDescriptorMap = dialogDescriptorMapper.groupByName(dialogDescriptors);
        List<DialogDescriptor> referencedDialogDescriptors = dialogDescriptorReferenceJsonParser.parse(sequenceElement, dialogDescriptorMap);
        SequenceDescriptorAttributes attributes = sequenceDescriptorAttributesJsonParser.parseObject(sequenceElement, root);
        return new SequenceDescriptor(conditionDescriptors, referencedDialogDescriptors, attributes);
    }

}
