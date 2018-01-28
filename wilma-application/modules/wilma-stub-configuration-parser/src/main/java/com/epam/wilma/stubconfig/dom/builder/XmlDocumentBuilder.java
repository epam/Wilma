package com.epam.wilma.stubconfig.dom.builder;
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

import java.io.InputStream;

import javax.xml.validation.Schema;

import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.exception.DocumentBuilderException;

/**
 * Creates a {@link Document} from an XML file on a given path.
 * @author Marton_Sereg
 *
 */
public interface XmlDocumentBuilder {

    /**
     * Creates a {@link Document} from an XML {@link InputStream}.
     * @param inputStream the XML inputStream
     * @param schema the parsed XSD
     * @return the DOM document created from the XML file.
     * @throws DocumentBuilderException when XML document cannot be parsed
     */
    Document buildDocument(InputStream inputStream, Schema schema) throws DocumentBuilderException;

}
