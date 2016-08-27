package com.epam.wilma.extras.replicator.interceptor;
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
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.extras.replicator.repeater.HttpRequestSender;

/**
 * The main interceptor class.
 *
 * @author Tamas Kohegyi
 */
public class ReplicatorInterceptor implements RequestInterceptor {

    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
        HttpRequestSender httpRequestSender = new HttpRequestSender();
        WilmaHttpRequest secondaryRequest = cloneRequest(wilmaHttpRequest);

        //here put it into queue
        //TODO

        //here assume that it is in the queue, and a consumer reads it out
        WilmaHttpResponse secondaryResponse = httpRequestSender.callSecondaryServer(secondaryRequest, "http://localhost:8081/testserver");

        //finally story the messages
        storeMessages(secondaryRequest, secondaryResponse);
    }

    private WilmaHttpRequest cloneRequest(WilmaHttpRequest wilmaHttpRequest) {
        WilmaHttpRequest clone = new WilmaHttpRequest();

        clone.setRequestLine(wilmaHttpRequest.getRequestLine());
        for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
            clone.addHeader(headerKey, wilmaHttpRequest.getHeader(headerKey));
        }

        clone.setBody(wilmaHttpRequest.getBody());
        clone.setUri(wilmaHttpRequest.getUri());
        clone.setResponseVolatile(false);

        //set Wilma Message Id
        clone.setWilmaMessageId("REPLICA_" + wilmaHttpRequest.getWilmaMessageId());

        //set remote addr
        clone.setRemoteAddr(wilmaHttpRequest.getRemoteAddr());

        return clone;
    }

    private void storeMessages(WilmaHttpRequest secondaryRequest, WilmaHttpResponse secondaryResponse) {
        //put both request and response to message saving queue
    }


}

