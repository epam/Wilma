package com.epam.wilma.stubconfig.condition.checker.general;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Searches for patterns in the request. If one of the pattern is an empty string, it
 * returns false. Otherwise if one of the given patterns is found either in the headers or in the body, it returns true.
 * @author Tamas_Bihari
 *
 */
public class OrGeneralChecker implements ConditionChecker {

    private final ConditionChecker headerChecker;
    private final ConditionChecker bodyChecker;
    private final ConditionChecker urlChecker;

    /**
     * Creates a new {@link AndGeneralChecker} instance with its dependencies.
     * @param headerChecker is the necessary header checker instance.
     * @param bodyChecker is the necessary body checker instance.
     * @param urlChecker is the necessary request URL checker instance.
     */
    public OrGeneralChecker(final ConditionChecker headerChecker, final ConditionChecker bodyChecker, final ConditionChecker urlChecker) {
        this.headerChecker = headerChecker;
        this.bodyChecker = bodyChecker;
        this.urlChecker = urlChecker;
    }

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterMap) {
        return headerChecker.checkCondition(request, parameterMap) || bodyChecker.checkCondition(request, parameterMap)
                || urlChecker.checkCondition(request, parameterMap);
    }

}
