package com.epam.wilma.core.processor.entity;

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

import com.epam.wilma.common.stream.helper.StreamResultFactory;
import com.epam.wilma.common.stream.helper.StreamSourceFactory;
import com.epam.wilma.core.processor.entity.helper.XmlTransformerFactory;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link PrettyPrintProcessor}.
 *
 * @author Tunde_Kovacs
 */
public class PrettyPrintProcessorTest {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_SOAPXML = "application/soap+xml";
    private static final String CONTENT_TYPE_SVGXML = "image/svg+xml";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String BODY = "body";
    @Mock
    private StreamResultFactory streamResultFactory;
    @Mock
    private StreamSourceFactory streamSourceFactory;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private Transformer transformer;
    @Mock
    private XmlTransformerFactory transformerFactory;
    @Mock
    private StreamResult streamResult;
    @Mock
    private StreamSource streamSource;
    @Mock
    private Writer writer;
    @Mock
    private Logger logger;

    @InjectMocks
    private PrettyPrintProcessor underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(request.getBody()).willReturn(BODY);
    }

    @Test
    public void testProcessShouldNotOnEmptyBody() throws ApplicationException {
        //GIVEN
        given(request.getBody()).willReturn(null);
        //WHEN
        underTest.process(request);
        //THEN
        verify(request, never()).setBody(Mockito.anyString());
    }

    @Test
    public void testProcessShouldCallTransform() throws ApplicationException, TransformerException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_SOAPXML);
        given(transformerFactory.createTransformer()).willReturn(transformer);
        given(streamResultFactory.createStreamResult()).willReturn(streamResult);
        given(streamSourceFactory.createStreamSourceFromString(BODY)).willReturn(streamSource);
        given(streamResult.getWriter()).willReturn(writer);
        //WHEN
        underTest.process(request);
        //THEN
        verify(transformer).transform(streamSource, streamResult);
    }

    @Test
    public void testProcessShouldNotCallTransformWhenImageXml() throws ApplicationException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_SVGXML);
        //WHEN
        underTest.process(request);
        //THEN
        verify(request, never()).setBody(Mockito.anyString());
    }

    @Test
    public void testProcessShouldSetRequestBodyXML() throws ApplicationException, TransformerException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_SOAPXML);
        given(transformerFactory.createTransformer()).willReturn(transformer);
        given(streamResultFactory.createStreamResult()).willReturn(streamResult);
        given(streamSourceFactory.createStreamSourceFromString(BODY)).willReturn(streamSource);
        given(streamResult.getWriter()).willReturn(writer);
        given(writer.toString()).willReturn(BODY);
        //WHEN
        underTest.process(request);
        //THEN
        verify(request).setBody(BODY);
    }

    @Test
    public void testProcessWhenRequestBodyIsNotXmlAndNotJsonShouldNotDoAnything() throws ApplicationException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn("application/fastinfoset");
        //WHEN
        underTest.process(request);
        //THEN
        verify(request, never()).setBody(Mockito.anyString());
    }

    @Test
    public void testProcessWhenContentTypeIsNullShouldNotDoAnything() throws ApplicationException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        underTest.process(request);
        //THEN
        verify(request, never()).setBody(Mockito.anyString());
    }

    @Test
    public void testProcessWhenTransformerFactoryThrowsExceptionShouldLogError() throws ApplicationException, TransformerConfigurationException {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_SOAPXML);
        given(transformerFactory.createTransformer()).willThrow(new TransformerConfigurationException(""));
        //WHEN
        underTest.process(request);
        //THEN
        verify(logger).error(Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testProcessShouldSetRequestBodyJSON() throws ApplicationException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_JSON);
        given(request.getBody()).willReturn("{\"test\":test}");
        //WHEN
        underTest.process(request);
        //THEN
        verify(request).setBody("{\n  \"test\": \"test\"\n}");
    }

    @Test
    public void testProcessShouldNotSetRequestBodyJSONWhenBodyIsNull() throws ApplicationException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_JSON);
        given(request.getBody()).willReturn(null);
        //WHEN
        underTest.process(request);
        //THEN
        verify(request, never()).setBody(Mockito.anyString());
    }

    @Test
    public void testProcessWhenJsonParserThrowsExceptionShouldLogError() throws ApplicationException {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "logger", logger);
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_JSON);
        given(request.getBody()).willReturn("{foo: ['bar', 'baz',]*/*-}");
        //WHEN
        underTest.process(request);
        //THEN
        verify(logger).error(Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testProcessWhenJsonParserThrowsExceptionShouldLeaveOriginalBodyAsItIs() throws ApplicationException {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "logger", logger);
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(CONTENT_TYPE_JSON);
        given(request.getBody()).willReturn("invalidjson{:}");
        //WHEN
        underTest.process(request);
        //THEN
        assertEquals("invalidjson{:}", request.getBody());
    }

}
