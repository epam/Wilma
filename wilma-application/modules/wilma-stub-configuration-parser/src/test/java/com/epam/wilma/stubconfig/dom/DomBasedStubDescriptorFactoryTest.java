package com.epam.wilma.stubconfig.dom;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Schema;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubConfigSchema;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.dom.builder.XmlDocumentBuilder;
import com.epam.wilma.stubconfig.dom.parser.StubDescriptorParser;
import com.epam.wilma.stubconfig.dom.parser.StubResourceHolderUpdater;
import com.epam.wilma.stubconfig.dom.validator.StubDescriptorValidator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.domain.stubconfig.exception.DocumentBuilderException;

/**
 * Provides unit tests for the class {@link DomBasedStubDescriptorFactory}.
 * @author Marton_Sereg
 *
 */
public class DomBasedStubDescriptorFactoryTest {

    @InjectMocks
    private DomBasedStubDescriptorFactory underTest;
    @Mock
    private InputStream inputStream;
    @Mock
    private XmlDocumentBuilder xmlDocumentBuilder;
    @Mock
    private StubDescriptorValidator stubDescriptorValidator1;
    @Mock
    private StubDescriptorValidator stubDescriptorValidator2;
    @Mock
    private Document document;
    @Mock
    private DocumentBuilderException documentBuilderException;
    @Mock
    private StubDescriptorParser descriptorBuilder;
    @Mock
    private StubDescriptor stubDescriptor;
    @Mock
    private StubResourceHolderUpdater stubResourceHolderUpdater;
    @Mock
    private StubConfigurationAccess configurationAccess;
    @Mock
    private StubConfigSchema stubConfigSchema;

    private final List<StubDescriptorValidator> descriptorValidators = new ArrayList<>();

    @Mock
    private Schema schema;

    private StubDescriptorAttributes attributes;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        descriptorValidators.add(stubDescriptorValidator1);
        descriptorValidators.add(stubDescriptorValidator2);
        Whitebox.setInternalState(underTest, "descriptorValidators", descriptorValidators);
        attributes = new StubDescriptorAttributes("test");
        given(stubDescriptor.getAttributes()).willReturn(attributes);
        given(stubConfigSchema.getSchema()).willReturn(schema);
    }

    @Test
    public final void testBuildStubDescriptorShouldCallEveryValidatorInListAndBuildDescriptor() throws Exception {
        //GIVEN
        given(xmlDocumentBuilder.buildDocument(inputStream, schema)).willReturn(document);
        given(descriptorBuilder.parse(document)).willReturn(stubDescriptor);
        //WHEN
        StubDescriptor actual = underTest.buildStubDescriptor(inputStream);
        //THEN
        verify(stubDescriptorValidator1).validate(document, schema);
        verify(stubDescriptorValidator2).validate(document, schema);
        assertEquals(actual, stubDescriptor);
    }

    @Test(expectedExceptions = DescriptorCannotBeParsedException.class)
    public final void testBuildStubDescriptorShouldThrowExceptionWhenDocumentBuildingFails() throws Exception {
        //GIVEN
        given(xmlDocumentBuilder.buildDocument(inputStream, schema)).willThrow(documentBuilderException);
        //WHEN
        underTest.buildStubDescriptor(inputStream);
        //THEN exception thrown
    }

    @Test
    public final void testBuildStubDescriptorShouldCallUpdaterInitialize() throws Exception {
        //GIVEN
        given(xmlDocumentBuilder.buildDocument(inputStream, schema)).willReturn(document);
        given(descriptorBuilder.parse(document)).willReturn(stubDescriptor);
        //WHEN
        underTest.buildStubDescriptor(inputStream);
        //THEN
        verify(stubResourceHolderUpdater).initializeTemporaryResourceHolder();
    }

    @Test
    public final void testBuildStubDescriptorShouldCallUpdaterUpdateResourceHolder() throws Exception {
        //GIVEN
        given(xmlDocumentBuilder.buildDocument(inputStream, schema)).willReturn(document);
        given(descriptorBuilder.parse(document)).willReturn(stubDescriptor);
        //WHEN
        underTest.buildStubDescriptor(inputStream);
        //THEN
        verify(stubResourceHolderUpdater).updateResourceHolder();
    }

    @Test
    public final void testBuildStubDescriptorShouldCallUpdaterClearTempResourceHolder() throws Exception {
        //GIVEN
        given(xmlDocumentBuilder.buildDocument(inputStream, schema)).willReturn(document);
        given(descriptorBuilder.parse(document)).willReturn(stubDescriptor);
        //WHEN
        underTest.buildStubDescriptor(inputStream);
        //THEN
        verify(stubResourceHolderUpdater).clearTemporaryResourceHolder();
    }

    @Test
    public final void testBuildStubDescriptorShouldCallStubConfigurationAccess() throws Exception {
        //GIVEN
        given(xmlDocumentBuilder.buildDocument(inputStream, schema)).willReturn(document);
        given(descriptorBuilder.parse(document)).willReturn(stubDescriptor);
        //WHEN
        underTest.buildStubDescriptor(inputStream);
        //THEN
        verify(configurationAccess).setProperties();
    }
}
