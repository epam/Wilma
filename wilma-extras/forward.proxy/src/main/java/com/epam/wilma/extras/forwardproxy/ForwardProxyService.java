package com.epam.wilma.extras.forwardproxy;
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

import com.epam.wilma.webapp.service.external.ExternalWilmaService;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * This class provides the Circuit Breaker Service calls by using the ExternalWilmaService extension point of Wilma.
 *
 * @author tkohegyi
 */
class ForwardProxyService extends ForwardProxyFileHandler implements ExternalWilmaService {

    private static final String HANDLED_SERVICE = "/forward-proxy";

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
        if (myCall && httpServletRequest.getQueryString() == null) {
            //get the map, or delete whole map or delete entry from map
            response = handleBasicCall(myMethod, httpServletRequest, httpServletResponse, httpServletRequest.getPathInfo());
        }

        //handle complex calls (with query string as folder)
        if (myCall && httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().length() > 0) {
            String folder = httpServletRequest.getParameter("folder");
            if (folder != null && folder.length() > 0) {
                //save (post) and load (get) map
                response = handleComplexCall(myMethod, folder, httpServletResponse);
            }
        }
        return response;
    }

    private String handleBasicCall(String myMethod, HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse, String path) {
        String response = null;
        if ("get".equalsIgnoreCase(myMethod)) {
            //get the forward proxy map
            response = handleGet(httpServletResponse);
        }
        if ("delete".equalsIgnoreCase(myMethod)) {
            response = handleDelete(httpServletResponse, path);
        }
        if ("post".equalsIgnoreCase(myMethod)) {
            //we create a new entry
            response = handlePost(httpServletRequest, httpServletResponse, path);
        }
        return response;
    }

    /**
     * Method that handles request to save and load the Short Circuit Map.
     *
     * @param myMethod            POST (for Save) and GET (for Load) and DELETE for a selected
     * @param folder              is the folder to be used, or the identifier of the entry to be deleted
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String handleComplexCall(String myMethod, String folder, HttpServletResponse httpServletResponse) {
        String response = null;
        if ("post".equalsIgnoreCase(myMethod)) {
            //save map (to files) (post + forwardProxy?folder=...)
            response = saveMap(folder, httpServletResponse);
        }
        if ("get".equalsIgnoreCase(myMethod)) {
            //load map (from files) (get + forwardProxy?folder=....)
            loadMap(folder);
            response = getForwardPoxyMap(httpServletResponse);
        }
        return response;
    }

    /**
     * Gets the actual status of the Circuit Breaker Map.
     *
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String getForwardPoxyMap(HttpServletResponse httpServletResponse) {
        StringBuilder response = new StringBuilder("{\n  \"forwardProxyMap\": [\n");
        if (!FORWARD_PROXY_MAP.isEmpty()) {
            String[] keySet = FORWARD_PROXY_MAP.keySet().toArray(new String[FORWARD_PROXY_MAP.size()]);
            for (int i = 0; i < keySet.length; i++) {
                String entryKey = keySet[i];
                ForwardProxyInformation circuitBreakerInformation = FORWARD_PROXY_MAP.get(entryKey);
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

    private String handleGet(HttpServletResponse httpServletResponse) {
        return getForwardPoxyMap(httpServletResponse);
    }

    private String handlePost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String path) {
        String response;
        int index = path.lastIndexOf("/");
        String idStr = path.substring(index + 1);
        //build up the new info
        try {
            String myBody = IOUtils.toString(httpServletRequest.getReader());
            JSONObject json = new JSONObject(myBody);
            String originalTarget = json.getString("originalTarget");
            String realTarget = json.getString("realTarget");
            ForwardProxyInformation forwardProxyInformation = new ForwardProxyInformation(idStr, originalTarget, realTarget);
            if (forwardProxyInformation.isValid()) {
                synchronized (GUARD) {
                    FORWARD_PROXY_MAP.put(idStr, forwardProxyInformation);
                }
                response = getForwardPoxyMap(httpServletResponse);
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            } else {
                //request is not valid
                response = "{ \"error\": \"Specified forward-proxy information is not accepted.\" }";
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (JSONException | IOException e) {
            response = "{ \"error\": \"" + e.getLocalizedMessage() + "\" }";
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }

    private String handleDelete(HttpServletResponse httpServletResponse, String path) {
        String response;
        //first detect if we have basic path or we have a specified id in it
        int index = path.lastIndexOf("/");
        String idStr = path.substring(index + 1);
        int id;
        try {
            id = Integer.parseInt(idStr);
            //remove a specific map entry
            String[] keySet = FORWARD_PROXY_MAP.keySet().toArray(new String[FORWARD_PROXY_MAP.size()]);
            if (keySet.length > id) {
                //we can delete it
                String entryKey = keySet[id];
                FORWARD_PROXY_MAP.remove(entryKey);
                response = getForwardPoxyMap(httpServletResponse);
            } else {
                //resource cannot be deleted
                response = "{ \"Cannot found specified entry in Forward Proxy\": \"" + id + "\" }";
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            //it is not a problem, we should delete all
            //invalidate map (remove all from map) (circuits + delete)
            synchronized (GUARD) {
                FORWARD_PROXY_MAP.clear();
            }
            response = getForwardPoxyMap(httpServletResponse);
        }
        return response;
    }

}
