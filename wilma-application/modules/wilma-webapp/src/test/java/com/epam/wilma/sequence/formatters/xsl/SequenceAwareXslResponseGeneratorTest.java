package com.epam.wilma.sequence.formatters.xsl;
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

import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;
import net.sf.saxon.s9api.SaxonApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Unit test for {@link SequenceAwareXslResponseGenerator}.
 *
 * @author Balazs_Berkes
 */
public class SequenceAwareXslResponseGeneratorTest {

    private final byte[] requestXml = new byte[1];
    private final byte[] xsl = new byte[2];
    private final byte[] templateXml = new byte[3];
    @Mock
    private SequenceAwareXslTransformer transformer;
    @Mock
    private ByteArrayInputStreamFactory inputStreamFactory;
    @Mock
    private ByteArrayInputStream requestInputStream;
    @Mock
    private ByteArrayInputStream xslInputStream;
    @Mock
    private ByteArrayInputStream templateInputStream;
    @Mock
    private Map<String, String> nameToXml;

    @InjectMocks
    private SequenceAwareXslResponseGenerator underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(inputStreamFactory.createByteArrayInputStream(requestXml)).willReturn(requestInputStream);
        given(inputStreamFactory.createByteArrayInputStream(xsl)).willReturn(xslInputStream);
        given(inputStreamFactory.createByteArrayInputStream(templateXml)).willReturn(templateInputStream);
    }

    @Test
    public void testGenerateResponseShouldReturnResponse() throws SaxonApiException, SAXException {
        //GIVEN
        byte[] response = new byte[1];
        given(transformer.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml)).willReturn(response);
        //WHEN
        byte[] actual = underTest.generateResponse(requestXml, xsl, templateXml, nameToXml);
        //THEN
        assertEquals(response, actual);
    }

    @Test
    public void testGenerateResponseWhenSaxonApiExceptionDuringTransformShouldThrowException() {
        Assertions.assertThrows(ResponseFormattingFailedException.class, () -> {
            //GIVEN
            given(transformer.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml))
                    .willThrow(new SaxonApiException("exception"));
            //WHEN
            underTest.generateResponse(requestXml, xsl, templateXml, nameToXml);
            //THEN it should throw exception
        });
    }

    @Test
    public void testGenerateResponseWhenSAXExceptionDuringTransformShouldThrowException() {
        Assertions.assertThrows(ResponseFormattingFailedException.class, () -> {
            // GIVEN
            given(transformer.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml)).willThrow(new SAXException());
            //WHEN
            underTest.generateResponse(requestXml, xsl, templateXml, nameToXml);
            //THEN it should throw exception
        });
    }
}
