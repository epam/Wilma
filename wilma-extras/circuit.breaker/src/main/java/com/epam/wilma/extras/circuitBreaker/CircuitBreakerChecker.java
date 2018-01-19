package com.epam.wilma.extras.circuitBreaker;
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
 * Condition checker class that is used for example: Circuit Breaker.
 * Its main task is to determine if the request is marked with the circuit breaker identifier,
 * because if it is marked, that means the specific circuit breaker is ON, and then return with true.
 *
 * @author Tamas_Kohegyi
 */
public class CircuitBreakerChecker implements ConditionChecker {

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        boolean conditionResult;
        //get the key
        String identifier = parameters.get("identifier");
        if (identifier == null || identifier.length() == 0) {
            //we don't have identifier, exiting now
            conditionResult = false;
        } else {
            //we have the identifier
            String headedInformation = request.getHeaderUpdateValue(CircuitBreakerInterceptor.CIRCUIT_BREAKER_HEADER);
            conditionResult = (headedInformation != null) && (headedInformation.compareTo(identifier) == 0);
        }
        return conditionResult; //true only, if the specific circuit breaker is active, so the stub must generate the answer
    }
}
