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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.initializer.condition.ConditionCheckerInitializer;

/**
 * Parses a simple condition.
 * @author Tunde_Kovacs
 *
 */
@Component
public class SimpleConditionParser {

    @Autowired
    private ConditionCheckerInitializer conditionCheckerInitializer;
    @Autowired
    private CustomXQueryCheckerValidator xQueryCheckerValidator;

    /**
     * Parses a simple condition and adds the result of the parsing to a lists.
     * @param parsedCondition the list the condition is added to
     * @param el the element that will be parsed
     */
    public void parseSimpleCondition(final List<Condition> parsedCondition, final Element el) {
        String className = el.getAttribute("class");
        boolean negate = Boolean.parseBoolean(el.getAttribute("negate"));
        ParameterList params = parseSimpleConditionParams(el.getElementsByTagName("param"));
        ConditionChecker conditionChecker = conditionCheckerInitializer.getExternalClassObject(className);
        xQueryCheckerValidator.validate(conditionChecker, params);
        parsedCondition.add(new SimpleCondition(conditionChecker, negate, params));
    }

    private ParameterList parseSimpleConditionParams(final NodeList params) {
        ParameterList paramList = new ParameterList();
        if (params != null && params.getLength() > 0) {
            for (int i = 0; i < params.getLength(); i++) {
                Element el = (Element) params.item(i);
                String name = el.getAttribute("name");
                String value = el.getAttribute("value");
                paramList.addParameter(new Parameter(name, value));
            }
        }
        return paramList;
    }
}
