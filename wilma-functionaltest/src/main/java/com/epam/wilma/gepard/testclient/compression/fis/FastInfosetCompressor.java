package com.epam.wilma.gepard.testclient.compression.fis;
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

import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;
import org.jvnet.fastinfoset.FastInfosetSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * This class compresses/decompresses the input stream with FastInfoset.
 */
public class FastInfosetCompressor {

    /**
     * Compress the input stream with FastInfoset.
     *
     * @param source is the starting input stream
     * @return with the compressed result
     * @throws ParserConfigurationException in case of compression problem
     * @throws SAXException in case of compression problem
     * @throws IOException in case of compression problem
     */
    public InputStream compress(final InputStream source) throws ParserConfigurationException, SAXException, IOException {
        OutputStream fis = new ByteArrayOutputStream();
        SAXDocumentSerializer saxDocumentSerializer = new SAXDocumentSerializer();
        saxDocumentSerializer.setOutputStream(fis);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(source, saxDocumentSerializer);
        return new ByteArrayInputStream(((ByteArrayOutputStream) fis).toByteArray());
    }

    /**
     * De-Compress the input stream from FastInfoset.
     *
     * @param source is the starting input stream
     * @return with the de-compressed result
     * @throws TransformerException in case of compression problem
     * @throws IOException in case of compression problem
     */
    public String decompress(final InputStream source) throws TransformerException, IOException {
        StreamResult streamResult = new StreamResult(new StringWriter());
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.setOutputProperty(OutputKeys.INDENT, "yes");
        tx.transform(new FastInfosetSource(source), streamResult);
        source.close();
        return streamResult.getWriter().toString();
    }
}
