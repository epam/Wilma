package com.epam.wilma.stubconfig.dom.parser.node;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.dom.parser.NodeParser;
import com.epam.wilma.stubconfig.initializer.sequencehandler.SequenceHandlerInitializer;

/**
 * Parses the attributes of a sequence descriptor element.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class SequenceDescriptorAttributesParser implements NodeParser<SequenceDescriptorAttributes> {

    @Autowired
    private SequenceHandlerInitializer sequenceHandlerInitializer;
    @Autowired
    private StubConfigurationAccess stubConfigurationAccess;

    @Override
    public SequenceDescriptorAttributes parseNode(final Node node, final Document document) {
        SequenceDescriptorAttributes result;
        Element sequenceElement = (Element) node;
        String groupName = document.getDocumentElement().getAttribute("groupname");
        String name = sequenceElement.getAttribute("name");
        String sequenceHandlerClassName = sequenceElement.getAttribute("class");
        long timeout = getTimeout(sequenceElement);
        ParameterList parameters = getParameters(sequenceElement);
        SequenceHandler sequenceHandler = sequenceHandlerInitializer.getExternalClassObject(sequenceHandlerClassName);
        result = new SequenceDescriptorAttributes.Builder().handler(sequenceHandler).name(name).groupname(groupName).defaultTimeout(timeout)
                .parameterList(parameters).build();
        return result;
    }

    private ParameterList getParameters(final Element sequenceElement) {
        ParameterList result = new ParameterList();
        NodeList parameterReferences = sequenceElement.getElementsByTagName("param");
        for (int i = 0; i < parameterReferences.getLength(); i++) {
            Element parameterReferenceElement = (Element) parameterReferences.item(i);
            String name = parameterReferenceElement.getAttribute("name");
            String value = parameterReferenceElement.getAttribute("value");
            if ((name != null) && (value != null)) {
                result.addParameter(new Parameter(name, value));
            }
        }
        return result;
    }

    private long getTimeout(final Element sequenceElement) {
        long timeout = stubConfigurationAccess.getProperties().getDefaultSequenceTimeout();
        String timeoutString = sequenceElement.getAttribute("timeout");
        if (!"".equals(timeoutString)) {
            timeout = Long.parseLong(timeoutString);
        }
        return timeout;
    }

}
