package com.epam.wilma.extras.mockJsonService;
/*==========================================================================
Copyright since 2022, EPAM Systems

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
 * @author tkohegyi
 */
public class MockJsonServiceInterceptor extends MockJsonServiceCore implements RequestInterceptor, ExternalWilmaService {

    private static final String HANDLED_SERVICE = "/mock-json-service";

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

        //handle the call
        if (myCall) {
            //if GET - then list the preserved mocks
            if ("GET".equalsIgnoreCase(myMethod)) {
                response = getMockContent(httpServletResponse);
            }
            //if POST - then update a preserved mock - need to get proper message of course
            if ("POST".equalsIgnoreCase(myMethod)) {
                response = addMock(httpServletRequest, httpServletResponse);
            }
            //if DELETE - then clean up all the preserved mocks
            if ("DELETE".equalsIgnoreCase(myMethod)) {
                response = cleanMockContent(httpServletResponse);
            }
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

    /**
     * Nothing to do for the interceptor.
     */
    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
    }
}
