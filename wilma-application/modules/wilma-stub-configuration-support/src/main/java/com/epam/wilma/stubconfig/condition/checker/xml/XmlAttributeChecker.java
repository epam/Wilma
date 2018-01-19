package com.epam.wilma.stubconfig.condition.checker.xml;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.xml.helper.XQueryExpressionEvaluator;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;
import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Checks for the value of an attribute of a node in an xml.
 * @author Tunde_Kovacs
 *
 */
@Component
public class XmlAttributeChecker implements ConditionChecker {

    private static final String FASTINFOSET_CONTENT = "application/fastinfoset";
    private static final String XML_CONTENT = "xml";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    @Autowired
    private XQueryExpressionEvaluator queryExpressionEvaluator;

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        List<Parameter> params = parameterList.getAllParameters();
        boolean result = false;
        if (params.size() == 1) {
            String contentType = request.getHeader(CONTENT_TYPE_HEADER);
            if (contentType != null && (contentType.contains(XML_CONTENT) || FASTINFOSET_CONTENT.equals(contentType))) {

                try {
                    Parameter paramater = params.iterator().next();
                    String element = paramater.getName();
                    String value = paramater.getValue();
                    result = evaluateCondition(request.getBody(), element, value);
                } catch (SaxonApiException e) {
                    throw new ConditionEvaluationFailedException("XQuery evaluation failed at request: " + request.getWilmaMessageLoggerId(), e);
                }
            }
        } else {
            throw new ConditionEvaluationFailedException("Please provide exactly one parameter!");
        }
        return result;
    }

    private boolean evaluateCondition(final String xml, final String element, final String value) throws SaxonApiException {
        boolean result;
        String node = getNode(element);
        String attribute = getAttribute(element);
        String exp = "//*[name()='" + node + "']/@" + attribute + "='" + value + "'";
        String queryResult = queryExpressionEvaluator.evaluateXQuery(xml, exp);
        result = Boolean.valueOf(removeXmlDecTagFromXQueryResult(queryResult));
        return result;
    }

    private String getNode(final String element) {
        return getPartOfElement(element, 0);
    }

    private String getAttribute(final String element) {
        return getPartOfElement(element, 1);
    }

    private String getPartOfElement(final String element, final int index) {
        String result;
        String[] splitElement = element.split("/@");
        if (splitElement.length > 1) {
            result = splitElement[index];
        } else {
            throw new ConditionEvaluationFailedException("Incorrect parameter '" + element
                    + "' provided for XmlAttributeChecker class. Please provide a valid parameter, i.e. 'node/@attribute'!");
        }
        return result;
    }

    private String removeXmlDecTagFromXQueryResult(final String xml) {
        String xmlDeclarationTag = XML_DECLARATION;
        return xml.replace(xmlDeclarationTag, "");
    }
}
