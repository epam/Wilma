package com.epam.wilma.webapp.service.command;
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
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class creates a new stub descriptor from the given inputStream and put it into the collection of descriptors.
 *
 * @author Tibor_Kovacs
 */
public class NewStubDescriptorCommand implements StubDescriptorModificationCommand {

    private final StubDescriptorJsonFactory stubConfigurationJsonBuilder;
    private final InputStream inputStream;
    private final SequenceDescriptorHolder sequenceDescriptorHolder;

    /**
     * Creates a Command which will parse the given stub configuration and put it into the collection. JSON version.
     *
     * @param inputStream              is the stream of new stub configuration
     * @param stubConfigurationJsonBuilder creates a StubDescriptor object from a inputStream
     * @param sequenceDescriptorHolder the object which will store the sequence descriptors
     */
    public NewStubDescriptorCommand(final InputStream inputStream, final StubDescriptorJsonFactory stubConfigurationJsonBuilder,
                                    final SequenceDescriptorHolder sequenceDescriptorHolder) {
        this.inputStream = inputStream;
        this.stubConfigurationJsonBuilder = stubConfigurationJsonBuilder;
        this.sequenceDescriptorHolder = sequenceDescriptorHolder;
    }

    @Override
    public Map<String, StubDescriptor> modify(final Map<String, StubDescriptor> stubDescriptors) {
        Map<String, StubDescriptor> updated = new LinkedHashMap<>(stubDescriptors);
        StubDescriptor stubDescriptor = stubConfigurationJsonBuilder.buildStubDescriptor(inputStream);
        StubDescriptorAttributes attributes = stubDescriptor.getAttributes();
        String groupName = attributes.getGroupName();
        sequenceDescriptorHolder.addAllSequenceDescriptors(stubDescriptor);
        updated.put(groupName, stubDescriptor);
        if (!attributes.isValid()) {
            updated.remove(groupName);
        }
        return updated;
    }

}
