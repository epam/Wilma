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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This example service shows a special usage of the proxy part of Wilma. With this plugin, Wilma acts as a bulkhead for a service.
 * Read more about what Bulkhead means: .....
 *
 * @author tkohegyi
 */
public class BulkHeadChecker implements ExternalWilmaService, ConditionChecker {

    private static final String HANDLED_SERVICE = "/bulkhead";
    private static final String BULKHEAD_PARAMETER_ID = "headId";
    private static final String BULKHEAD_PARAMETER_SPEED = "headSpeed";
    private static final Map<String, BulkHeadMapInformation> BULK_HEAD_MAP = new HashMap<>();
    private static final Object GUARD = new Object();

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        //determine parameters
        String parameterHashCode = parameterList.get(BULKHEAD_PARAMETER_ID);
        String parameterSpeed = parameterList.get(BULKHEAD_PARAMETER_SPEED);
        if ((parameterHashCode == null) || (parameterHashCode.length() == 0)) {
            parameterHashCode = "ALLCASE_BULKHEAD"; // this is the default head id
        }
        double speedLimit;
        try {
            speedLimit = Double.valueOf(parameterSpeed);
        } catch (NumberFormatException e) {
            speedLimit = 50.0;   //default allowed speed is 50 hit per sec
        }

        // we need to know the actual time
        long now = System.currentTimeMillis();
        boolean skipRequest = false; //default return value is false that means the bulk head is not used

        //we have parameters set, we need to do things in synch
        synchronized (GUARD) {
            // first identify if we have measurement already or not
            BulkHeadMapInformation info = BULK_HEAD_MAP.get(parameterHashCode);
            if (info == null) {
                // we don't have, so this is the first measurement, we cannot measure the speed yet, just store the time
                BULK_HEAD_MAP.put(parameterHashCode, new BulkHeadMapInformation(now));
            } else {
                // we have time information, so we can measure the speed, and update the info
                long lastTime = info.getLastTime();
                if (lastTime != now) {
                    //this is what we expect, let's calculate the load
                    double speed = 1.0 / (now - lastTime);
                    info.setLastSpeed(speed);
                    skipRequest = (speed > speedLimit);
                    if (!skipRequest) {
                        //if we don't skip the request, we update the reference time
                        info.setLastTime(now);
                    }
                } else {
                    //actually we are in trouble, as this should not happen, as this means infinite high load :)
                    skipRequest = true; //we need to skip it in case of infinite high load - just joking
                }
                BULK_HEAD_MAP.put(parameterHashCode, info);
            }
        }
        return skipRequest;
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

        //handle basic call
        if (myCall && "get".equalsIgnoreCase(myMethod)) {
            //get the map
            response = getBulkHeadMap(httpServletResponse);
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
     * List the actual status of the BulkHead Map.
     *
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String getBulkHeadMap(HttpServletResponse httpServletResponse) {
        StringBuilder response = new StringBuilder("{\n  \"bulkHeadStatus\": [\n");
        if (!BULK_HEAD_MAP.isEmpty()) {
            String[] keySet = BULK_HEAD_MAP.keySet().toArray(new String[BULK_HEAD_MAP.size()]);
            for (int i = 0; i < keySet.length; i++) {
                String entryKey = keySet[i];
                BulkHeadMapInformation bulkHeadMapInformation = BULK_HEAD_MAP.get(entryKey);
                response.append("    { \"id\": \"").append(i)
                        .append("\", \"headId\": \"").append(entryKey)
                        .append("\", \"lastSpeed\": ").append(bulkHeadMapInformation.getLastSpeed())
                        .append(" }");
                if (i < keySet.length - 1) {
                    response.append(",");
                }
                response.append("\n");
            }
        }
        response.append("  ]\n}\n");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response.toString();
    }

}
