package com.epam.wilma.stubconfig.dom;
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

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubConfigSchema;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.stubconfig.StubDescriptorFactory;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.dom.builder.XmlDocumentBuilder;
import com.epam.wilma.stubconfig.dom.parser.StubDescriptorParser;
import com.epam.wilma.stubconfig.dom.parser.StubResourceHolderUpdater;
import com.epam.wilma.stubconfig.dom.validator.StubDescriptorValidator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.domain.stubconfig.exception.DocumentBuilderException;

/**
 * Loads the stub descriptor object model from an {@link InputStream} by reading it to a DOM Document and
 * then validating and parsing it to the proper object structure.
 * @author Marton_Sereg
 *
 */
@Component
public class DomBasedStubDescriptorFactory implements StubDescriptorFactory {

    @Autowired
    private XmlDocumentBuilder xmlDocumentBuilder;

    @Autowired
    private List<StubDescriptorValidator> descriptorValidators;

    @Autowired
    private StubDescriptorParser descriptorParser;

    @Autowired
    private StubResourceHolderUpdater stubResourceHolderUpdater;

    @Autowired
    private StubConfigurationAccess configurationAccess;

    @Autowired
    private StubConfigSchema stubConfigSchema;

    /**
     * Loads the stub descriptor from an {@link InputStream}.
     * It is synchronized in order to avoid inconsistent states during an on the fly stub
     * configuration.
     * @param inputStream the stream that contains the stub descriptor.
     * @return the newly built {@link StubDescriptor}
     */
    @Override
    public synchronized StubDescriptor buildStubDescriptor(final InputStream inputStream) {
        try {
            Document document = xmlDocumentBuilder.buildDocument(inputStream, stubConfigSchema.getSchema());
            validate(document);
            stubResourceHolderUpdater.initializeTemporaryResourceHolder();
            configurationAccess.setProperties();
            StubDescriptor stubDescriptor = descriptorParser.parse(document);
            stubResourceHolderUpdater.updateResourceHolder();
            stubResourceHolderUpdater.clearTemporaryResourceHolder();
            StubDescriptorAttributes attributes = stubDescriptor.getAttributes();
            stubResourceHolderUpdater.addDocumentToResourceHolder(attributes.getGroupName(), document);
            return stubDescriptor;
        } catch (DocumentBuilderException e) {
            throw new DescriptorCannotBeParsedException("Stub descriptor cannot be parsed.", e);
        }
    }

    private void validate(final Document document) {
        for (StubDescriptorValidator descriptorValidator : descriptorValidators) {
            descriptorValidator.validate(document, stubConfigSchema.getSchema());
        }
    }
}
