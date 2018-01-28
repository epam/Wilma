package com.epam.wilma.test.server.compress.fis;
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
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.jvnet.fastinfoset.FastInfosetSource;

import com.epam.wilma.test.server.SystemException;
import com.epam.wilma.test.server.compress.Decompressor;

/**
 * FastInfoset implementation of the decompressor interface.
 * Decompresses an InputStream that hold the information compressed with Fast InfoSet.
 * @author Marton_Sereg
 *
 */
public class FastInfosetDecompressor implements Decompressor {

    @Override
    public String decompress(final InputStream source) {
        OutputStream xmlOutputStream = new ByteArrayOutputStream();
        // Create the transformer
        Transformer tx;
        try {
            tx = TransformerFactory.newInstance().newTransformer();
            tx.transform(new FastInfosetSource(source), new StreamResult(xmlOutputStream));
            // Transform to convert the FI document to an XML document
            return xmlOutputStream.toString();
        } catch (TransformerConfigurationException e) {
            throw new SystemException("error", e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new SystemException("error", e);
        } catch (TransformerException e) {
            throw new SystemException("error", e);
        }
    }
}
