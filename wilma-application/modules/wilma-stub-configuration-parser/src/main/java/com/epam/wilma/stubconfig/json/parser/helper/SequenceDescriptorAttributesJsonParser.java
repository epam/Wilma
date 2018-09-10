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

import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.initializer.sequencehandler.SequenceHandlerInitializer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Parses the attributes of a sequence descriptor element.
 *
 * @author Tamas Kohegyi
 */
@Component
public class SequenceDescriptorAttributesJsonParser implements ObjectParser<SequenceDescriptorAttributes> {

    @Autowired
    private SequenceHandlerInitializer sequenceHandlerInitializer;
    @Autowired
    private StubConfigurationAccess stubConfigurationAccess;
    @Autowired
    private ParameterListParser parameterListParser;

    @Override
    public SequenceDescriptorAttributes parseObject(JSONObject object, JSONObject root) {
        SequenceDescriptorAttributes result;
        String groupName = "Default";
        if (root.has("groupName")) {
            groupName = root.getString("groupName");
        }
        String name = object.getString("name");
        String sequenceHandlerClassName = object.getString("class");
        long timeout = getTimeout(object);
        ParameterList parameters = parameterListParser.parseObject(object, root);
        SequenceHandler sequenceHandler = sequenceHandlerInitializer.getExternalClassObject(sequenceHandlerClassName);
        result = new SequenceDescriptorAttributes.Builder().handler(sequenceHandler).name(name).groupname(groupName).defaultTimeout(timeout)
                .parameterList(parameters).build();
        return result;
    }

    private long getTimeout(final JSONObject sequenceElement) {
        long timeout = stubConfigurationAccess.getProperties().getDefaultSequenceTimeout();
        if (sequenceElement.has("timeout")) {
            timeout = sequenceElement.getLong("timeout");
        }
        return timeout;
    }

}
