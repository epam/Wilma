package com.epam.wilma.stubconfig.dom.parser.xsd;
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
import static org.testng.AssertJUnit.assertNotNull;

import java.net.URL;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.epam.wilma.common.helper.ResourceLoader;
import com.epam.wilma.stubconfig.dom.validator.xsd.helper.SchemaFactoryBuilder;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Unit test for {@link StubConfigSchemaParser}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class StubConfigSchemaParserTest {

    private static final String STUB_CONFIG_SCHEMA_LOCATION = "location";

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private SchemaFactoryBuilder schemaFactoryBuilder;

    @InjectMocks
    private StubConfigSchemaParser underTest;

    @Mock
    private SchemaFactory schemaFactory;
    @Mock
    private Schema schema;

    private URL url;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "stubConfigSchemaLocation", STUB_CONFIG_SCHEMA_LOCATION);
        given(resourceLoader.loadResource(STUB_CONFIG_SCHEMA_LOCATION)).willReturn(url);
    }

    @Test
    public void testParseSchemaShouldNotReturnNullWhenSchemaCanBeParsed() throws SAXException {
        //GIVEN
        given(schemaFactoryBuilder.buildSchemaFactory()).willReturn(schemaFactory);
        given(schemaFactory.newSchema(url)).willReturn(schema);
        //WHEN
        Schema result = underTest.parseSchema();
        //THEN
        assertNotNull(result);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testParseSchemaShouldThrowDescriptorValidationFailedExceptionWhenSchemaCanNotBeParsed() throws SAXException {
        //GIVEN
        given(schemaFactoryBuilder.buildSchemaFactory()).willReturn(schemaFactory);
        given(schemaFactory.newSchema(url)).willThrow(new SAXException());
        //WHEN
        underTest.parseSchema();
        //THEN exception is thrown
    }

}
