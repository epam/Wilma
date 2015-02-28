package com.epam.wilma.stubconfig.cache.saver;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.dom.transformer.DomBasedDocumentTransformer;
import com.epam.wilma.stubconfig.domain.exception.DocumentTransformationException;

/**
 * Provides unit tests for the class {@link StubConfigurationSaver}.
 * @author Tibor_Kovacs
 *
 */
public class StubConfigurationSaverTest {
    private static final String TEST_PATH = "test/path";
    private static final String TEST_KEY = "TestKey";
    private static final String STUB_CONFIG_XML_POSTFIX = "_stubConfig.xml";

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
    private Document doc;
    @Mock
    private DomBasedDocumentTransformer documentTransformer;
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
    public void testSaveAllStubConfigurations() throws TransformerException, DocumentTransformationException {
        //GIVEN
        given(stubResourceHolder.getActualStubConfigDocument(TEST_KEY)).willReturn(doc);
        //WHEN
        underTest.saveAllStubConfigurations(descriptors);
        //THEN
        verify(stubResourceHolder).getActualStubConfigDocument(TEST_KEY);
        verify(documentTransformer).transformToFile(doc, TEST_PATH + "/" + 1 + STUB_CONFIG_XML_POSTFIX, true);
    }

    @Test(expectedExceptions = DocumentTransformationException.class)
    public void testSaveAllStubConfigurationsWhenOccurAnDocumentTransformationException() throws DocumentTransformationException,
        TransformerException {
        //GIVEN
        doThrow(new TransformerException("Test")).when(documentTransformer).transformToFile(doc, TEST_PATH + "/" + 1 + STUB_CONFIG_XML_POSTFIX, true);
        given(stubResourceHolder.getActualStubConfigDocument(TEST_KEY)).willReturn(doc);
        //WHEN
        underTest.saveAllStubConfigurations(descriptors);
        //THEN
        verify(stubResourceHolder).getActualStubConfigDocument(TEST_KEY);
    }
}
