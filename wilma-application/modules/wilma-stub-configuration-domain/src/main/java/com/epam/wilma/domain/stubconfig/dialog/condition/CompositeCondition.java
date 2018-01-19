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

import java.util.List;

/**
 * Represents a set of logical and simple conditions.
 * @author Tunde_Kovacs
 *
 */
public class CompositeCondition implements Condition {

    private final List<Condition> conditionList;
    private final ConditionType conditionType;

    /**
     * Constructs a new {@link CompositeCondition}.
     * @param conditionType the type of the condition. Value of {@link ConditionType}.
     * @param conditionList the list of child conditions
     */
    public CompositeCondition(final ConditionType conditionType, final List<Condition> conditionList) {
        super();
        this.conditionType = conditionType;
        this.conditionList = conditionList;
    }

    @Override
    public ConditionType getConditionType() {
        return conditionType;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

}
