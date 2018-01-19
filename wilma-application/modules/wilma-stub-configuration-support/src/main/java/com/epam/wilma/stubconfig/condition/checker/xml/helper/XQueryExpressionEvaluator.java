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

import java.io.ByteArrayOutputStream;

import javax.xml.transform.sax.SAXSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.sax.helper.InputSourceFactory;
import com.epam.wilma.common.sax.helper.SAXSourceFactory;
import com.epam.wilma.common.saxon.helper.ProcessorFactory;
import com.epam.wilma.common.saxon.helper.SaxonCompilerErrorListener;
import com.epam.wilma.common.saxon.helper.SerializerFactory;
import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;

/**
 * Class for XQuery executions.
 * @author Tamas_Bihari
 *
 */
@Component
public class XQueryExpressionEvaluator {

    @Autowired
    private ProcessorFactory processorFactory;
    @Autowired
    private SAXSourceFactory saxSourceFactory;
    @Autowired
    private InputSourceFactory inputSourceFactory;
    @Autowired
    private SerializerFactory serializerFactory;
    @Autowired
    private ByteArrayOutputStreamFactory byteArrayOutputStreamFactory;
    @Autowired
    private SaxonCompilerErrorListener errorListener;

    /**
     * Executes a given XQuery expression on the given XML.
     * @param xml is the XQuery data source
     * @param query is the query expression
     * @return with the query result as String
     * @throws SaxonApiException was thrown when the XQuery execution is failed
     */
    public String evaluateXQuery(final String xml, final String query) throws SaxonApiException {
        Processor processor = processorFactory.createProcessor();
        XQueryCompiler xqueryCompiler = processor.newXQueryCompiler();
        xqueryCompiler.setErrorListener(errorListener);
        XQueryExecutable xqueryExec = xqueryCompiler.compile(query);
        XQueryEvaluator xqueryEval = xqueryExec.load();
        xqueryEval.setErrorListener(errorListener);
        SAXSource requestXml = saxSourceFactory.createSAXSource(inputSourceFactory.createInputSource(xml));
        xqueryEval.setSource(requestXml);
        ByteArrayOutputStream baos = byteArrayOutputStreamFactory.createByteArrayOutputStream();
        Serializer ser = serializerFactory.createSerializer(baos);
        xqueryEval.setDestination(ser);
        xqueryEval.run();
        return baos.toString();
    }
}
