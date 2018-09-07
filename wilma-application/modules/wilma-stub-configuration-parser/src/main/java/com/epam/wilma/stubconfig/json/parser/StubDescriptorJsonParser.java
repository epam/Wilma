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

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.stubconfig.json.parser.helper.DialogDescriptorAttributeJsonParser;
import com.epam.wilma.stubconfig.json.parser.helper.ObjectParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a new {@link StubDescriptor} model from a given DOM document.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class StubDescriptorJsonParser {

    @Autowired
    @Qualifier("conditionDescriptorJsonParser")
    private ObjectParser<ConditionDescriptor> conditionDescriptorJsonParser;
    @Autowired
    @Qualifier("responseDescriptorJsonParser")
    private ObjectParser<ResponseDescriptor> responseDescriptorJsonParser;
    @Autowired
    private DialogDescriptorAttributeJsonParser dialogDescriptorAttributeJsonParser;
    @Autowired
    private SequenceDescriptorJsonParser sequenceDescriptorJsonParser;
    @Autowired
    private InterceptorDescriptorJsonParser interceptorDescriptorJsonParser;

    /**
     * Builds a new {@link StubDescriptor} domain model from a given DOM document.
     * @param jsonStubDescriptor the JSON Object of the stub descriptor
     * @return the newly built StubDescriptor
     */
    public StubDescriptor parse(final JSONObject jsonStubDescriptor) {
        JSONObject root = jsonStubDescriptor.getJSONObject("wilmaStubConfiguration");
        StubDescriptorAttributes attributes = getStubDescriptorAttributes(root);
        List<DialogDescriptor> dialogDescriptors = getDialogDescriptors(root);
        List<InterceptorDescriptor> interceptorDescriptors = getInterceptorDescriptors(root);
        List<SequenceDescriptor> sequenceDescriptors = sequenceDescriptorJsonParser.parse(root, dialogDescriptors);
        return new StubDescriptor(attributes, dialogDescriptors, interceptorDescriptors, sequenceDescriptors);
    }

    private StubDescriptorAttributes getStubDescriptorAttributes(final JSONObject root) {
        String groupName = root.has("groupName") ? root.getString("groupName") : "Default";
        String activeText = root.has("active") ? root.getString("active") : "true";
        boolean active;
        active = Boolean.valueOf(activeText);
        return new StubDescriptorAttributes(groupName, active);
    }

    private List<DialogDescriptor> getDialogDescriptors(final JSONObject root) {
        List<DialogDescriptor> dialogDescriptors = new ArrayList<DialogDescriptor>();
        if (root.has("dialogDescriptors")) {
            JSONArray dialogDescriptorsArray = root.getJSONArray("dialogDescriptors");
                for (int i = 0; i < dialogDescriptorsArray.length(); i++) {
                    JSONObject dialogDescriptor = dialogDescriptorsArray.getJSONObject(i);
                    DialogDescriptorAttributes attributes = dialogDescriptorAttributeJsonParser.getAttributes(dialogDescriptor);
                    ConditionDescriptor conditionDescriptor = conditionDescriptorJsonParser.parseObject(dialogDescriptor.getJSONObject(ConditionDescriptor.TAG_NAME_JSON), root);
                    ResponseDescriptor responseDescriptor = responseDescriptorJsonParser.parseObject(dialogDescriptor.getJSONObject(ResponseDescriptor.TAG_NAME_JSON),root);
                    dialogDescriptors.add(new DialogDescriptor(attributes, conditionDescriptor, responseDescriptor));
                }
        }
        return dialogDescriptors;
    }

    private List<InterceptorDescriptor> getInterceptorDescriptors(final JSONObject root) {
        List<InterceptorDescriptor> interceptorDescriptors = new ArrayList<>();
        if (root.has("interceptors")) {
            JSONArray interceptorArray = root.getJSONArray("interceptors");
            for (int i = 0; i < interceptorArray.length(); i++) {
                InterceptorDescriptor interceptorDescriptor = interceptorDescriptorJsonParser.parseObject(interceptorArray.getJSONObject(i), root);
                interceptorDescriptors.add(interceptorDescriptor);
            }
        }
        return interceptorDescriptors;
    }
}
