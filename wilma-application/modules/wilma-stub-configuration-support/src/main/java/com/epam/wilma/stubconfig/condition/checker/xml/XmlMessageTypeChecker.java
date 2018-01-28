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
 * Class for checking for a node. In the stub configuration file the {@link XmlMessageTypeChecker}
 * must have one parameter with the desired tag name as value. If the tag declared with prefix,
 * it must be provided as well.
 * @author Tamas_Bihari
 */
@Component
public class XmlMessageTypeChecker implements ConditionChecker {

    private static final String FASTINFOSET_CONTENT = "application/fastinfoset";
    private static final String XML_CONTENT = "xml";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String XML_DECLARATION_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @Autowired
    private XQueryExpressionEvaluator xQueryEvaluator;

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        boolean result = false;
        String contentType = request.getHeader(CONTENT_TYPE_HEADER);
        if (checkContentTypeIsXml(contentType)) {
            result = Boolean.valueOf(evaluateQueryWithParamCheck(request, parameterList));
        }
        return result;
    }

    private boolean checkContentTypeIsXml(final String contentType) {
        return contentType != null && (contentType.contains(XML_CONTENT) || FASTINFOSET_CONTENT.equals(contentType));
    }

    private String evaluateQueryWithParamCheck(final WilmaHttpRequest request, final ParameterList parameterList) {
        String result = "";
        List<Parameter> params = parameterList.getAllParameters();
        if (params.size() == 1) {
            String nodeName = params.iterator().next().getValue();
            result = evaluateQuery(request, nodeName);
        } else {
            throw new ConditionEvaluationFailedException(
                    "Condition evaluation failed, the XmlRootNodeChecker must have one parameter in the stub configuration file!");
        }
        return result;
    }

    private String evaluateQuery(final WilmaHttpRequest request, final String nodeName) {
        String result;
        try {
            result = xQueryEvaluator.evaluateXQuery(request.getBody(), "count(//*[name()='" + nodeName + "'])>0");
        } catch (SaxonApiException e) {
            throw new ConditionEvaluationFailedException("XmlRootNodeChecker failed, when searched for root element with name: "
                    + nodeName + " in message:" + request.getWilmaMessageLoggerId(), e);
        }
        return removeXmlDeclarationTagFromXQueryResult(result);
    }

    private String removeXmlDeclarationTagFromXQueryResult(final String nameSpaceWithXmlDecTag) {
        String xmlDeclarationTag = XML_DECLARATION_TAG;
        return nameSpaceWithXmlDecTag.replace(xmlDeclarationTag, "");
    }
}
