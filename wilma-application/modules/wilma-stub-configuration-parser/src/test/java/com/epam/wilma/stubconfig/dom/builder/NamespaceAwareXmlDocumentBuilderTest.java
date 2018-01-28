package com.epam.wilma.stubconfig.dom.builder;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.epam.wilma.stubconfig.dom.builder.helper.DocumentBuilderFactoryWrapper;
import com.epam.wilma.domain.stubconfig.exception.DocumentBuilderException;

/**
 * Provides unit tests for the class {@link NamespaceAwareXmlDocumentBuilder}.
 * @author Tibor_Kovacs
 *
 */
public class NamespaceAwareXmlDocumentBuilderTest {

    @InjectMocks
    private NamespaceAwareXmlDocumentBuilder underTest;

    @Mock
    private DocumentBuilderFactoryWrapper documentBuilderFactoryBuilder;

    @Mock
    private DocumentBuilderFactory documentBuilderFactory;

    @Mock
    private InputStream inputStream;

    @Mock
    private DocumentBuilder documentBuilder;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Document document;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testBuildDocumentShouldParseInputStreamToDocument() throws Exception {
        //GIVEN
        given(documentBuilderFactoryBuilder.buildDocumentBuilderFactory()).willReturn(documentBuilderFactory);
        given(documentBuilderFactory.newDocumentBuilder()).willReturn(documentBuilder);
        given(documentBuilder.parse(inputStream)).willReturn(document);
        //WHEN
        Document actual = underTest.buildDocument(inputStream, null);
        //THEN
        verify(documentBuilderFactory).setNamespaceAware(true);
        verify(documentBuilder).parse(inputStream);
        assertEquals(actual, document);
    }

    @Test(expectedExceptions = DocumentBuilderException.class)
    public final void testBuildDocumentShouldThrowExceptionWhenDocumentBuilderCannotBeCreated() throws Exception {
        //GIVEN
        given(documentBuilderFactoryBuilder.buildDocumentBuilderFactory()).willReturn(documentBuilderFactory);
        given(documentBuilderFactory.newDocumentBuilder()).willThrow(new ParserConfigurationException());
        //WHEN
        underTest.buildDocument(inputStream, null);
        //THEN exception thrown
    }

    @Test(expectedExceptions = DocumentBuilderException.class)
    public final void testBuildDocumentShouldThrowExceptionWhenParsingFails() throws Exception {
        //GIVEN
        given(documentBuilderFactoryBuilder.buildDocumentBuilderFactory()).willReturn(documentBuilderFactory);
        given(documentBuilderFactory.newDocumentBuilder()).willReturn(documentBuilder);
        given(documentBuilder.parse(inputStream)).willThrow(new SAXException());
        //WHEN
        underTest.buildDocument(inputStream, null);
        //THEN exception thrown
    }
}
