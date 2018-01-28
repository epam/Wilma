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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;

/**
 * Evaluates xpath expressions.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StubConfigXPathEvaluator {

    @Autowired
    private NameSpaceProvider nameSpaceProvider;
    @Autowired
    private XPathProvider xPathProvider;

    /**
     * Finds an {@link Element} by an xpath expression. Also uses a namespace called "wilma".
     * A valid expression may need to be of the following form:
     * <pre>
     *  /wilma:root/wilma:node1/wilma/node2[1]
     * </pre>
     * @param expression the xpath expression that is evaluated
     * @param document the document that is searched
     * @return the element that matches the <tt>expression</tt>. Returns null if no
     * element matches the <tt>expression</tt>.
     */
    public Element getElementByXPath(final String expression, final Document document) {
        Element element = null;
        XPath xPath = xPathProvider.getXPath();
        try {
            nameSpaceProvider.setNameSpaceContext(xPath);
            XPathExpression expr = xPath.compile(expression);
            Node result = (Node) expr.evaluate(document, XPathConstants.NODE);
            if (result.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) result;
            }
        } catch (XPathExpressionException e) {
            throw new DescriptorCannotBeParsedException("Exception while trying to locate node in the stub configuration file", e);
        }
        return element;
    }

    /**
     * Finds an {@link NodeList} by an xpath expression. Also uses a namespace called "wilma".
     * A valid expression may need to be of the following form:
     * <pre>
     *  /wilma:root/wilma:node
     * </pre>
     * @param expression the xpath expression that is evaluated
     * @param document the document that is searched
     * @return the list of nodes that matches the <tt>expression</tt>. Returns null if no
     * element matches the <tt>expression</tt>.
     */
    public NodeList getElementsByXPath(final String expression, final Document document) {
        NodeList result = null;
        XPath xPath = xPathProvider.getXPath();
        try {
            nameSpaceProvider.setNameSpaceContext(xPath);
            XPathExpression expr = xPath.compile(expression);
            result = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new DescriptorCannotBeParsedException("Exception while trying to locate nodes in the stub configuration file", e);
        }
        return result;
    }
}
