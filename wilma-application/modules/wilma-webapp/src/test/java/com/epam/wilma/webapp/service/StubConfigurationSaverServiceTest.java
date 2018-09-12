package com.epam.wilma.webapp.service;
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
import com.epam.wilma.domain.stubconfig.exception.JsonTransformationException;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.cache.cleaner.CacheCleaner;
import com.epam.wilma.stubconfig.cache.saver.StubConfigurationSaver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link StubConfigurationSaverService}.
 *
 * @author Tibor_Kovacs
 */
public class StubConfigurationSaverServiceTest {
    @Mock
    private RoutingService routingService;
    @Mock
    private StubConfigurationSaver saver;
    @Mock
    private CacheCleaner cleaner;

    @InjectMocks
    private StubConfigurationSaverService underTest;

    private Map<String, StubDescriptor> descriptors = new HashMap<>();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(routingService.getStubDescriptors()).willReturn(descriptors);
    }

    @Test
    public void testSaveStubConfigurationsShouldCallCleanerThenSaver() throws JsonTransformationException {
        //GIVEN
        //WHEN
        underTest.saveStubConfigurations();
        //THEN
        verify(cleaner).cleanCache();
        verify(saver).saveAllStubConfigurations(descriptors);
    }
}
