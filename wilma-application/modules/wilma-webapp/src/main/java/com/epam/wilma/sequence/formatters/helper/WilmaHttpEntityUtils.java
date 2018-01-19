package com.epam.wilma.sequence.formatters.helper;

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

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Contains utility methods for {@link WilmaHttpEntity}.
 *
 * @author Balazs_Berkes
 */

@Component("sequenceWilmaHttpEntityUtils")
public class WilmaHttpEntityUtils {

    private static final String XML_HEADER = "<?xml";
    private static final String SOAP_OPENING_TAG = ":Envelope";
    private final Logger logger = LoggerFactory.getLogger(WilmaHttpEntityUtils.class);

    /**
     * Decides whether given {@code WilmaHttpRequest} is JSON or not.
     * @param request request to check
     * @return {@code true} if the request is a JSON message otherwise {@code false}.
     */
    public boolean isJsonMessage(final WilmaHttpEntity request) {
        boolean isJson;
        String body = null;
        try {
            body = request.getBody();
            new JSONObject(body);
            isJson = true;
        } catch (JSONException ex) {
            logger.debug("Request (" + request.getWilmaMessageLoggerId() + ") body is not of JSON type or has invalid syntax: " + body, ex);
            isJson = false;
        }
        return isJson;
    }

    /**
     * Decides whether given {@code WilmaHttpRequest} is XML or not.
     * @param request request to check
     * @return {@code true} if the request is a XML message otherwise {@code false}.
     */
    public boolean isXmlMessage(final WilmaHttpEntity request) {
        return request.getBody().startsWith(XML_HEADER);
    }

    /**
     * Decides whether given {@code WilmaHttpRequest} is SOAP or not.
     * @param request request to check
     * @return {@code true} if the request is a SOAP message otherwise {@code false}.
     */
    public boolean isSoapMessage(final WilmaHttpEntity request) {
        return request.getBody().contains(SOAP_OPENING_TAG) || request.getBody().contains(SOAP_OPENING_TAG.toLowerCase());
    }

}
