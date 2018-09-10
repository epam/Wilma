package com.epam.wilma.stubconfig.json;
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
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.dom.parser.StubResourceHolderUpdater;
import com.epam.wilma.stubconfig.json.parser.StubDescriptorJsonParser;
import com.epam.wilma.stubconfig.json.schema.StubConfigJsonSchemaParser;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Loads the stub descriptor object model from an {@link InputStream} by reading it to a JSON Object and
 * then validating and parsing it to the proper object structure.
 *
 * @author Tamas Kohegyi
 */
@Component
public class JsonBasedStubDescriptorFactory implements StubDescriptorJsonFactory {

    @Autowired
    private StubDescriptorJsonParser descriptorParser;
    @Autowired
    private StubResourceHolderUpdater stubResourceHolderUpdater;
    @Autowired
    private StubConfigurationAccess configurationAccess;
    @Autowired
    private StubConfigJsonSchemaParser stubConfigJsonSchemaParser;

    /**
     * Loads the stub descriptor from an {@link InputStream}.
     * It is synchronized in order to avoid inconsistent states during an on the fly stub
     * configuration.
     *
     * @param inputStream the stream that contains the stub descriptor.
     * @return the newly built {@link StubDescriptor}
     */
    @Override
    public synchronized StubDescriptor buildStubDescriptor(final InputStream inputStream) {
        try {
            //load the json file
            JSONObject jsonStubDescriptor = new JSONObject(new JSONTokener(inputStream));
            //load the json schema file
            Schema jsonSchema = stubConfigJsonSchemaParser.parseSchema();
            //validate against schema
            jsonSchema.validate(jsonStubDescriptor);
            //validate against extra rules (those cannot be im schema)
            //TODO: validating
            //load the extra validation
            //understand the extra validation rules and do such extra validation
            // - name in sequenceDescriptor array must be unique
            // - name in conditionSets array must be unique
            // - name in template array must be unique
            // - name in dialog descriptors must be unique
            // - name in templateFormatterSets must be unique
            // - wilma stub groupName shall not contain chars: "|" ";"
            // - sequenceDescriptor name shall not contain chars: "|" ";"
            // - all class name must be valid/loadable
            //if everything goes well, continue with registering the stub configuration
            stubResourceHolderUpdater.initializeTemporaryResourceHolder();
            configurationAccess.setProperties();
            StubDescriptor stubDescriptor = descriptorParser.parse(jsonStubDescriptor);
            stubResourceHolderUpdater.updateResourceHolder();
            stubResourceHolderUpdater.clearTemporaryResourceHolder();
            StubDescriptorAttributes attributes = stubDescriptor.getAttributes();
            stubResourceHolderUpdater.addDocumentToResourceHolder(attributes.getGroupName(), jsonStubDescriptor);
            return stubDescriptor;
        } catch (ValidationException | JSONException e) {
            throw new DescriptorCannotBeParsedException("Stub descriptor cannot be parsed.", e);
        }
    }

}