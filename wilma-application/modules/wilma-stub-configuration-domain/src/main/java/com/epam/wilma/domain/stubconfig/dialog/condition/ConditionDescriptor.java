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

/**
 * This element describes a complex condition which should be evaluated by the switch engine.
 * Method: ResponseDescriptor evaluateCondition(DialogDescriptor, RequestMessage), -&gt;
 * evaluates the ConditionDescriptor part and returns with the ResponseDescriptor class the condition matched,
 * or NULL in case condition failed.
 * When it has value -&gt; stub shall be activated with the ResponseDescriptor info.
 * When it has no value (NULL) -&gt; next DialogDescriptor, or if no such left -&gt; proxy mode
 *
 * @author Tunde_Kovacs
 */
public class ConditionDescriptor {

    public static final String TAG_NAME = "condition-descriptor";
    public static final String TAG_NAME_JSON = "conditionDescriptor";

    private final Condition condition;

    /**
     * Constructs a new instance of {@link ConditionDescriptor}.
     *
     * @param condition the condition that is matched over the request
     */
    public ConditionDescriptor(final Condition condition) {
        super();
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

}
