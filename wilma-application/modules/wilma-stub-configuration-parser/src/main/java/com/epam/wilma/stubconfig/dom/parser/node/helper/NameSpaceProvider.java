package com.epam.wilma.stubconfig.dom.parser.node.helper;
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

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Provides a namespace for a given xpath.
 * @author Tunde_Kovacs
 *
 */
@Component
public class NameSpaceProvider {

    @Autowired
    @Qualifier("stubConfigNamespaceUri")
    private String nameSpaceUri;
    @Autowired
    @Qualifier("stubConfigNamespacePrefix")
    private String nameSpacePrefix;

    /**
     * Sets the namespace of an xpath.
     * @param xpath the {@link XPath} object that will receive the namespace
     */
    public void setNameSpaceContext(final XPath xpath) {
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(final String prefix) {
                String result;
                if (prefix == null) {
                    throw new NullPointerException("Null prefix");
                } else if (nameSpacePrefix.equals(prefix)) {
                    result = nameSpaceUri;
                } else if ("xml".equals(prefix)) {
                    result = XMLConstants.XML_NS_URI;
                } else {
                    result = XMLConstants.NULL_NS_URI;
                }
                return result;
            }

            // This method isn't necessary for XPath processing.
            @Override
            public String getPrefix(final String uri) {
                throw new UnsupportedOperationException();
            }

            // This method isn't necessary for XPath processing either.
            @Override
            public Iterator<?> getPrefixes(final String uri) {
                throw new UnsupportedOperationException();
            }
        });
    }
}
