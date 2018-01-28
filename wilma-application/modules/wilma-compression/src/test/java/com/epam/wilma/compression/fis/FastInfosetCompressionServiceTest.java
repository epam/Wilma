package com.epam.wilma.compression.fis;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.jvnet.fastinfoset.FastInfosetSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;
import com.epam.wilma.common.stream.helper.StreamResultFactory;
import com.epam.wilma.compression.fis.helper.FastInfosetSourceFactory;
import com.epam.wilma.compression.fis.helper.FastInfosetTransformerFactory;
import com.epam.wilma.compression.fis.helper.SAXDocumentSerializerFactory;
import com.epam.wilma.compression.fis.helper.SAXParserFactoryCreator;
import com.epam.wilma.domain.exception.SystemException;
import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;

/**
 * Provides unit tests for the {@link FastInfosetCompressionService} class.
 * @author Tunde_Kovacs
 *
 */
public class FastInfosetCompressionServiceTest {

    @Mock
    private SAXDocumentSerializer saxDocumentSerializer;
    @Mock
    private SAXParser saxParser;
    @Mock
    private SAXParserFactory saxParserFactory;
    @Mock
    private InputStream inputStream;
    @Mock
    private SAXParserFactoryCreator saxParserFactoryCreator;
    @Mock
    private SAXDocumentSerializerFactory documentSerializerFactory;
    @Mock
    private ByteArrayOutputStreamFactory outputStreamFactory;
    @Mock
    private ByteArrayOutputStream baos;
    @Mock
    private Transformer transformer;
    @Mock
    private FastInfosetSource fISource;
    @Mock
    private FastInfosetSourceFactory fastInfosetSourceFactory;
    @Mock
    private FastInfosetTransformerFactory transformerFactory;
    @Mock
    private StreamResultFactory streamResultFactory;

    @InjectMocks
    private FastInfosetCompressionService underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new FastInfosetCompressionService());
        MockitoAnnotations.initMocks(this);
        given(documentSerializerFactory.createSAXDocumentSerializer()).willReturn(saxDocumentSerializer);
        given(saxParserFactoryCreator.createSAXParserFactory()).willReturn(saxParserFactory);
    }

    @Test
    public void testCompressShouldCallParse() throws ParserConfigurationException, SAXException, IOException {
        //GIVEN
        given(saxParserFactory.newSAXParser()).willReturn(saxParser);
        //WHEN
        underTest.compress(inputStream);
        //THEN
        verify(saxParser).parse(inputStream, saxDocumentSerializer);
    }

    @Test
    public void testCompressShouldSetNameSpaceAwareTrue() throws ParserConfigurationException, SAXException {
        //GIVEN
        given(saxParserFactory.newSAXParser()).willReturn(saxParser);
        //WHEN
        underTest.compress(inputStream);
        //THEN
        verify(saxParserFactory).setNamespaceAware(true);
    }

    @Test
    public void testCompressShouldReturnByteArrayOutputStream() throws ParserConfigurationException, SAXException {
        //GIVEN
        given(outputStreamFactory.createByteArrayOutputStream()).willReturn(baos);
        given(saxParserFactory.newSAXParser()).willReturn(saxParser);
        //WHEN
        ByteArrayOutputStream actual = underTest.compress(inputStream);
        //THEN
        Assert.assertEquals(actual, baos);
    }

    @Test(expectedExceptions = SystemException.class)
    public void testCompressShouldThrowSystemExceptionWhenParserConfigExceptionIsCatched() throws ParserConfigurationException, SAXException {
        //GIVEN
        given(saxParserFactory.newSAXParser()).willThrow(new ParserConfigurationException());
        //WHEN
        underTest.compress(inputStream);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = SystemException.class)
    public void testCompressShouldThrowSystemExceptionWhenSAXExceptionIsCatched() throws ParserConfigurationException, SAXException {
        //GIVEN
        given(saxParserFactory.newSAXParser()).willThrow(new SAXException());
        //WHEN
        underTest.compress(inputStream);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = SystemException.class)
    public void testCompressShouldThrowSystemExceptionWhenIOExceptionIsCatched() throws ParserConfigurationException, SAXException, IOException {
        //GIVEN
        given(saxParserFactory.newSAXParser()).willReturn(saxParser);
        willThrow(new IOException()).given(saxParser).parse(inputStream, saxDocumentSerializer);
        //WHEN
        underTest.compress(inputStream);
        //THEN exception should be thrown
    }

    @Test
    public void testDecompressShouldDoTransform() throws TransformerException {
        //GIVEN
        StreamResult result = new StreamResult(new StringWriter());
        given(transformerFactory.createTransformer()).willReturn(transformer);
        given(fastInfosetSourceFactory.createFastInfosetSource(inputStream)).willReturn(fISource);
        given(streamResultFactory.createStreamResult()).willReturn(result);
        given(outputStreamFactory.createByteArrayOutputStream()).willReturn(baos);
        //WHEN
        underTest.decompress(inputStream);
        //THEN
        verify(transformer).transform(fISource, result);
    }

    @Test
    public void testDecompressShouldReturnOutputStream() throws TransformerException {
        //GIVEN
        StreamResult result = new StreamResult(new StringWriter());
        given(transformerFactory.createTransformer()).willReturn(transformer);
        given(fastInfosetSourceFactory.createFastInfosetSource(inputStream)).willReturn(fISource);
        given(streamResultFactory.createStreamResult()).willReturn(result);
        given(outputStreamFactory.createByteArrayOutputStream()).willReturn(baos);
        //WHEN
        ByteArrayOutputStream actual = underTest.decompress(inputStream);
        //THEN
        Assert.assertEquals(actual, baos);
    }

    @Test
    public void testDecompressShouldCloseTheInputStream() throws TransformerException, IOException {
        //GIVEN
        StreamResult result = new StreamResult(new StringWriter());
        given(transformerFactory.createTransformer()).willReturn(transformer);
        given(fastInfosetSourceFactory.createFastInfosetSource(inputStream)).willReturn(fISource);
        given(streamResultFactory.createStreamResult()).willReturn(result);
        given(outputStreamFactory.createByteArrayOutputStream()).willReturn(baos);
        //WHEN
        underTest.decompress(inputStream);
        //THEN
        verify(inputStream).close();
    }

    @Test(expectedExceptions = SystemException.class)
    public void testDecompressWhenTransformerConfigurationExceptionIsThrownShouldThrowException() throws TransformerConfigurationException {
        //GIVEN
        StreamResult result = new StreamResult(new StringWriter());
        given(transformerFactory.createTransformer()).willThrow(new TransformerConfigurationException());
        given(streamResultFactory.createStreamResult()).willReturn(result);
        //WHEN
        underTest.decompress(inputStream);
        //THEN it should throw exception
    }

    @Test(expectedExceptions = SystemException.class)
    public void testDecompressWhenTransformerFactoryConfigurationErrorIsThrownShouldLogError() throws TransformerConfigurationException {
        //GIVEN
        StreamResult result = new StreamResult(new StringWriter());
        given(transformerFactory.createTransformer()).willThrow(new TransformerFactoryConfigurationError());
        given(streamResultFactory.createStreamResult()).willReturn(result);
        //WHEN
        underTest.decompress(inputStream);
        //THEN it should throw exception
    }

    @Test(expectedExceptions = SystemException.class)
    public void testDecompressWhenInputStreamCloseThrowsIOExceptionShouldLogError() throws TransformerConfigurationException, IOException {
        //GIVEN
        StreamResult result = new StreamResult(new StringWriter());
        given(transformerFactory.createTransformer()).willReturn(transformer);
        given(fastInfosetSourceFactory.createFastInfosetSource(inputStream)).willReturn(fISource);
        willThrow(new IOException()).given(inputStream).close();
        given(streamResultFactory.createStreamResult()).willReturn(result);
        //WHEN
        underTest.decompress(inputStream);
        //THEN it should throw exception
    }
}
