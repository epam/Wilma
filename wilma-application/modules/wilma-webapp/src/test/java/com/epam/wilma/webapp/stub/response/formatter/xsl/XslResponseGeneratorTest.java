package com.epam.wilma.webapp.stub.response.formatter.xsl;
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

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import net.sf.saxon.s9api.SaxonApiException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.epam.wilma.webapp.domain.exception.TemplateFormattingFailedException;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

/**
 * Provides unit tests for the class {@link XslResponseGenerator}.
 * @author Tunde_Kovacs
 *
 */
public class XslResponseGeneratorTest {

    private final byte[] requestXml = new byte[1];
    private final byte[] xsl = new byte[2];
    private final byte[] templateXml = new byte[3];
    @Mock
    private XslTransformer transformer;
    @Mock
    private ByteArrayInputStreamFactory inputStreamFactory;
    @Mock
    private ByteArrayInputStream requestInputStream;
    @Mock
    private ByteArrayInputStream xslInputStream;
    @Mock
    private ByteArrayInputStream templateInputStream;

    @InjectMocks
    private XslResponseGenerator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(inputStreamFactory.createByteArrayInputStream(requestXml)).willReturn(requestInputStream);
        given(inputStreamFactory.createByteArrayInputStream(xsl)).willReturn(xslInputStream);
        given(inputStreamFactory.createByteArrayInputStream(templateXml)).willReturn(templateInputStream);
    }

    @Test
    public void testGenerateResponseShouldReturnResponse() throws SaxonApiException, SAXException {
        //GIVEN
        byte[] response = new byte[1];
        given(transformer.transform(xslInputStream, requestInputStream, templateInputStream)).willReturn(response);
        //WHEN
        byte[] actual = underTest.generateResponse(requestXml, xsl, templateXml);
        //THEN
        assertEquals(actual, response);
    }

    @Test(expectedExceptions = TemplateFormattingFailedException.class)
    public void testGenerateResponseWhenSaxonApiExceptionDuringTransformShouldThrowException() throws SaxonApiException, SAXException {
        //GIVEN
        given(transformer.transform(xslInputStream, requestInputStream, templateInputStream)).willThrow(new SaxonApiException("exception"));
        //WHEN
        underTest.generateResponse(requestXml, xsl, templateXml);
        //THEN it should throw excpetion
    }

    @Test(expectedExceptions = TemplateFormattingFailedException.class)
    public void testGenerateResponseWhenSAXExceptionDuringTransformShouldThrowException() throws SaxonApiException, SAXException {
        //GIVEN
        given(transformer.transform(xslInputStream, requestInputStream, templateInputStream)).willThrow(new SAXException());
        //WHEN
        underTest.generateResponse(requestXml, xsl, templateXml);
        //THEN it should throw excpetion
    }
}
