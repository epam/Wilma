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

import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.saxon.helper.SerializerFactory;
import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;

/**
 * Provides the output for an xslt transformer.
 * @author Tunde_Kovacs
 *
 */
@Component
public class XslOutputProvider {

    @Autowired
    private ByteArrayOutputStreamFactory byteArrayOutputStreamFactory;
    @Autowired
    private SerializerFactory serializerFactory;

    /** Retrieves the output for a given xslt transformer.
     * @param xsltTransformer the {@link XsltTransformer} that will receive the output
     * @return the output as a {@link ByteArrayOutputStream}
     */
    public ByteArrayOutputStream getOutput(final XsltTransformer xsltTransformer) {
        Serializer serializer = serializerFactory.createSerializer();
        serializer.setOutputProperty(Serializer.Property.METHOD, "xml");
        serializer.setOutputProperty(Serializer.Property.INDENT, "no");
        ByteArrayOutputStream output = byteArrayOutputStreamFactory.createByteArrayOutputStream();
        serializer.setOutputStream(output);
        xsltTransformer.setDestination(serializer);
        return output;
    }

}
