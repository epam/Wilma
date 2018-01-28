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

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.epam.wilma.common.sax.helper.InputSourceFactory;
import com.epam.wilma.common.sax.helper.SAXSourceFactory;
import com.epam.wilma.common.saxon.helper.ProcessorFactory;
import com.epam.wilma.common.stream.helper.StreamSourceFactory;
import com.epam.wilma.webapp.stub.response.formatter.xsl.XslCompiler;
import com.epam.wilma.webapp.stub.response.formatter.xsl.XslOutputProvider;
import com.epam.wilma.webapp.stub.response.formatter.xsl.helper.QNameFactory;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

/**
 * Unit test for {@link SessionAwareXslTransformer}.
 * @author Balazs_Berkes
 */
public class SequenceAwareXslTransformerTest {

    @Mock
    private XslCompiler xslCompiler;
    @Mock
    private XslOutputProvider xslOutputProvider;
    @Mock
    private StreamSourceFactory streamSourceFactory;
    @Mock
    private SAXSourceFactory saxSourceFactory;
    @Mock
    private ProcessorFactory processorFactory;
    @Mock
    private InputSourceFactory inputSourceFactory;
    @Mock
    private QNameFactory qNameFactory;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Processor processor;
    @Mock
    private XMLReader xmlReader;
    @Mock
    private XsltExecutable xsltExecutable;
    @Mock
    private ByteArrayOutputStream outputStream;
    @Mock
    private XsltTransformer xsltTransformer;
    @Mock
    private InputStream xslInputStream;
    @Mock
    private InputStream requestInputStream;
    @Mock
    private InputStream templateInputStream;
    @Mock
    private XdmNode requestDocument;
    @Mock
    private QName requestName;
    @Mock
    private InputSource inputSource;
    @Mock
    private QName historyRequestName;
    @Mock
    private QName historyResponseName;
    @Mock
    private SAXSource templateSource;
    @Mock
    private XdmNode templateNode;
    @Mock
    private ByteArrayInputStreamFactory inputStreamFactory;
    @Mock
    private Map<String, String> nameToXml;

    @InjectMocks
    private SequenceAwareXslTransformer underTest;

    @BeforeMethod
    public void setUp() throws SAXException {
        underTest = Mockito.spy(new SequenceAwareXslTransformer());
        MockitoAnnotations.initMocks(this);
        given(processorFactory.createProcessor()).willReturn(processor);
        doReturn(xmlReader).when(underTest).createXMLReader();

        Map<String, String> historyEntries = new HashMap<>();
        historyEntries.put("historyRequest", "historyRequestContent");
        historyEntries.put("historyResponse", "historyResponseContent");

        given(nameToXml.entrySet()).willReturn(historyEntries.entrySet());
    }

    @Test
    public void testTransformShouldSetRequestAsParameter() throws SaxonApiException, SAXException {
        //GIVEN
        given(xslCompiler.compileXsl(xslInputStream, processor)).willReturn(xsltExecutable);
        given(xsltExecutable.load()).willReturn(xsltTransformer);
        given(xslOutputProvider.getOutput(xsltTransformer)).willReturn(outputStream);
        givenParameter("request", requestName);
        setTemplateMocks();
        //WHEN
        underTest.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml);
        //THEN
        verify(xsltTransformer).setParameter(requestName, requestDocument);
    }

    @Test
    public void testTransformShouldSetHistoryElementsAsParameter() throws SaxonApiException, SAXException {
        //GIVEN
        given(xslCompiler.compileXsl(xslInputStream, processor)).willReturn(xsltExecutable);
        given(xsltExecutable.load()).willReturn(xsltTransformer);
        given(xslOutputProvider.getOutput(xsltTransformer)).willReturn(outputStream);
        givenParameter("request", requestName);
        givenParameter("historyRequest", historyRequestName);
        givenParameter("historyResponse", historyResponseName);
        setTemplateMocks();
        //WHEN
        underTest.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml);
        //THEN
        verify(xsltTransformer).setParameter(requestName, requestDocument);
        verify(xsltTransformer).setParameter(historyRequestName, requestDocument);
        verify(xsltTransformer).setParameter(historyResponseName, requestDocument);
    }

    @Test
    public void testTransformShouldSetTemplateAsInitialContextNode() throws SaxonApiException, SAXException {
        //GIVEN
        given(xslCompiler.compileXsl(xslInputStream, processor)).willReturn(xsltExecutable);
        given(xsltExecutable.load()).willReturn(xsltTransformer);
        given(xslOutputProvider.getOutput(xsltTransformer)).willReturn(outputStream);
        givenParameter("request", requestName);
        setTemplateMocks();
        //WHEN
        underTest.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml);
        //THEN
        verify(xsltTransformer).setInitialContextNode(templateNode);
    }

    @Test
    public void testTransformShouldDoTransform() throws SaxonApiException, SAXException {
        //GIVEN
        given(xslCompiler.compileXsl(xslInputStream, processor)).willReturn(xsltExecutable);
        given(xsltExecutable.load()).willReturn(xsltTransformer);
        given(xslOutputProvider.getOutput(xsltTransformer)).willReturn(outputStream);
        givenParameter("request", requestName);
        setTemplateMocks();
        //WHEN
        underTest.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml);
        //THEN
        verify(xsltTransformer).transform();
    }

    @Test
    public void testTransformShouldReturnOutput() throws SaxonApiException, SAXException {
        //GIVEN
        given(xslCompiler.compileXsl(xslInputStream, processor)).willReturn(xsltExecutable);
        given(xsltExecutable.load()).willReturn(xsltTransformer);
        given(xslOutputProvider.getOutput(xsltTransformer)).willReturn(outputStream);
        givenParameter("request", requestName);
        setTemplateMocks();
        //WHEN
        byte[] actual = underTest.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml);
        //THEN
        assertEquals(actual, outputStream.toByteArray());
    }

    private void givenParameter(final String parameterName, final QName qName) throws SaxonApiException {
        given(streamSourceFactory.createStreamSource(requestInputStream)).willReturn(new StreamSource());
        given(processor.newDocumentBuilder().build(any(StreamSource.class))).willReturn(requestDocument);
        given(qNameFactory.createQName(parameterName)).willReturn(qName);
    }

    private void setTemplateMocks() throws SaxonApiException {
        given(inputSourceFactory.createInputSource(templateInputStream)).willReturn(inputSource);
        given(saxSourceFactory.createSAXSource(xmlReader, inputSource)).willReturn(templateSource);
        given(processor.newDocumentBuilder().build(templateSource)).willReturn(templateNode);
    }
}
