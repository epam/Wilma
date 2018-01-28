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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.epam.wilma.common.sax.helper.InputSourceFactory;
import com.epam.wilma.common.sax.helper.SAXSourceFactory;
import com.epam.wilma.common.saxon.helper.ProcessorFactory;
import com.epam.wilma.common.stream.helper.StreamSourceFactory;
import com.epam.wilma.webapp.stub.response.formatter.xsl.XslCompiler;
import com.epam.wilma.webapp.stub.response.formatter.xsl.XslOutputProvider;
import com.epam.wilma.webapp.stub.response.formatter.xsl.helper.QNameFactory;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

//CHECKSTYLE OFF
// class fan-out complexity is 22 meanwhile the max is 20 - unfortunately this class need to be such complex.
/**
 * Provides transformation of an input stream based on a template and an xsl.
 * This transformer will attach all the previous messages from the history.
 * @author Balazs_Berkes
 */
@Component
public class SequenceAwareXslTransformer {
//CHECKSTYLE ON
    private static final String REQUEST_PARAMETER_NAME = "request";
    @Autowired
    private XslCompiler xslCompiler;
    @Autowired
    private XslOutputProvider xslOutputProvider;
    @Autowired
    private StreamSourceFactory streamSourceFactory;
    @Autowired
    private SAXSourceFactory saxSourceFactory;
    @Autowired
    private ProcessorFactory processorFactory;
    @Autowired
    private InputSourceFactory inputSourceFactory;
    @Autowired
    private QNameFactory qNameFactory;
    @Autowired
    private ByteArrayInputStreamFactory inputStreamFactory;

    /**
     * Transforms an inputstream based on a template and an xsl.
     * @param xslInputStream the xsl as an {@link InputStream}
     * @param requestInputStream the request as an {@link InputStream}
     * @param templateInputStream the template as an {@link InputStream}
     * @param nameToXml the names identifying a request or response and their bodies
     * @return the result of the transformation
     * @throws SaxonApiException it is thrown if an exception occurs during the xsl transformation
     * @throws SAXException it is thrown if an exception occurs during the xsl transformation
     */
    public byte[] transform(final InputStream xslInputStream, final InputStream requestInputStream, final InputStream templateInputStream,
            final Map<String, String> nameToXml) throws SaxonApiException, SAXException {
        Processor processor = processorFactory.createProcessor();
        XMLReader xmlReader = createXMLReader();
        //xsl compilation
        XsltExecutable xsltExecutable = xslCompiler.compileXsl(xslInputStream, processor);
        XsltTransformer xsltTransformer = xsltExecutable.load();
        //set the output
        ByteArrayOutputStream output = xslOutputProvider.getOutput(xsltTransformer);
        //set the request
        setRequest(requestInputStream, processor, xsltTransformer);
        setSessionEntities(nameToXml, processor, xsltTransformer);
        //set the template
        setTemplate(templateInputStream, processor, xmlReader, xsltTransformer);
        xsltTransformer.transform();
        return output.toByteArray();
    }

    private void setSessionEntities(final Map<String, String> nameToXml, final Processor processor, final XsltTransformer xsltTransformer)
        throws SaxonApiException {
        for (Entry<String, String> entry : nameToXml.entrySet()) {
            InputStream parameterInputStream = inputStreamFactory.createByteArrayInputStream(entry.getValue().getBytes());
            setParameter(parameterInputStream, processor, xsltTransformer, entry.getKey());
        }
    }

    private void setTemplate(final InputStream templateInputStream, final Processor processor, final XMLReader xmlReader,
            final XsltTransformer xsltTransformer) throws SaxonApiException {
        InputSource inputSource = inputSourceFactory.createInputSource(templateInputStream);
        Source templateSource = saxSourceFactory.createSAXSource(xmlReader, inputSource);
        XdmNode templateNode = processor.newDocumentBuilder().build(templateSource);
        xsltTransformer.setInitialContextNode(templateNode);
    }

    private void setParameter(final InputStream parameterInputStream, final Processor processor, final XsltTransformer xsltTransformer,
            final String parameterName) throws SaxonApiException {
        StreamSource requestStreamSource = streamSourceFactory.createStreamSource(parameterInputStream);
        XdmNode requestDocument = processor.newDocumentBuilder().build(requestStreamSource);
        QName parameter = qNameFactory.createQName(parameterName);
        xsltTransformer.setParameter(parameter, requestDocument);
    }

    private void setRequest(final InputStream requestInputStream, final Processor processor, final XsltTransformer xsltTransformer)
        throws SaxonApiException {
        setParameter(requestInputStream, processor, xsltTransformer, REQUEST_PARAMETER_NAME);
    }

    XMLReader createXMLReader() throws SAXException {
        return XMLReaderFactory.createXMLReader();
    }
}
