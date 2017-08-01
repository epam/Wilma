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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * This class provides the Circuit Breaker Service calls.
 *
 * @author tkohegyi
 */
class CircuitBreakerInterceptorCore {

    private static Map<String, CircuitBreakerConditionInformation> circuitBreakerMap = CircuitBreakerChecker.getCircuitBreakerMap();
    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerInterceptorCore.class);

    /**
     * Method that handles GET (all) methods on the actual Circuit Breaker Map.
     *
     * @param myMethod            is expected as either GET
     * @param httpServletResponse is the response object
     * @param path                is the request path
     * @return with the response body (and with the updated httpServletResponse object
     */
    String handleBasicCall(String myMethod, HttpServletResponse httpServletResponse, String path) {
        String response = null;
        if ("get".equalsIgnoreCase(myMethod)) {
            //list the map (circuits + get)
            response = getCircuitBreakerMap(httpServletResponse);
        }
        return response;
    }

    /**
     * Method that handles request to update the Circuit Breaker Map.
     *
     * @param myMethod            POST (for Save) a selected Circuit Breaker
     * @param parameterId         is the identifier of the parameter to be modified
     * @param parameterValue      is the value to be used for updating the given parameter
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    String handleComplexCall(String myMethod, String parameterId, String parameterValue, HttpServletResponse httpServletResponse) {
        String response = null;
        if ("post".equalsIgnoreCase(myMethod)) {
            //save parameter with value TODO (post + circuitBreaker?parameter=value)
            //TODO
            response = "{\"circuitBreaker\": {} }";
        }
        return response;
    }

    /**
     * List the actual status of the Short Circuit Map.
     *
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String getCircuitBreakerMap(HttpServletResponse httpServletResponse) {
        StringBuilder response = new StringBuilder("{\n  \"circuitBreakerMap\": [\n");
        if (!circuitBreakerMap.isEmpty()) {
            String[] keySet = circuitBreakerMap.keySet().toArray(new String[circuitBreakerMap.size()]);
            for (int i = 0; i < keySet.length; i++) {
                String entryKey = keySet[i];
                CircuitBreakerConditionInformation circuitBreakerConditionInformation = circuitBreakerMap.get(entryKey);
                response.append(circuitBreakerConditionInformation.toString());
                if (i < keySet.length - 1) {
                    response.append(",\n");
                }
                response.append("\n");
            }
        }
        response.append("  ]\n}\n");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response.toString();
    }

}
