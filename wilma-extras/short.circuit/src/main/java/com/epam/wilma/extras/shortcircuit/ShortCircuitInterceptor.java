package com.epam.wilma.extras.shortcircuit;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * Created by tkohegyi on 2016. 02. 20.
 */
public class ShortCircuitInterceptor implements ResponseInterceptor, RequestInterceptor, ExternalWilmaService {

    private static Map<String, ShortCircuitResponseInformation> shortCircuitMap = ShortCircuitChecker.getShortCircuitMap();
    private final Logger logger = LoggerFactory.getLogger(ShortCircuitChecker.class);
    private final String timeoutParameterName = "timeout";

    @Override
    public void onResponseReceive(WilmaHttpResponse wilmaHttpResponse, ParameterList parameterList) {
        String shortCircuitHashCode = wilmaHttpResponse.getRequestHeader(ShortCircuitChecker.SHORT_CIRCUIT_HEADER);
        if (shortCircuitHashCode != null) { //this was marked, check if it is in the map
            long timeout;
            if (shortCircuitMap.containsKey(shortCircuitHashCode)) { //only if this needs to be handled
                //do we have the response already, or we need to catch it right now?
                ShortCircuitResponseInformation shortCircuitResponseInformation = shortCircuitMap.get(shortCircuitHashCode);
                if (shortCircuitResponseInformation == null) {
                    //we need to store the response now
                    if (parameterList != null && parameterList.get(timeoutParameterName) != null) {
                        timeout = Long.valueOf(parameterList.get(timeoutParameterName))
                                + Calendar.getInstance().getTimeInMillis();
                    } else {
                        timeout = Long.MAX_VALUE; //forever
                    }
                    shortCircuitResponseInformation = new ShortCircuitResponseInformation(wilmaHttpResponse, timeout);
                    shortCircuitMap.put(shortCircuitHashCode, shortCircuitResponseInformation);
                    logger.info("ShortCircuit: Message captured for hashcode: " + shortCircuitHashCode);
                } else { //we have response
                    //take care about timeout
                    timeout = Calendar.getInstance().getTimeInMillis();
                    if (timeout > shortCircuitResponseInformation.getTimeout()) {
                        shortCircuitMap.remove(shortCircuitHashCode);
                        logger.debug("ShortCircuit: Timeout has happened for hashcode: " + shortCircuitHashCode);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
        String hashCode = "" + wilmaHttpRequest.getHeaders().hashCode() + wilmaHttpRequest.getBody().hashCode();
        wilmaHttpRequest.addHeaderUpdate(ShortCircuitChecker.SHORT_CIRCUIT_HEADER, hashCode);
    }

    @Override
    public String handleRequest(HttpServletRequest httpServletRequest, String request, HttpServletResponse httpServletResponse) {
        String response;
        if (request.equalsIgnoreCase(this.getClass().getSimpleName() + "/circuits")) {
            response = handleCircuitRequest(httpServletRequest, httpServletResponse);
        } else {
            response = "{\"unimplementedServiceCall\":\"" + request + "\"}";
        }
        return response;
    }

    private String handleCircuitRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String response = "{\"serviceCallUnderConstruction\":\"circuits\"}";
        return response;
    }

    @Override
    public Set<String> getHandlers() {
        Set<String> handlers = Sets.newHashSet(
                this.getClass().getSimpleName() + "/circuits",       // default, gets the list
                this.getClass().getSimpleName() + "/save-circuits",  // ?path
                this.getClass().getSimpleName() + "/load-circuits",  // ?path
                this.getClass().getSimpleName() + "/invalidate-circuits");
        return handlers;
    }
}
