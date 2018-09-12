package com.epam.wilma.webapp.service.command.factory;
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

import com.epam.wilma.common.stream.helper.FileInputStreamFactory;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.webapp.service.command.NewStubDescriptorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Factory for instantiating {@link NewStubDescriptorCommand}s.
 *
 * @author Adam_Csaba_Kiraly
 */
@Component
public class NewStubDescriptorCommandFactory {

    @Autowired
    private FileInputStreamFactory fileInputStreamFactory;

    /**
     * Instantiates a {@link NewStubDescriptorCommand} object.
     *
     * @param filePath                     is the path of the new stub configuration
     * @param stubConfigurationJsonBuilder creates a StubDescriptor object from a inputStream
     * @param sequenceDescriptorHolder     the object which will store the sequence descriptors
     * @return the new instance
     * @throws FileNotFoundException if no file is found on the given file path
     */
    public StubDescriptorModificationCommand create(final String filePath, final StubDescriptorJsonFactory stubConfigurationJsonBuilder,
                                                    final SequenceDescriptorHolder sequenceDescriptorHolder) throws FileNotFoundException {
        InputStream inputStream = fileInputStreamFactory.createFileInputStream(filePath);
        return create(inputStream, stubConfigurationJsonBuilder, sequenceDescriptorHolder);
    }

    /**
     * Instantiates a {@link NewStubDescriptorCommand} object.
     *
     * @param inputStream                  is the stream of new stub configuration
     * @param stubConfigurationJsonBuilder creates a StubDescriptor object from a inputStream
     * @param sequenceDescriptorHolder     the object which will store the sequence descriptors
     * @return the new instance
     */
    public StubDescriptorModificationCommand create(final InputStream inputStream, final StubDescriptorJsonFactory stubConfigurationJsonBuilder,
                                                    final SequenceDescriptorHolder sequenceDescriptorHolder) {
        return new NewStubDescriptorCommand(inputStream, stubConfigurationJsonBuilder, sequenceDescriptorHolder);
    }
}
