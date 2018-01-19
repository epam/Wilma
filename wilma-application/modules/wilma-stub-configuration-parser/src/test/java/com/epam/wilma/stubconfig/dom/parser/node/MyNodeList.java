package com.epam.wilma.stubconfig.dom.parser.node;
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

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Custom implementation of {@link NodeList} interface.
 * Needed for unit testing purposes.
 * @author Tunde_Kovacs
 *
 */
public class MyNodeList implements NodeList {

    private final List<Node> arrayNode;

    /**
     * Constructs a new node list.
     * @param arrayNode list of nodes it uses to build a new list.
     */
    public MyNodeList(final List<Node> arrayNode) {
        super();
        this.arrayNode = arrayNode;
    }

    @Override
    public int getLength() {
        return arrayNode.size();
    }

    @Override
    public Node item(final int index) {
        return arrayNode.get(index);
    }

}
