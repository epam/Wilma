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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.epam.wilma.stubconfig.dom.builder.helper.DocumentBuilderFactoryWrapper;
import com.epam.wilma.domain.stubconfig.exception.DocumentBuilderException;

/**
 * Creates a namespace aware {@link Document} from an XML file on a given path.
 * @author Marton_Sereg
 *
 */
@Component
public class NamespaceAwareXmlDocumentBuilder implements XmlDocumentBuilder {

    @Autowired
    private DocumentBuilderFactoryWrapper documentBuilderFactoryBuilder;

    @Override
    public Document buildDocument(final InputStream inputStream, final Schema schema) throws DocumentBuilderException {
        try {
            DocumentBuilderFactory dbFactory = documentBuilderFactoryBuilder.buildDocumentBuilderFactory();
            dbFactory.setNamespaceAware(true);
            dbFactory.setSchema(schema);
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();
            return document;
        } catch (Exception e) {
            throw new DocumentBuilderException("Exception while trying to parse XML document", e);
        }

    }
}
