package com.epam.wilma.webapp.service;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.cache.cleaner.CacheCleaner;
import com.epam.wilma.stubconfig.cache.saver.StubConfigurationSaver;
import com.epam.wilma.domain.stubconfig.exception.DocumentTransformationException;

/**
 * This class provides the ability of saving the stub configurations into the cache folder.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationSaverService {

    @Autowired
    private RoutingService routingService;
    @Autowired
    private StubConfigurationSaver saver;
    @Autowired
    private CacheCleaner cleaner;

    /**
     * This method deletes all old files from the cache folder.
     * Then it gets all stub descriptors from {@link RoutingService} and call {@link StubConfigurationSaver} to save all of the descriptors.
     * @throws DocumentTransformationException is thrown when a document can not be written
     */
    public void saveStubConfigurations() throws DocumentTransformationException {
        cleaner.cleanCache();
        Map<String, StubDescriptor> descriptors = routingService.getStubDescriptors();
        saver.saveAllStubConfigurations(descriptors);
    }

}
