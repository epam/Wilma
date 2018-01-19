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

import com.epam.wilma.webapp.service.external.ExternalWilmaService;
import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class provides the Circuit Breaker Service calls by using the ExternalWilmaService extension point of Wilma.
 *
 * @author tkohegyi
 */
class CircuitBreakerService implements ExternalWilmaService {

    static final Map<String, CircuitBreakerInformation> CIRCUIT_BREAKER_MAP = new HashMap<>();
    private static final String HANDLED_SERVICE = "/circuit-breaker";

    /**
     * ExternalWilmaService method implementation - entry point to handle the request by the external service.
     *
     * @param httpServletRequest  is the original request
     * @param request             is the request string itself (part of the URL, focusing on the requested service)
     * @param httpServletResponse is the response object
     * @return with the body of the response (need to set response code in httpServletResponse object)
     */
    @Override
    public String handleRequest(HttpServletRequest httpServletRequest, String request, HttpServletResponse httpServletResponse) {
        String myMethod = httpServletRequest.getMethod();
        String myService = (this.getClass().getSimpleName() + HANDLED_SERVICE).toLowerCase();
        boolean myCall = request.toLowerCase().startsWith(myService);

        //set default response
        String response = "{ \"unknownServiceCall\": \"" + myMethod + " " + request + "\" }";
        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);

        //handle basic call (without query string)
        if (myCall) {
            //get the circuit breaker map actual status
            response = handleRequest(myMethod, httpServletResponse);
        }

        return response;
    }

    /**
     * ExternalWilmaService method implementation - provides the list of requests, this service will handle.
     * Note that both GAT and DELETE are supported.
     *
     * @return with the set of handled services.
     */
    @Override
    public Set<String> getHandlers() {
        return Sets.newHashSet(
                this.getClass().getSimpleName() + HANDLED_SERVICE
        );
    }

    /**
     * Method that handles GET and DELETE methods on the actual Circuit Breaker Map.
     *
     * @param myMethod            is expected as either GET or DELETE
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String handleRequest(String myMethod, HttpServletResponse httpServletResponse) {
        String response = null;
        if ("get".equalsIgnoreCase(myMethod)) {
            //get the circuitBreaker map
            response = getCircuitBreakerMap(httpServletResponse);
        }
        if ("delete".equalsIgnoreCase(myMethod)) {
            //clean up the circuitsBreaker map + get result
            CIRCUIT_BREAKER_MAP.clear();
            response = getCircuitBreakerMap(httpServletResponse);
        }
        return response;
    }

    /**
     * Gets the actual status of the Circuit Breaker Map.
     *
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String getCircuitBreakerMap(HttpServletResponse httpServletResponse) {
        StringBuilder response = new StringBuilder("{\n  \"circuitBreakerMap\": [\n");
        if (!CIRCUIT_BREAKER_MAP.isEmpty()) {
            String[] keySet = CIRCUIT_BREAKER_MAP.keySet().toArray(new String[CIRCUIT_BREAKER_MAP.size()]);
            for (int i = 0; i < keySet.length; i++) {
                String entryKey = keySet[i];
                CircuitBreakerInformation circuitBreakerInformation = CIRCUIT_BREAKER_MAP.get(entryKey);
                circuitBreakerInformation.checkValidity(); //ensuring that we have the latest valid info of the circuit breaker
                response.append(circuitBreakerInformation.toString());
                if (i < keySet.length - 1) {
                    response.append(",\n");
                }
            }
            response.append("\n");
        }
        response.append("  ]\n}\n");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response.toString();
    }

}
