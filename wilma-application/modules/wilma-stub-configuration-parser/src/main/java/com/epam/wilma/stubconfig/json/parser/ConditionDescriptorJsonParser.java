package com.epam.wilma.stubconfig.json.parser;

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

import com.epam.wilma.domain.stubconfig.dialog.condition.CompositeCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionType;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.dom.parser.node.helper.ConditionTagNames;
import com.epam.wilma.stubconfig.json.parser.helper.ObjectParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Parses a ConditionDescriptor tag elements from Stub configuration JSON object.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class ConditionDescriptorJsonParser implements ObjectParser<ConditionDescriptor> {

    private Integer maxDepthOfJsonTree;

//    @Autowired
//    private StubConfigXPathEvaluator xPathEvaluator;
    @Autowired
    private SimpleConditionJsonParser simpleConditionJsonParser;
    @Autowired
    private StubConfigurationAccess configurationAccess;

    @Override
    public ConditionDescriptor parseObject(final JSONObject conditionDescriptor, final JSONObject root) {
        //This number represents the depth of the subtree
        int depth = 0;
        Condition condition = null;
        if (conditionDescriptor != null) {
            List<Condition> parsedConditions = parseCondition(conditionDescriptor, root, depth);
            if (!parsedConditions.isEmpty()) {
                condition = parsedConditions.get(0);
            }
        }
        return new ConditionDescriptor(condition);
    }

    private List<Condition> parseCondition(JSONObject conditions, final JSONObject root, final int depth) {
        List<Condition> parsedCondition = new LinkedList<>();
        if (conditions != null) {
            Iterator<String> keys = conditions.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                switch (ConditionTagNames.getTagName(next)) {
                    case TAGNAME_AND:
                        parsedCondition.add(new CompositeCondition(ConditionType.AND, parseConditionArray(conditions.getJSONArray(next), root, depth, ConditionType.AND)));
                        break;
                    case TAGNAME_OR:
                        parsedCondition.add(new CompositeCondition(ConditionType.OR, parseConditionArray(conditions.getJSONArray(next), root, depth, ConditionType.OR)));
                        break;
                    case TAGNAME_NOT:
                        parsedCondition.add(new CompositeCondition(ConditionType.NOT, parseCondition(conditions.getJSONObject(next), root, depth)));
                        break;
                    case TAGNAME_COND_SET_INVOKER:
                        //TODO
//                        int newDepth = validateDepth(depth, el.getAttribute("name"));
//                        parseConditionSet(root, parsedCondition, el, newDepth);
                        break;
                    case TAGNAME_CONDITION:
                        simpleConditionJsonParser.parseSimpleCondition(parsedCondition, conditions.getJSONObject(next));
                        break;
                    default:
                        break;
                }
            }
        }
        return parsedCondition;
    }
    private List<Condition> parseConditionArray(JSONArray conditions, final JSONObject root, final int depth, final ConditionType conditionType) {
        List<Condition> parsedCondition = new LinkedList<>();
        if (conditions != null) {
            for (int i=0; i < conditions.length(); i++) {
                JSONObject object = conditions.getJSONObject(i);
                parsedCondition.add(new CompositeCondition(conditionType, parseCondition(conditions.getJSONObject(i), root, depth)));
            }
        }
        return parsedCondition;
    }

    private void parseConditionSet(final JSONObject root, final List<Condition> parsedCondition, final JSONObject object, final int depth) {
//TODO
/*        String conditionSetName = el.getAttribute("name");
        String expression = "/wilma:wilma-stub/wilma:condition-templates/wilma:condition-set[@name='" + conditionSetName + "']";
        Element result = xPathEvaluator.getElementByXPath(expression, document);
        parsedCondition.addAll(parseConditions(result.getChildNodes(), document, depth));
        */
    }

    private int validateDepth(final int depth, final String invokerName) {
        int newDepth = depth + 1;
        getMaxDepth();
        if (newDepth >= maxDepthOfJsonTree) {
            throw new DescriptorValidationFailedException(
                    "Validation of stub descriptor failed: Condition-descriptor subtree is too deep or contains circles, error occurs at: conditionSetInvoker name='"
                            + invokerName + "' ...>");
        }
        return newDepth;
    }

    private void getMaxDepth() {
        if (maxDepthOfJsonTree == null) {
            PropertyDto properties = configurationAccess.getProperties();
            maxDepthOfJsonTree = properties.getMaxDepthOfTree();
        }
    }
}
