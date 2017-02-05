package com.epam.wilma.extras.bulkhead;
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
import com.epam.wilma.webapp.service.external.ExternalWilmaService;
import com.google.common.collect.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

/**
 * This example service shows a special usage of the proxy part of Wilma. Withthis plugin, Wilma acts as a bulkhead for a service.
 * Read more about what Bulhead means: .....
 *
 * @author tkohegyi
 */
public class Bulkhead implements ExternalWilmaService, ConditionChecker {

    private static final String HANDLED_SERVICE = "/bulkhead";

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        return false;
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
        boolean myCall = request.equalsIgnoreCase(this.getClass().getSimpleName() + HANDLED_SERVICE);

        //set default response
        String response = "{ \"incorrectServiceCall\": \"" + myMethod + ":" + request + "\" }";
        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);

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
