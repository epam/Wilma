package com.epam.wilma.stubconfig.dom.validator.xsd;
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
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.epam.wilma.stubconfig.dom.validator.xsd.helper.DOMSourceFactory;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Provides unit tests for the class {@link XsdStubDescriptorValidator}.
 * @author Tibor_Kovacs
 *
 */
public class XsdStubDescriptorValidatorTest {

    @InjectMocks
    private XsdStubDescriptorValidator underTest;

    @Mock
    private Document document;

    @Mock
    private DOMSourceFactory domSourceFactory;

    @Mock
    private Schema schema;

    @Mock
    private DOMSource domSource;

    @Mock
    private Validator validator;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testValidateShouldValidateXMLWithXsd() throws SAXException, IOException {
        //GIVEN
        given(schema.newValidator()).willReturn(validator);
        given(domSourceFactory.newDOMSource(document)).willReturn(domSource);
        //WHEN
        underTest.validate(document, schema);
        //THEN
        verify(validator).validate(domSource);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public final void testValidateShouldThrowExceptionWhenValidationFailed() throws SAXException, IOException {
        //GIVEN
        given(schema.newValidator()).willReturn(validator);
        given(domSourceFactory.newDOMSource(document)).willReturn(domSource);
        willThrow(new IOException()).given(validator).validate(domSource);
        //WHEN
        underTest.validate(document, schema);
        //THEN exception thrown
    }
}
