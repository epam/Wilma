package com.epam.wilma.webapp.stub.response.formatter.xsl;
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
import com.epam.wilma.webapp.stub.response.formatter.xsl.helper.QNameFactory;

/**
 * Provides transformation of an inputstream based on a template and an xsl.
 * @author Tunde_Kovacs
 *
 */
@Component
public class XslTransformer {

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

    /**
     * Transforms an inputstream based on a template and an xsl.
     * @param xslInputStream the xsl as an {@link InputStream}
     * @param requestInputStream the request as an {@link InputStream}
     * @param templateInputStream the template as an {@link InputStream}
     * @return the result of the transformation
     * @throws SaxonApiException it is thrown if an exception occurs during the xsl transformation
     * @throws SAXException it is thrown if an exception occurs during the xsl transformation
     */
    public byte[] transform(final InputStream xslInputStream, final InputStream requestInputStream, final InputStream templateInputStream)
        throws SaxonApiException, SAXException {
        Processor processor = processorFactory.createProcessor();
        XMLReader xmlReader = createXMLReader();
        //xsl compilation
        XsltExecutable xsltExecutable = xslCompiler.compileXsl(xslInputStream, processor);
        XsltTransformer xsltTransformer = xsltExecutable.load();
        //set the output
        ByteArrayOutputStream output = xslOutputProvider.getOutput(xsltTransformer);
        //set the request
        setRequest(requestInputStream, processor, xsltTransformer);
        //set the template
        setTemplate(templateInputStream, processor, xmlReader, xsltTransformer);
        xsltTransformer.transform();
        return output.toByteArray();
    }

    private void setTemplate(final InputStream templateInputStream, final Processor processor, final XMLReader xmlReader,
            final XsltTransformer xsltTransformer) throws SaxonApiException {
        InputSource inputSource = inputSourceFactory.createInputSource(templateInputStream);
        Source templateSource = saxSourceFactory.createSAXSource(xmlReader, inputSource);
        XdmNode templateNode = processor.newDocumentBuilder().build(templateSource);
        xsltTransformer.setInitialContextNode(templateNode);
    }

    private void setRequest(final InputStream requestInputStream, final Processor processor, final XsltTransformer xsltTransformer)
        throws SaxonApiException {
        StreamSource requestStreamSource = streamSourceFactory.createStreamSource(requestInputStream);
        XdmNode requestDocument = processor.newDocumentBuilder().build(requestStreamSource);
        QName requestName = qNameFactory.createQName(REQUEST_PARAMETER_NAME);
        xsltTransformer.setParameter(requestName, requestDocument);
    }

    XMLReader createXMLReader() throws SAXException {
        return XMLReaderFactory.createXMLReader();
    }
}
