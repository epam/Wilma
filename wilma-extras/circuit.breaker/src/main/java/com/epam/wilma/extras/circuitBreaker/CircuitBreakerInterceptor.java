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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.service.external.ExternalWilmaService;
import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Interceptor that implements request interceptor and ExternalWilmaService interface.
 * Response interceptor detects if the response is acceptable, and if not, then turns of the circuit breaker logic.
 * ExternalWilmaService offers the possibility of getting the actual status of the internal circuit breaker status.
 *
 * @author tkohegyi
 */
public class CircuitBreakerInterceptor extends CircuitBreakerService implements ResponseInterceptor, ExternalWilmaService {
    private static final String HANDLED_SERVICE = "/circuit-breaker";

    /**
     * This is the Response Interceptor implementation. In case the response is marked with hashcode,
     * that means the response should be preserved.
     *
     * @param wilmaHttpResponse is the response
     * @param parameterList     may contain the response validity timeout - if not response will be valid forever
     */
    @Override
    public void onResponseReceive(WilmaHttpResponse wilmaHttpResponse, ParameterList parameterList) {
        //detect if it belongs to any circuit breaker
        String headerIdentifier = CircuitBreakerChecker.CIRCUIT_BREAKER_HEADER;
        String identifier = wilmaHttpResponse.getRequestHeader(headerIdentifier);
        if (identifier != null) {
            //we have circuit-breaker identifier
            synchronized (CircuitBreakerChecker.getCircuitBreakerMapGuard()) {
                CircuitBreakerInformation circuitBreakerInformation = CircuitBreakerChecker.getCircuitBreakerMap().get(identifier);
                if (circuitBreakerInformation != null) {
                    //we identified the circuit-breaker
                    Integer[] successCodes = circuitBreakerInformation.getSuccessCodes();
                    int responseCode = wilmaHttpResponse.getStatusCode();
                    //detect if the response status code is in the list of acceptable response codes or not
                    boolean found = false;
                    for (Integer successCode : successCodes) {
                        if (successCode == responseCode) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        //the response has acceptable status code
                        circuitBreakerInformation.resetErrorLevel();
                    } else {
                        //the response has not acceptable status code, so increase the number of problematic responses found
                        boolean isOverLimit = circuitBreakerInformation.increaseErrorLevel();
                        if (isOverLimit) { //and in case it reaches the error limit, then turn the circuit breaker ON
                            circuitBreakerInformation.turnCircuitBreakerOn();
                        }
                    }
                }
            }
        }
    }

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
     *
     * @return with the set of handled services.
     */
    @Override
    public Set<String> getHandlers() {
        return Sets.newHashSet(
                this.getClass().getSimpleName() + HANDLED_SERVICE
        );
    }
}
