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
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.exception.JsonTransformationException;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.json.parser.helper.JsonBasedObjectTransformer;
import org.json.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.transform.TransformerException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link StubConfigurationSaver}.
 *
 * @author Tibor_Kovacs
 */
public class StubConfigurationSaverTest {
    private static final String TEST_PATH = "test/path";
    private static final String TEST_KEY = "TestKey";
    private static final String STUB_CONFIG_JSON_POSTFIX = "_stubConfig.json";

    @Mock
    private StubResourceHolder stubResourceHolder;
    @Mock
    private StubConfigurationAccess configurationAccess;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private StubDescriptor descriptor;
    @Mock
    private StubDescriptorAttributes attributes;
    @Mock
    private JSONObject jsonObject;
    @Mock
    private JsonBasedObjectTransformer documentTransformer;
    @Mock
    private Logger logger;

    private Map<String, StubDescriptor> descriptors = new HashMap<>();

    @InjectMocks
    private StubConfigurationSaver underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(stubResourcePathProvider.getCachePath()).willReturn(TEST_PATH);
        given(descriptor.getAttributes()).willReturn(attributes);
        given(attributes.isActive()).willReturn(true);
        descriptors.clear();
        descriptors.put(TEST_KEY, descriptor);
    }

    @Test
    public void testSaveAllStubConfigurations() throws TransformerException, JsonTransformationException {
        //GIVEN
        given(stubResourceHolder.getActualStubConfigJsonObject(TEST_KEY)).willReturn(jsonObject);
        //WHEN
        underTest.saveAllStubConfigurations(descriptors);
        //THEN
        verify(stubResourceHolder).getActualStubConfigJsonObject(TEST_KEY);
        verify(documentTransformer).transformToFile(jsonObject, TEST_PATH + "/" + 1 + STUB_CONFIG_JSON_POSTFIX, true);
    }

    @Test(expectedExceptions = JsonTransformationException.class)
    public void testSaveAllStubConfigurationsWhenOccurAnDocumentTransformationException() throws JsonTransformationException,
            TransformerException {
        //GIVEN
        doThrow(new TransformerException("Test")).when(documentTransformer).transformToFile(jsonObject, TEST_PATH + "/" + 1 + STUB_CONFIG_JSON_POSTFIX, true);
        given(stubResourceHolder.getActualStubConfigJsonObject(TEST_KEY)).willReturn(jsonObject);
        //WHEN
        underTest.saveAllStubConfigurations(descriptors);
        //THEN
        verify(stubResourceHolder).getActualStubConfigJsonObject(TEST_KEY);
    }
}
