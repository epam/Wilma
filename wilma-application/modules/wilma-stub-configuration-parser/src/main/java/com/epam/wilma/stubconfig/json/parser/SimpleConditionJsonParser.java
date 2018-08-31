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

import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.dom.parser.node.helper.CustomXQueryCheckerValidator;
import com.epam.wilma.stubconfig.initializer.condition.ConditionCheckerInitializer;
import com.epam.wilma.stubconfig.json.parser.helper.ParameterListParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Parses a simple condition.
 * @author Tamas_Kohegyi
 *
 */
@Component
public class SimpleConditionJsonParser {

    @Autowired
    private ConditionCheckerInitializer conditionCheckerInitializer;
    @Autowired
    private CustomXQueryCheckerValidator xQueryCheckerValidator;
    @Autowired
    private ParameterListParser parameterListParser;

    /**
     * Parses a simple condition and adds the result of the parsing to a lists.
     * @param parsedCondition the list the condition is added to
     * @param condition the JSON object that will be parsed
     */
    public void parseSimpleCondition(final List<Condition> parsedCondition, final JSONObject condition) {
        String className = condition.getString("class");
        boolean negate = false;
        if (condition.has("negate")) {
            negate = condition.getBoolean("negate");
        }
        ParameterList params = parameterListParser.parseObject(condition, null);
        ConditionChecker conditionChecker = conditionCheckerInitializer.getExternalClassObject(className);
        xQueryCheckerValidator.validate(conditionChecker, params);
        parsedCondition.add(new SimpleCondition(conditionChecker, negate, params));
    }

}
