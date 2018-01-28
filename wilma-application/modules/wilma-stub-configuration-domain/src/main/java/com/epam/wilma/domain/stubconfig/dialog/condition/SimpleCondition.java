package com.epam.wilma.domain.stubconfig.dialog.condition;
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

import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Describes a single condition that should be evaluated, its result is a boolean value.
 * @author Tunde_Kovacs
 *
 */
public class SimpleCondition implements Condition {

    private final ConditionChecker conditionChecker;
    private final boolean negate;
    private final ParameterList parameters;

    /**
     * Constructs a new instance of a simple condition.
     * @param conditionChecker the class that has a condition checker method
     * @param negate decides whether the result should negated or not
     * @param parameters input parameters for the <tt>conditionChecker</tt>
     */
    public SimpleCondition(final ConditionChecker conditionChecker, final boolean negate, final ParameterList parameters) {
        super();
        this.conditionChecker = conditionChecker;
        this.negate = negate;
        this.parameters = parameters;
    }

    @Override
    public ConditionType getConditionType() {
        return ConditionType.SIMPLE;
    }

    public boolean isNegate() {
        return negate;
    }

    /**
     * Returns a copy of the condition's {@link ParameterList}. If the list is null, it returns an empty list.
     * @return the cloned map
     */
    public ParameterList getParameters() {
        ParameterList clone = new ParameterList();
        if (parameters != null) {
            clone.addAll(parameters);
        }
        return parameters;
    }

    public ConditionChecker getConditionChecker() {
        return conditionChecker;
    }

}
