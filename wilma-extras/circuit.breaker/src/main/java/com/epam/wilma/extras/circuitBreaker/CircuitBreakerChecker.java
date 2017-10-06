package com.epam.wilma.extras.circuitBreaker;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
 * Condition checker class that is used for example: Short Circuit.
 * Its main task is to determine if the request is marked with the circuit breaker identifier,
 * because if it marked, that means the circuit breaker is ON.
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
            //we have identifier
            String headedInformation = request.getHeader(CircuitBreakerInterceptor.CIRCUIT_BREAKER_HEADER);
            conditionResult = (headedInformation != null) && (headedInformation.compareTo(identifier) == 0);
        }
        return conditionResult; //true only, if the response is stored, so we know what to answer
    }
}
