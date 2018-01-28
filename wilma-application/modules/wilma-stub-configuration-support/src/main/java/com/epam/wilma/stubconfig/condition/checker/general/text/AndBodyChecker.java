package com.epam.wilma.stubconfig.condition.checker.general.text;

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

import java.util.Iterator;
import java.util.List;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.general.operator.ConditionCheckerOperator;

/**
 * Checks the message body as plain text with the given conditions and the body must contain all of the parameters to return with true.
 * @author Tamas_Bihari
 *
 */
public class AndBodyChecker implements ConditionChecker {

    private final ConditionCheckerOperator operator;

    /**
     * Creates a new {@link AndBodyChecker} instance with its dependency as parameter.
     * @param operator is the necessary {@link ConditionCheckerOperator} instance.
     */
    public AndBodyChecker(final ConditionCheckerOperator operator) {
        this.operator = operator;
    }

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        List<Parameter> params = parameterList.getAllParameters();
        Iterator<Parameter> iterator = params.iterator();
        boolean result = !params.isEmpty();
        while (iterator.hasNext() && result) {
            Parameter parameter = iterator.next();
            String parameterValue = parameter.getValue();
            result = evaluateParameter(request, parameterValue);
        }
        return result;
    }

    private boolean evaluateParameter(final WilmaHttpRequest request, final String parameterValue) {
        boolean evaluatedResult;
        if (!"".equals(parameterValue)) {
            evaluatedResult = operator.checkTarget(parameterValue, request.getBody());
        } else {
            evaluatedResult = false;
        }
        return evaluatedResult;
    }

}
