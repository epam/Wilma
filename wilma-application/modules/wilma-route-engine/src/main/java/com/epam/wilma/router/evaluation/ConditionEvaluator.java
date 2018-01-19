package com.epam.wilma.router.evaluation;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.CompositeCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionType;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;

/**
 * Provides evaluation of requests over a condition.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ConditionEvaluator {

    /**
     * Evaluates a request based on a condition.
     * @param condition the condition the request is evaluated with
     * @param request the {@link WilmaHttpRequest} that is evaluated
     * @return true if the condition is fulfilled, false otherwise
     */
    public boolean evaluate(final Condition condition, final WilmaHttpRequest request) {
        boolean fulfilled = false;
        if (condition instanceof CompositeCondition) {
            if (condition.getConditionType() == ConditionType.AND) {
                fulfilled = evaluateAndCondition(condition, request);
            } else if (condition.getConditionType() == ConditionType.OR) {
                fulfilled = evaluateOrCondition(condition, request);
            } else if (condition.getConditionType() == ConditionType.NOT) {
                fulfilled = evaluateNotCondition(condition, request);
            }
        } else if (condition instanceof SimpleCondition) {
            SimpleCondition simpleCondition = (SimpleCondition) condition;
            ConditionChecker conditionChecker = simpleCondition.getConditionChecker();
            boolean negate = simpleCondition.isNegate();
            fulfilled = !negate && conditionChecker.checkCondition(request, simpleCondition.getParameters());
        }
        return fulfilled;
    }

    private boolean evaluateAndCondition(final Condition condition, final WilmaHttpRequest request) {
        boolean result = true;
        CompositeCondition andCondition = (CompositeCondition) condition;
        for (Condition c : andCondition.getConditionList()) {
            result = result && evaluate(c, request);
        }
        return result;
    }

    private boolean evaluateOrCondition(final Condition condition, final WilmaHttpRequest request) {
        boolean result = false;
        CompositeCondition orCondition = (CompositeCondition) condition;
        for (Condition c : orCondition.getConditionList()) {
            result = result || evaluate(c, request);
        }
        return result;
    }

    private boolean evaluateNotCondition(final Condition condition, final WilmaHttpRequest request) {
        CompositeCondition notCondition = (CompositeCondition) condition;
        return !evaluate(notCondition.getConditionList().get(0), request);
    }

}
