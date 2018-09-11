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
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * This class will generate stub response by using the response captured and cached by the ShortCircuitInterceptor class.
 *
 * @author tkohegyi, created on 2016. 02. 20.
 */
public class ShortCircuitResponseGenerator implements TemplateGenerator, ResponseFormatter {
    private final Logger logger = LoggerFactory.getLogger(ShortCircuitResponseGenerator.class);

    @Override
    public byte[] generateTemplate() {
        return new byte[0];
    }

    @Override
    public byte[] formatResponse(WilmaHttpRequest wilmaHttpRequest, HttpServletResponse httpServletResponse,
                                 byte[] bytes, ParameterList parameterList) throws Exception {
        byte[] newBody;
        //prepare a key for this request
        String hashCode = wilmaHttpRequest.getHeader(ShortCircuitChecker.SHORT_CIRCUIT_HEADER);
        //CHECKSTYLE OFF - we must use "new String" here
        String decodedEntryKey = new String(Base64.decodeBase64(hashCode)); //make it human readable
        //CHECKSTYLE ON
        ShortCircuitResponseInformation shortCircuitResponseInformation = ShortCircuitChecker.getShortCircuitMap().get(hashCode);
        if (shortCircuitResponseInformation != null) {
            //we have the answer, so set it properly
            httpServletResponse.setContentType(shortCircuitResponseInformation.getContentType());
            httpServletResponse.setStatus(shortCircuitResponseInformation.getStatusCode());
            Map<String, String> map = shortCircuitResponseInformation.getHeaders();
            if (map != null) {
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    httpServletResponse.addHeader(key, map.get(key));
                }
            }
            newBody = shortCircuitResponseInformation.getBody().getBytes();
            shortCircuitResponseInformation.increaseUsageCount(); //hey we just used this cache entry
            logger.info("ShortCircuit: Answer generated for request with hashcode: " + decodedEntryKey);
        } else {
            //this should not happen, we did not find the cached stub answer
            newBody = "Ups, ShortCircuit was unable to find the proper answer, sorry.".getBytes();
            logger.error("ShortCircuit: Response generator did not find proper response for request with code: " + decodedEntryKey);
        }
        return newBody;
    }
}
