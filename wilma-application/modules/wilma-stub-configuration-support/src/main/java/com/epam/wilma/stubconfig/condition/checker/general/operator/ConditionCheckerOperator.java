package com.epam.wilma.stubconfig.condition.checker.general.operator;
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
 * Operator for checking conditions on request body with different kind of investigation processes.
 * @author Tamas_Bihari
 *
 */
public interface ConditionCheckerOperator {

    /**
     * Checking the target with an investigation process considering the condition.
     * @param parameter is necessary dependency of the investigation process
     * @param target is the request body as String
     * @return with the result of the investigation
     */
    boolean checkTarget(String parameter, String target);
}
