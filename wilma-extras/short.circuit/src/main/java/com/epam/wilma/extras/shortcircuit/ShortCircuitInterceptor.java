package com.epam.wilma.extras.shortcircuit;
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
import com.epam.wilma.webapp.service.external.ExternalWilmaService;
import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Interceptor that implements all three possibilities.
 * Request interceptor marks the message with a hash code, for ShortCircuitChecker, that registers the request, if it is not yet registered.
 * Response interceptor captures the response, if it is not yet captured.
 * ExternalWilmaService offers the possibility of getting the actual status of the internal req-resp map,
 * and offers the possibility of saving the req-resp map into and loading from a folder.
 *
 * @author tkohegyi
 */
public class ShortCircuitInterceptor extends ShortCircuitInterceptorCore implements RequestInterceptor, ResponseInterceptor, ExternalWilmaService {
    private static final String HANDLED_SERVICE = "/circuits";

    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
        wilmaHttpRequest.addHeaderUpdate(ShortCircuitChecker.SHORT_CIRCUIT_HEADER, generateKeyForMap(wilmaHttpRequest));
    }

    /**
     * This is the Response Interceptor implementation. In case the response is marked with hashcode,
     * that means the response should be preserved.
     *
     * @param wilmaHttpResponse is the response
     * @param parameterList     may contain the response validity timeout - if not response will be valid forever
     */
    @Override
    public void onResponseReceive(WilmaHttpResponse wilmaHttpResponse, ParameterList parameterList) {
        String shortCircuitHashCode = wilmaHttpResponse.getRequestHeader(ShortCircuitChecker.SHORT_CIRCUIT_HEADER);
        if (shortCircuitHashCode != null) { //this was marked, check if it is in the map
            preserveResponse(shortCircuitHashCode, wilmaHttpResponse, parameterList);
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

        if (myCall) {
            //handle basic call (without query string)
            if (httpServletRequest.getQueryString() == null) {
                //get the map, or delete whole map
                response = handleBasicCall(myMethod, httpServletResponse, httpServletRequest.getPathInfo());
            } else {
                if (httpServletRequest.getQueryString().length() > 0) {
                    //handle calls with query string as folder
                    String folder = httpServletRequest.getParameter("folder");
                    if (folder != null && folder.length() > 0) {
                        //save (post) and load (get) map
                        response = handleComplexCall(myMethod, folder, httpServletResponse);
                    }
                    //handle calls with query string as id
                    String id = httpServletRequest.getParameter("id");
                    if (id != null && id.length() > 0) {
                        //delete specific entry and load (get) map
                        response = handleDeleteById(myMethod, id, httpServletResponse);
                    }
                }
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
}
