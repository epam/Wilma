package com.epam.wilma.sequence.formatters.helper.resolver;
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

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.epam.wilma.stubconfig.dom.parser.node.helper.XPathProvider;

/**
 * Creates and evaluates an XPATH query.
 *
 * @author Balazs_Berkes
 */
@Component("sequenceXmlXpathResolver")
public class XmlXpathResolver {

    @Autowired
    private XPathProvider xPathProvider;

    /**
     * Runs the XPATH query on the given String and returns its result.
     * @param typeQuery query to run
     * @param xml xml content which the query will run on
     * @return the evaluated result
     * @throws InvalidXPathExperssionException if the XPATH cannot be evaluated
     */
    public String getValue(final String typeQuery, final String xml) {
        XPath xPath = xPathProvider.getXPath();
        InputSource source = new InputSource(new StringReader(xml));
        return evaluate(typeQuery, xPath, source);
    }

    private String evaluate(final String typeQuery, final XPath xPath, final InputSource source) {
        try {
            return xPath.evaluate(typeQuery, source);
        } catch (XPathExpressionException e) {
            throw new InvalidXPathExperssionException();
        }
    }

    /**
     * Exception is thrown when XPATH cannot be evaluated.
     */
    public static final class InvalidXPathExperssionException extends RuntimeException {
    }
}
