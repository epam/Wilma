package com.epam.wilma.stubconfig.cache.saver;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.dom.transformer.DomBasedDocumentTransformer;
import com.epam.wilma.stubconfig.domain.exception.DocumentTransformationException;

/**
 * This class provides the ability of saving stub configurations with in their order.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationSaver {
    private static final String STUB_CONFIG_XML_POSTFIX = "_stubConfig.xml";
    @Autowired
    private StubResourceHolder stubResourceHolder;
    @Autowired
    private StubConfigurationAccess configurationAccess;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private DomBasedDocumentTransformer documentTransformer;

    private String cacheFolderPath;

    /**
     * This method will look up the path of the cache folder from properties.
     * Then it gets stub descriptors in its parameter, and call the {@link DomBasedDocumentTransformer} with each document of stub configurations.
     * @param descriptors is the collection of {@link StubDescriptor}s that you want to save
     * @throws DocumentTransformationException is thrown when {@link Document} can not be transformed
     */
    public void saveAllStubConfigurations(final Map<String, StubDescriptor> descriptors) throws DocumentTransformationException {
        getCacheFolderPath();
        int index = 1;
        for (String groupname : descriptors.keySet()) {
            StubDescriptor actualDescriptor = descriptors.get(groupname);
            tryToSaveActualStubConfig(actualDescriptor, index, groupname);
            index++;
        }
    }

    private void tryToSaveActualStubConfig(final StubDescriptor descriptor, final int index, final String groupname) throws DocumentTransformationException {
        try {
            Document actualDocument = stubResourceHolder.getActualStubConfigDocument(groupname);
            documentTransformer.transformToFile(actualDocument, cacheFolderPath + "/" + index + STUB_CONFIG_XML_POSTFIX, descriptor.getAttributes()
                    .isActive());
        } catch (Exception e) {
            throw new DocumentTransformationException(groupname + "'s stub configuration XML can not be transformed. ", e);
        }
    }

    private void getCacheFolderPath() {
        configurationAccess.setProperties();
        cacheFolderPath = stubResourcePathProvider.getCachePath();
    }
}
