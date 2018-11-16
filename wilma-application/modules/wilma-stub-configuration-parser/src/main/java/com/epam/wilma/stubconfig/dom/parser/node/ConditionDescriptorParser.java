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

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.dialog.condition.CompositeCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionType;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.dom.parser.NodeParser;
import com.epam.wilma.stubconfig.dom.parser.node.helper.ConditionTagNames;
import com.epam.wilma.stubconfig.dom.parser.node.helper.SimpleConditionParser;
import com.epam.wilma.stubconfig.dom.parser.node.helper.StubConfigXPathEvaluator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Parses a ConditionDescriptor tag elements from Stub configuration XML file.
 * @author Tamas_Bihari
 *
 */
@Component
public class ConditionDescriptorParser implements NodeParser<ConditionDescriptor> {

    private Integer maxDepthOfXmlTree;

    @Autowired
    private StubConfigXPathEvaluator xPathEvaluator;
    @Autowired
    private SimpleConditionParser simpleConditionParser;
    @Autowired
    private StubConfigurationAccess configurationAccess;

    @Override
    public ConditionDescriptor parseNode(final Node conDes, final Document document) {
        //This number represents the depth of the subtree
        int depth = 0;
        Condition condition = null;
        if (conDes != null) {
            Element el = (Element) conDes;
            List<Condition> parsedConditions = parseConditions(el.getChildNodes(), document, depth);
            if (!parsedConditions.isEmpty()) {
                condition = parsedConditions.get(0);
            }
        }
        return new ConditionDescriptor(condition);
    }

    private List<Condition> parseConditions(final NodeList conditions, final Document document, final int depth) {
        List<Condition> parsedCondition = new LinkedList<>();
        if (conditions != null && conditions.getLength() > 0) {
            for (int i = 0; i < conditions.getLength(); i++) {
                if (conditions.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) conditions.item(i);
                    String xmlTagName = el.getTagName();
                    switch (ConditionTagNames.getTagName(xmlTagName)) {
                    case TAG_NAME_AND:
                        parsedCondition.add(new CompositeCondition(ConditionType.AND, parseConditions(el.getChildNodes(), document, depth)));
                        break;
                    case TAG_NAME_OR:
                        parsedCondition.add(new CompositeCondition(ConditionType.OR, parseConditions(el.getChildNodes(), document, depth)));
                        break;
                    case TAG_NAME_NOT:
                        parsedCondition.add(new CompositeCondition(ConditionType.NOT, parseConditions(el.getChildNodes(), document, depth)));
                        break;
                    case TAG_NAME_COND_SET_INVOKER:
                        int newDepth = validateDepth(depth, el.getAttribute("name"));
                        parseConditionSet(document, parsedCondition, el, newDepth);
                        break;
                    case TAG_NAME_CONDITION:
                        simpleConditionParser.parseSimpleCondition(parsedCondition, el);
                        break;
                    default:
                        break;
                    }
                }
            }
        }
        return parsedCondition;
    }

    private void parseConditionSet(final Document document, final List<Condition> parsedCondition, final Element el, final int depth) {
        String conditionSetName = el.getAttribute("name");
        String expression = "/wilma:wilma-stub/wilma:condition-templates/wilma:condition-set[@name='" + conditionSetName + "']";
        Element result = xPathEvaluator.getElementByXPath(expression, document);
        parsedCondition.addAll(parseConditions(result.getChildNodes(), document, depth));
    }

    private int validateDepth(final int depth, final String invokerName) {
        int newDepth = depth + 1;
        getMaxDepth();
        if (newDepth >= maxDepthOfXmlTree) {
            throw new DescriptorValidationFailedException(
                    "Validation of stub descriptor failed: Condition-descriptor subtree is too deep or contains circles, error occurs at: <condition-set-invoker name='"
                            + invokerName + "' ...>");
        }
        return newDepth;
    }

    private void getMaxDepth() {
        if (maxDepthOfXmlTree == null) {
            PropertyDto properties = configurationAccess.getProperties();
            maxDepthOfXmlTree = properties.getMaxDepthOfTree();
        }
    }
}
