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
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Checks the message body as XML over a given xquery.
 * @author Tunde_Kovacs
 *
 */
@Component
public class CustomXQueryBodyChecker implements ConditionChecker {

    private static final String FASTINFOSET_CONTENT = "application/fastinfoset";
    private static final String XML_CONTENT = "xml";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final Logger logger = LoggerFactory.getLogger(CustomXQueryBodyChecker.class);
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
                    String value = paramater.getValue();
                    result = evaluateCondition(request.getBody(), value);
                } catch (SaxonApiException e) {
                    throw new ConditionEvaluationFailedException("XQuery evaluation failed at request: " + request.getWilmaMessageLoggerId(), e);
                }
            }
        } else {
            throw new ConditionEvaluationFailedException("Please provide exactly one parameter in Stub Configuration!");
        }
        return result;
    }

    private boolean evaluateCondition(final String xml, final String query) throws SaxonApiException {
        boolean result;
        String queryResult = queryExpressionEvaluator.evaluateXQuery(xml, query);
        String fromXQueryResult = "";
        try {
            fromXQueryResult = removeXmlDecTagFromXQueryResult(queryResult);
            result = BooleanUtils.toBooleanObject(fromXQueryResult);
        } catch (NullPointerException e) {
            logger.debug("Expected result of the XQuery evaluation was true or false, but it returned with the following:" + fromXQueryResult
                    + ".\n Thus the condition evaluated to true!", e);
            result = true;
        }
        return result;
    }

    private String removeXmlDecTagFromXQueryResult(final String xml) {
        String xmlDeclarationTag = XML_DECLARATION;
        return xml.replace(xmlDeclarationTag, "");
    }
}
