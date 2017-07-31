package com.epam.wilma.extras.circuitBreaker;
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
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * This class will generate stub response by using the response captured and cached by the CircuitBreakerBreakerInterceptor class.
 *
 * @author tkohegyi, created on 2016. 02. 20.
 */
public class CircuitBreakerResponseGenerator implements TemplateGenerator, TemplateFormatter {
    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerResponseGenerator.class);

    @Override
    public byte[] generateTemplate() {
        return new byte[0];
    }

    @Override
    public byte[] formatTemplate(WilmaHttpRequest wilmaHttpRequest, HttpServletResponse httpServletResponse,
                                 byte[] bytes, ParameterList parameterList) throws Exception {
        byte[] newBody;
        //prepare a key for this request
        String hashCode = wilmaHttpRequest.getHeader(CircuitBreakerChecker.CIRCUIT_BREAKER_HEADER);
        //CHECKSTYLE OFF - we must use "new String" here
        String decodedEntryKey = new String(Base64.decodeBase64(hashCode)); //make it human readable
        //CHECKSTYLE ON
        CircuitBreakerConditionInformation circuitBreakerConditionInformation = CircuitBreakerChecker.getCircuitBreakerMap().get(hashCode);
        if (circuitBreakerConditionInformation != null) {
            //we have the answer, so set it properly
            newBody = "SOMETHING WILL BE HERE".getBytes();
            logger.info("ShortCircuit: Answer generated for request with hashcode: " + decodedEntryKey);
        } else {
            //this should not happen, we did not find the cached stub answer
            newBody = "Ups, ShortCircuit was unable to find the proper answer, sorry.".getBytes();
            logger.error("ShortCircuit: Response generator did not find proper response for request with code: " + decodedEntryKey);
        }
        return newBody;
    }
}
