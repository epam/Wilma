package com.epam.wilma.stubconfig.condition.checker.xml.helper;
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

import java.io.ByteArrayOutputStream;

import javax.xml.transform.sax.SAXSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

import com.epam.wilma.common.sax.helper.InputSourceFactory;
import com.epam.wilma.common.sax.helper.SAXSourceFactory;
import com.epam.wilma.common.saxon.helper.ProcessorFactory;
import com.epam.wilma.common.saxon.helper.SerializerFactory;
import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;

/**
 * Provides unit tests for the class {@link XQueryExpressionEvaluator}.
 * @author Tunde_Kovacs
 *
 */
public class XQueryExpressionEvaluatorTest {

    private static final String QUERY = "query";
    private static final String INPUT = "xml";
    @Mock
    private ProcessorFactory processorFactory;
    @Mock
    private SAXSourceFactory saxSourceFactory;
    @Mock
    private InputSourceFactory inputSourceFactory;
    @Mock
    private SerializerFactory serializerFactory;
    @Mock
    private ByteArrayOutputStreamFactory byteArrayOutputStreamFactory;
    @Mock
    private Processor processor;
    @Mock
    private XQueryCompiler xQueryCompiler;
    @Mock
    private XQueryExecutable xQueryExecutable;
    @Mock
    private XQueryEvaluator xQueryEvaluator;
    @Mock
    private SAXSource saxSource;
    @Mock
    private ByteArrayOutputStream outputStream;
    @Mock
    private Serializer serializer;
    @Mock
    private InputSource inputSource;

    @InjectMocks
    private XQueryExpressionEvaluator underTest;

    @BeforeMethod
    public void setUp() throws SaxonApiException {
        MockitoAnnotations.initMocks(this);
        given(processorFactory.createProcessor()).willReturn(processor);
        given(processor.newXQueryCompiler()).willReturn(xQueryCompiler);
        given(xQueryCompiler.compile(QUERY)).willReturn(xQueryExecutable);
        given(xQueryExecutable.load()).willReturn(xQueryEvaluator);
        given(inputSourceFactory.createInputSource(INPUT)).willReturn(inputSource);
        given(saxSourceFactory.createSAXSource(inputSource)).willReturn(saxSource);
        given(byteArrayOutputStreamFactory.createByteArrayOutputStream()).willReturn(outputStream);
    }

    @Test
    public void testEvaluateXQueryShouldReturnOutputStream() throws SaxonApiException {
        //GIVEN in setUp
        //WHEN
        String actual = underTest.evaluateXQuery(INPUT, QUERY);
        //THEN
        assertEquals(actual, outputStream.toString());
    }

    @Test
    public void testEvaluateXQueryShouldCallRun() throws SaxonApiException {
        //GIVEN in setUp
        //WHEN
        underTest.evaluateXQuery(INPUT, QUERY);
        //THEN
        verify(xQueryEvaluator).run();
    }

}
