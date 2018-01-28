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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Interceptor that implements request and response interceptor interfaces.
 *
 * @author tkohegyi
 */
public class CircuitBreakerInterceptor extends CircuitBreakerService implements RequestInterceptor, ResponseInterceptor {

    static final String CIRCUIT_BREAKER_HEADER = "Wilma-CircuitBreakerId";
    private static final Object GUARD = new Object();

    /**
     * This is the Request Interceptor implementation.
     * First of all ensures that the interceptor is placed properly in the Circuit Breaker map.
     * Then, in case the request fits to the specific interceptor, and the specific circuit breaker is active,
     * notifies the condition checker by adding a header to the request.
     *
     * @param wilmaHttpRequest  is the incoming request
     * @param parameters        is the parameter list of the specific interceptor
     */
    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameters) {
        //get the key
        String identifier = parameters.get("identifier");
        if (identifier != null && identifier.length() != 0) {
            //we have identifier
            synchronized (GUARD) {
                CircuitBreakerInformation circuitBreakerInformation;
                if (!CIRCUIT_BREAKER_MAP.containsKey(identifier)) {
                    //we don't have this circuit breaker in the map yet, so put it there
                    circuitBreakerInformation = new CircuitBreakerInformation(identifier, parameters);
                    CIRCUIT_BREAKER_MAP.put(identifier, circuitBreakerInformation);
                }
                //we are sure that we have the info in the map, so evaluate it
                circuitBreakerInformation = CIRCUIT_BREAKER_MAP.get(identifier);
                //detect if it belongs to my circuit breaker (= is it the right path?) and is it valid
                if (circuitBreakerInformation.isValid()
                        && wilmaHttpRequest.getRequestLine().toLowerCase().contains(circuitBreakerInformation.getPath().toLowerCase())) {
                    //it is mine
                    //handle the active circuit breaker - if necessary
                    if (circuitBreakerInformation.isActive()) {
                        //specific circuit breaker is turned ON - is the timeout passed?
                        if (circuitBreakerInformation.getTimeout() < System.currentTimeMillis()) {
                            //yes, passed, so we need to turn off the active CB, and fall back to normal operation
                            circuitBreakerInformation.turnCircuitBreakerOff();
                        } else {
                            //no, not yet timed out, so CB must be active, and Wilma sends back the response,
                            //let's notify the condition checker
                            wilmaHttpRequest.addHeaderUpdate(CIRCUIT_BREAKER_HEADER, identifier);
                        }
                    }
                }
            }
        }
    }

    /**
     * This is the Response Interceptor implementation.
     * In case the request of the response fits to the specific Circuit Breaker, and that is inactive,
     * checks if the response is acceptable or not. If not, increases the counter of the errors,
     * and if that number is getting higher than the limit then turns the Circuit Breaker ON.
     *
     * @param wilmaHttpResponse is the response
     * @param parameters        may contain the response validity timeout - if not response will be valid forever
     */
    @Override
    public void onResponseReceive(WilmaHttpResponse wilmaHttpResponse, ParameterList parameters) {
        //get the key
        String identifier = parameters.get("identifier");
        if (identifier != null && identifier.length() != 0) {
            //we have identifier
            synchronized (GUARD) {
                if (CIRCUIT_BREAKER_MAP.containsKey(identifier)) {
                    CircuitBreakerInformation circuitBreakerInformation = CIRCUIT_BREAKER_MAP.get(identifier);
                    //detect if it belongs to my circuit breaker (= is it the right path?) and is it valid
                    if (circuitBreakerInformation.isValid()
                            && wilmaHttpResponse.getProxyRequestLine().toLowerCase().contains(circuitBreakerInformation.getPath().toLowerCase())) {
                        //it is mine, so let's evaluate the result
                        if (!circuitBreakerInformation.isActive()) {
                            //circuit breaker is not active
                            if (isStatusCodeAcceptable(wilmaHttpResponse.getStatusCode(), circuitBreakerInformation.getSuccessCodes())) {
                                //the response has acceptable status code
                                circuitBreakerInformation.resetErrorLevel();
                            } else {
                                //the response has no acceptable status code, so increase the number of problematic responses found
                                boolean isOverLimit = circuitBreakerInformation.increaseErrorLevel();
                                if (isOverLimit) { //and in case it reaches the error limit, then turn the circuit breaker ON
                                    circuitBreakerInformation.turnCircuitBreakerOn();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isStatusCodeAcceptable(Integer statusCode, Integer[] successCodes) {
        //detect if the response status code is in the list of the acceptable response codes or not
        boolean isAcceptable = false;
        for (Integer successCode : successCodes) {
            if (successCode.intValue() == statusCode.intValue()) {
                isAcceptable = true;
                break;
            }
        }
        return isAcceptable;
    }

}
