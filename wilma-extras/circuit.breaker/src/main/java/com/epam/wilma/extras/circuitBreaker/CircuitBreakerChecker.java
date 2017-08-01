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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Condition checker class that is used for example: Short Circuit.
 * Its main task is to determine if the request is cached already or not.
 * If cached and the response is available already, then returns true (need to be stubbed).
 * If not cached, then ensure that the request is cached, and prepare catching the response.
 *
 * @author Tamas_Kohegyi
 */
public class CircuitBreakerChecker implements ConditionChecker {

    public static final String CIRCUIT_BREAKER_HEADER = "Wilma-CircuitBreakerId";
    private static final Map<String, CircuitBreakerConditionInformation> CIRCUIT_BREAKER_MAP = new HashMap<>();
    private static final Object GUARD = new Object();

    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerChecker.class);

    public static Map<String, CircuitBreakerConditionInformation> getCircuitBreakerMap() {
        return CIRCUIT_BREAKER_MAP;
    }

    public static Object getShortCircuitMapGuard() {
        return GUARD;
    }

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        boolean conditionResult = false;
        //get the key
        String identifier = parameters.get("identifier");
        if (identifier == null || identifier.length() == 0) {
            //we don't have identifier, exiting now
            conditionResult = false;
        } else {
            //we have identifier
            CircuitBreakerConditionInformation circuitBreakerConditionInformation;
            synchronized (GUARD) {
                if (!CIRCUIT_BREAKER_MAP.containsKey(identifier)) {
                    //we don't have this circuit breaker in the map yet, so put it there
                    circuitBreakerConditionInformation = new CircuitBreakerConditionInformation(identifier, parameters);
                    CIRCUIT_BREAKER_MAP.put(identifier, circuitBreakerConditionInformation);
                }
                //we are sure that we have the info in the map, so evaluate it
                circuitBreakerConditionInformation = CIRCUIT_BREAKER_MAP.get(identifier);
                if (circuitBreakerConditionInformation.isValid()) {
                    conditionResult = circuitBreakerConditionInformation.isActive();
                    //check if we are at the right path, and if so, mark the message
                    if (request.getRequestLine().toLowerCase().contains(circuitBreakerConditionInformation.getPath().toLowerCase())) {
                        request.addHeader(CIRCUIT_BREAKER_HEADER, identifier);
                    }
                }
            }
        }
        return conditionResult; //true only, if the response is stored, so we know what to answer
    }
}
