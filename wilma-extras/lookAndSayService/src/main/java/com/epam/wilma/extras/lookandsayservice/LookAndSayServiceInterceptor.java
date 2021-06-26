package com.epam.wilma.extras.lookandsayservice;
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
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.service.external.ExternalWilmaService;
import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * This example service has nothing to do with the messages, just presents how a funny service can be implemented.
 * In order to use/load it by Wilma, one of the interceptors must be implemented too (in or case the request interceptor).
 *
 * In order to understand, what this service does, have a look at this video: https://www.youtube.com/watch?v=ea7lJkEhytA
 * Two parameters can be specified via query string,
 * "number" that the service will look, and "iterations" that will specify how many times the service will 'look-and-say'
 * before providing the result.
 * There are some limitations however
 * - "number" should be a positive integer number, and this parameter is mandatory
 * - "iterations" should be a positive integer number - it is optional, if not specified, or wrong value is give, "1" will be used
 * - the service will guess the length of the result, and if that is too long (i.e. probably longer than a billion characters),
 * it will refuse generating the answer.
 * Have fun!
 *
 * @author tkohegyi
 */
public class LookAndSayServiceInterceptor extends InterceptorCore implements RequestInterceptor, ExternalWilmaService {

    private static final String HANDLED_SERVICE = "/look-and-say-service";
    private static final double CONSTANT_OF_JOHN_CONWAY = 1.31; //instead of 1.303577269
    private static final double MAX_ANSWER_SIZE = 1000000.0;

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
        boolean myCall = request.equalsIgnoreCase(this.getClass().getSimpleName() + HANDLED_SERVICE);

        //set default response
        String response = "{ \"incorrectServiceCall\": \"" + myMethod + ":" + request + "\" }";
        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);

        String startingText = httpServletRequest.getParameter("number");
        String iterationText = httpServletRequest.getParameter("iterations");


        int iterations;
        try {
            iterations = Integer.parseInt(iterationText);
        } catch (NumberFormatException e) {
            iterations = 1;
        }

        long startingSequence;
        try {
            startingSequence = Long.parseLong(startingText);
        } catch (NumberFormatException e) {
            startingSequence = -1;
        }

        //handle the call
        if (myCall && startingSequence >= 0) {
            if (isRequestIssue(startingText.length(), iterations)) {
                response = "{\n  \"serviceCalculationProblem\": \"Specified initial string and requested number of iterations would take unacceptable long time.\"\n}";
            } else {
                response = startingText;
                while (iterations > 0) {
                    response = handleIteration(response);
                    iterations--;
                }
                //formulate the response
                response = "{\n  \"number\": \"" + startingSequence
                        + "\",\n  \"iterations\": \"" + iterationText
                        + "\",\n  \"serviceResult\": \"" + response
                        + "\",\n  \"length\": \"" + response.length() + "\"\n}";
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            }
        }
        return response;
    }

    private boolean isRequestIssue(int length, int iterations) {
        return length * Math.pow(CONSTANT_OF_JOHN_CONWAY, iterations) > MAX_ANSWER_SIZE;
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

    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
    }
}
