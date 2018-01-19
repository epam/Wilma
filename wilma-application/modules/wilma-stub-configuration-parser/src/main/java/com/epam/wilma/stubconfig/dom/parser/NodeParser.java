package com.epam.wilma.stubconfig.dom.parser;
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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Parser of a node in a DOM document.
 * @author Tunde_Kovacs
 * @param <T> Type of the parsed object
 */
public interface NodeParser<T> {

    /**
     * Parses a node in a DOM document.
     * @param node the node that is parsed
     * @param document the DOM document that is parsed
     * @return the result of the parsed node. If input parameter <tt>node</tt>
     * is null, the result is null as well.
     */
    T parseNode(Node node, Document document);
}
