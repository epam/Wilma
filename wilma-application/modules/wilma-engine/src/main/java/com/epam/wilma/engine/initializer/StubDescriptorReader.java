package com.epam.wilma.engine.initializer;

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

import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.webapp.service.command.factory.NewStubDescriptorJsonCommandFactory;
import com.epam.wilma.webapp.service.external.ServiceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Reads all new stub descriptor from a folder and applies the new configurations in the core module.
 *
 * @author Tibor_Kovacs
 */
@Component
public class StubDescriptorReader {

    @Autowired
    private StubDescriptorJsonFactory stubConfigurationJsonBuilder;
    @Autowired
    private RoutingService routingService;
    @Autowired
    private SequenceDescriptorHolder sequenceDescriptorHolder;
    @Autowired
    private NewStubDescriptorJsonCommandFactory newStubDescriptorJsonCommandFactory;
    @Autowired
    private ServiceMap serviceMap;

    /**
     * Load the all stub configuration files from the given path list and applies them after loading.
     *
     * @param filePaths this list contains the paths of those stub configurations that we want to load in
     */
    public void loadSpecificStubDescriptors(final List<String> filePaths) {
        for (String jsonFilePath : filePaths) {
            createStubDescriptor(jsonFilePath);
        }
        serviceMap.detectServices();
    }

    private void createStubDescriptor(final String jsonFilePath) {
        try {
            StubDescriptorModificationCommand command;
            command = newStubDescriptorJsonCommandFactory.create(jsonFilePath, stubConfigurationJsonBuilder, sequenceDescriptorHolder);
            routingService.performModification(command);
        } catch (ClassNotFoundException | FileNotFoundException e) {
            throw new DescriptorCannotBeParsedException("One of the stub descriptor files cannot be found!", e);
        }
    }

}
