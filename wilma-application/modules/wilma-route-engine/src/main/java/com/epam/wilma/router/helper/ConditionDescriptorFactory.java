package com.epam.wilma.router.helper;
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

import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Factory for creating specific condition descriptors.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ConditionDescriptorFactory {

    /**
     * Creates a new condition descriptor for stub mode.
     * @return the new instance
     */
    public ConditionDescriptor createConditionDescriptorForStubMode() {
        return createConditionDescriptor(new StubModeConditionChecker());
    }

    private ConditionDescriptor createConditionDescriptor(final ConditionChecker conditionChecker) {
        SimpleCondition simpleCondition = new SimpleCondition(conditionChecker, false, new ParameterList());
        ConditionDescriptor conditionDescriptor = new ConditionDescriptor(simpleCondition);
        return conditionDescriptor;
    }

}
