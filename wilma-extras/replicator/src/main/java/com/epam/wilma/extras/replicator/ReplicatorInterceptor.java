package com.epam.wilma.extras.replicator;
/*==========================================================================
Copyright 2016 EPAM Systems

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

import com.epam.wilma.core.processor.entity.JmsRequestLoggerProcessor;
import com.epam.wilma.core.processor.entity.JmsResponseProcessor;
import com.epam.wilma.core.processor.entity.PrettyPrintProcessor;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The main interceptor class.
 *
 * @author Tamas Kohegyi
 */
@Component
public class ReplicatorInterceptor implements RequestInterceptor {

    @Autowired
    private PrettyPrintProcessor prettyPrintProcessor;

    @Autowired
    private JmsRequestLoggerProcessor jmsRequestLoggerProcessor;

    @Autowired
    private JmsResponseProcessor jmsResponseProcessor;

    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
        //first decide if we need to replicate this request and forward to somewhere else too
        for (Parameter parameter : parameterList.getAllParameters()) {
            if (wilmaHttpRequest.getUri().toString().startsWith(parameter.getName())) {
                if (!SecondaryMessageLogger.wasSet()) {
                    SecondaryMessageLogger.setBeans(prettyPrintProcessor, jmsRequestLoggerProcessor, jmsResponseProcessor);
                }
                replicateRequest(wilmaHttpRequest, parameter.getName(), parameter.getValue());
            }
        }
    }

    private void replicateRequest(WilmaHttpRequest wilmaHttpRequest, String fromServer, String toServer) {
        //prepare the secondary request
        WilmaHttpRequest secondaryRequest;
        try {
            secondaryRequest = cloneRequest(wilmaHttpRequest);
            secondaryRequest.setRequestLine(secondaryRequest.getRequestLine().replace(fromServer, toServer));
            secondaryRequest.setUri(new URI(secondaryRequest.getUri().toString().replace(fromServer, toServer)));

            //here put it into queue
            //TODO

        } catch (URISyntaxException e) {
            return; //we were unable to put it to the queue
        }

        //here assume that it is in the queue, and a consumer reads it out, somehow this way:
        ReplicatorQueueHandler replicatorQueueHandler = new ReplicatorQueueHandler();
        replicatorQueueHandler.handleQueuedMessage(secondaryRequest);

    }

    private WilmaHttpRequest cloneRequest(WilmaHttpRequest wilmaHttpRequest) throws URISyntaxException {
        WilmaHttpRequest clone = new WilmaHttpRequest();

        clone.setRequestLine(wilmaHttpRequest.getRequestLine());
        for (String headerKey : wilmaHttpRequest.getHeaders().keySet()) {
            clone.addHeader(headerKey, wilmaHttpRequest.getHeader(headerKey));
        }

        clone.setBody(wilmaHttpRequest.getBody());
        clone.setUri(new URI(wilmaHttpRequest.getUri().toString()));
        clone.setResponseVolatile(false);

        //set Wilma Message Id
        clone.setWilmaMessageId("REPLICA_" + wilmaHttpRequest.getWilmaMessageId());

        //set remote addr
        clone.setRemoteAddr(wilmaHttpRequest.getRemoteAddr());

        return clone;
    }

}

