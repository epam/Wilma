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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.jvnet.fastinfoset.FastInfosetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;
import com.epam.wilma.common.stream.helper.StreamResultFactory;
import com.epam.wilma.compression.CompressionService;
import com.epam.wilma.compression.fis.helper.FastInfosetSourceFactory;
import com.epam.wilma.compression.fis.helper.FastInfosetTransformerFactory;
import com.epam.wilma.compression.fis.helper.SAXDocumentSerializerFactory;
import com.epam.wilma.compression.fis.helper.SAXParserFactoryCreator;
import com.epam.wilma.domain.exception.SystemException;
import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;

/**
 * Compresses and decompresses an inputStream into/from fastinfoset.
 * @author Tunde_Kovacs
 *
 */
@Component
public class FastInfosetCompressionService implements CompressionService {

    @Autowired
    private SAXParserFactoryCreator saxParserCreator;
    @Autowired
    private SAXDocumentSerializerFactory documentSerializerFactory;
    @Autowired
    private ByteArrayOutputStreamFactory outputStreamFactory;
    @Autowired
    private FastInfosetSourceFactory fastInfosetSourceFactory;
    @Autowired
    private FastInfosetTransformerFactory fastInfosetTransformerFactory;
    @Autowired
    private StreamResultFactory streamResultFactory;

    @Override
    public ByteArrayOutputStream compress(final InputStream inputStream) {
        InputStream source = inputStream;
        try {
            ByteArrayOutputStream fis = outputStreamFactory.createByteArrayOutputStream();
            SAXDocumentSerializer saxDocumentSerializer = documentSerializerFactory.createSAXDocumentSerializer();
            saxDocumentSerializer.setOutputStream(fis);
            SAXParserFactory saxParserFactory = saxParserCreator.createSAXParserFactory();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(source, saxDocumentSerializer);
            return fis;
        } catch (Exception e) {
            throw new SystemException("Could not perform fastinfoset compression!", e);
        }
    }

    @Override
    public ByteArrayOutputStream decompress(final InputStream inputStream) {
        StreamResult streamResult = streamResultFactory.createStreamResult();
        InputStream fiDocument = inputStream;
        ByteArrayOutputStream baos = outputStreamFactory.createByteArrayOutputStream();
        try {
            Transformer tx = fastInfosetTransformerFactory.createTransformer();
            tx.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            tx.setOutputProperty(OutputKeys.INDENT, "yes");
            FastInfosetSource fastInfosetSource = fastInfosetSourceFactory.createFastInfosetSource(fiDocument);
            tx.transform(fastInfosetSource, streamResult);
            fiDocument.close();
            baos.write(streamResult.getWriter().toString().getBytes());
        } catch (TransformerFactoryConfigurationError | Exception e) {
            throw new SystemException("Could not perform fastinfoset decompression!", e);
        }
        return baos;
    }

}
