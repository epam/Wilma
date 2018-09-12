package com.epam.wilma.stubconfig.cache.saver;
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
import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.exception.JsonTransformationException;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.json.parser.helper.JsonBasedObjectTransformer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class provides the ability of saving stub configurations with in their order.
 *
 * @author Tibor_Kovacs
 * @author Tamas_Kohegyi
 */
@Component
public class StubConfigurationSaver {
    private static final String STUB_CONFIG_JSON_POSTFIX = "_stubConfig.json";
    @Autowired
    private StubResourceHolder stubResourceHolder;
    @Autowired
    private StubConfigurationAccess configurationAccess;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private JsonBasedObjectTransformer jsonBasedObjectTransformer;

    private String cacheFolderPath;

    /**
     * This method will look up the path of the cache folder from properties.
     * Then it gets stub descriptors in its parameter, and call the {@link JsonBasedObjectTransformer} with each file of stub configurations.
     *
     * @param descriptors is the collection of {@link StubDescriptor}s that you want to save
     * @throws JsonTransformationException is thrown when {@link JSONObject} can not be transformed
     */
    public void saveAllStubConfigurations(final Map<String, StubDescriptor> descriptors) throws JsonTransformationException {
        getCacheFolderPath();
        int index = 1;
        for (String groupname : descriptors.keySet()) {
            StubDescriptor actualDescriptor = descriptors.get(groupname);
            tryToSaveActualStubConfig(actualDescriptor, index, groupname);
            index++;
        }
    }

    private void tryToSaveActualStubConfig(final StubDescriptor descriptor, final int index, final String groupname) throws JsonTransformationException {
        try {
            JSONObject actualObject = stubResourceHolder.getActualStubConfigJsonObject(groupname);
            if (actualObject != null) {
                jsonBasedObjectTransformer.transformToFile(actualObject, cacheFolderPath + "/" + index + STUB_CONFIG_JSON_POSTFIX, descriptor.getAttributes()
                        .isActive());
            }
        } catch (Exception e) {
            throw new JsonTransformationException(groupname + "'s stub configuration JSON can not be transformed. ", e);
        }
    }

    private void getCacheFolderPath() {
        configurationAccess.setProperties();
        cacheFolderPath = stubResourcePathProvider.getCachePath();
    }
}
