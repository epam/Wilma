package com.epam.wilma.stubconfig.dom.parser;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.stubconfig.dom.parser.node.DialogDescriptorAttributeParser;
import com.epam.wilma.stubconfig.dom.parser.node.InterceptorDescriptorParser;
import com.epam.wilma.stubconfig.dom.parser.sequence.SequenceDescriptorParser;

/**
 * Builds a new {@link StubDescriptor} model from a given DOM document.
 * @author Marton_Sereg
 *
 */
@Component
public class StubDescriptorParser {

    @Autowired
    @Qualifier("conditionDescriptorParser")
    private NodeParser<ConditionDescriptor> conditionDescriptorParser;
    @Autowired
    @Qualifier("responseDescriptorParser")
    private NodeParser<ResponseDescriptor> responseDescriptorParser;
    @Autowired
    private DialogDescriptorAttributeParser dialogDescriptorAttributeParser;
    @Autowired
    private SequenceDescriptorParser sequenceDescriptorParser;
    @Autowired
    private InterceptorDescriptorParser interceptorDescriptorParser;

    /**
     * Builds a new {@link StubDescriptor} domain model from a given DOM document.
     * @param document the DOM model of the stub descriptor
     * @return the newly built StubDescriptor
     */
    public StubDescriptor parse(final Document document) {
        Element root = document.getDocumentElement();
        StubDescriptorAttributes attributes = getStubDescriptorAttributes(root);
        List<DialogDescriptor> dialogDescriptors = getDialogDescriptors(document, root);
        List<InterceptorDescriptor> interceptorDescriptors = getInterceptorDescriptors(document, root);
        List<SequenceDescriptor> sequenceDescriptors = sequenceDescriptorParser.parse(document, root, dialogDescriptors);
        return new StubDescriptor(attributes, dialogDescriptors, interceptorDescriptors, sequenceDescriptors);
    }

    private StubDescriptorAttributes getStubDescriptorAttributes(final Element root) {
        String groupName = root.getAttribute("groupname");
        String activeText = root.getAttribute("active");
        boolean active = true;
        if (!"".equals(activeText)) {
            active = Boolean.valueOf(activeText);
        }
        return new StubDescriptorAttributes(groupName, active);
    }

    private List<DialogDescriptor> getDialogDescriptors(final Document document, final Element root) {
        NodeList dialogDescriptorList = root.getElementsByTagName("dialog-descriptor");
        List<DialogDescriptor> dialogDescriptors = new ArrayList<DialogDescriptor>();
        if (dialogDescriptorList != null) {
            for (int i = 0; i < dialogDescriptorList.getLength(); i++) {
                Element el = (Element) dialogDescriptorList.item(i);
                DialogDescriptorAttributes attributes = dialogDescriptorAttributeParser.getAttributes(el);
                ConditionDescriptor conditionDescriptor = conditionDescriptorParser.parseNode(el.getElementsByTagName(ConditionDescriptor.TAG_NAME)
                        .item(0), document);
                ResponseDescriptor responseDescriptor = responseDescriptorParser.parseNode(el.getElementsByTagName("response-descriptor").item(0),
                        document);
                dialogDescriptors.add(new DialogDescriptor(attributes, conditionDescriptor, responseDescriptor));
            }
        }
        return dialogDescriptors;
    }

    private List<InterceptorDescriptor> getInterceptorDescriptors(final Document document, final Element root) {
        NodeList interceptorsList = root.getElementsByTagName("interceptor");
        List<InterceptorDescriptor> interceptorDescriptors = new ArrayList<>();
        if (interceptorsList != null) {
            for (int i = 0; i < interceptorsList.getLength(); i++) {
                InterceptorDescriptor interceptorDescriptor = interceptorDescriptorParser.parseNode(interceptorsList.item(i), document);
                interceptorDescriptors.add(interceptorDescriptor);
            }
        }
        return interceptorDescriptors;
    }
}
