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

import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.webapp.service.command.NewStubDescriptorCommand;
import com.epam.wilma.webapp.service.command.factory.NewStubDescriptorJsonCommandFactory;
import com.epam.wilma.webapp.service.external.ServiceMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link StubDescriptorReader}.
 *
 * @author Tibor_Kovacs
 */
public class StubDescriptorReaderTest {

    @Mock
    private StubDescriptorJsonFactory stubConfigurationJsonBuilder;
    @Mock
    private RoutingService routingService;
    @Mock
    private SequenceDescriptorHolder sequenceDescriptorHolder;
    @Mock
    private NewStubDescriptorJsonCommandFactory newStubDescriptorJsonCommandFactory;
    @Mock
    private ServiceMap serviceMap;

    @InjectMocks
    private StubDescriptorReader underTest;

    @Mock
    private NewStubDescriptorCommand command;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadSpecificStubDescriptors() throws FileNotFoundException, ClassNotFoundException {
        //GIVEN
        List<String> paths = new ArrayList<>();
        paths.add("test");
        given(newStubDescriptorJsonCommandFactory.create("test", stubConfigurationJsonBuilder, sequenceDescriptorHolder)).willReturn(command);
        //WHEN
        underTest.loadSpecificStubDescriptors(paths);
        //THEN
        verify(routingService, times(1)).performModification(command);
    }
}
