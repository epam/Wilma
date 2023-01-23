package com.epam.wilma.extras.responseautofixer;
/*==========================================================================
Copyright since 2023, EPAM Systems

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

import java.nio.charset.StandardCharsets;

/**
 * Interceptor that implements request and response interceptor interfaces.
 *
 * @author tkohegyi
 */
public class ResponseAutoFixerInterceptor implements RequestInterceptor, ResponseInterceptor {

    private boolean isStatusCodeAcceptable(Integer statusCode, String successCodes) {
        //detect if the response status code is in the list of the acceptable response codes or not
        String[] codes = successCodes.split(",");
        String incomingCode = statusCode.toString();
        boolean isAcceptable = false;
        for (String successCode : codes) {
            if (successCode.equalsIgnoreCase(incomingCode)) {
                isAcceptable = true;
                break;
            }
        }
        return isAcceptable;
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
        String path = parameters.get("path");
        String successCodes = parameters.get("successCodes");
        String responseCode = parameters.get("responseCode");
        String responseType = parameters.get("responseType");
        String response = parameters.get("response");
        //if this response is for us - if not re-routed and the path can be found in the target
        if (wilmaHttpResponse.getRequestLine().contains(wilmaHttpResponse.getProxyRequestLine())
                && wilmaHttpResponse.getProxyRequestLine().toLowerCase().contains(path.toLowerCase())) {
            //is response OK?
            if (!isStatusCodeAcceptable(wilmaHttpResponse.getStatusCode(), successCodes)) {
                //need response replacement
                wilmaHttpResponse.setContentType(responseType);
                wilmaHttpResponse.setStatusCode(Integer.parseInt(responseCode));
                wilmaHttpResponse.setNewBody(response.getBytes(StandardCharsets.UTF_8), response);
            }
        }
    }

    @Override
    public void onRequestReceive(WilmaHttpRequest request, ParameterList parameters) {
        request.setResponseVolatile(true);
    }
}
