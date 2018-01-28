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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.message.Message;
import com.epam.wilma.sequence.formatters.helper.message.MessageFactory;

/**
 * Serves as the template class for the format specific sequence transformers.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public abstract class SequenceTransformer {

    @Qualifier("sequenceWilmaHttpEntityUtils")
    @Autowired
    private WilmaHttpEntityUtils httpEntityUtils;
    @Autowired
    private MessageFactory messageFactory;

    /**
     * Creates a map based on the resolved names as keys and the bodies (of
     * requests and responses converted to the preferred format) as values.
     * @param params the parameters used to resolve non-SOAP type requests and responses
     * @param pairs the request response pairs of a sequence
     * @return the map, if messages is empty then an empty map is returned
     */
    public Map<String, String> transform(final ParameterList params, final Map<String, RequestResponsePair> pairs) {
        Map<String, String> nameToBody = new HashMap<>();
        Map<String, RequestResponsePair> sortedPairs = new TreeMap<>(pairs);
        Collection<Message> messages = createMessageByType(sortedPairs.values());
        for (Message messageElement : messages) {
            storeBody(messageElement, params, nameToBody);
        }
        return nameToBody;
    }

    private void storeBody(final Message message, final ParameterList parameters, final Map<String, String> nameToBody) {
        String name = message.resolveName(parameters);
        if (!name.isEmpty()) {
            String body = message.convert(name);
            nameToBody.put(name, body);
        }
    }

    private Collection<Message> createMessageByType(final Collection<RequestResponsePair> pairs) {
        Collection<Message> messagesOfType = new ArrayList<>();
        for (RequestResponsePair pairElement : pairs) {
            WilmaHttpRequest request = pairElement.getRequest();
            WilmaHttpResponse response = pairElement.getResponse();
            addRequestMessageOfType(request, messagesOfType);
            if (response != null) {
                addResponseMessageOfType(response, messagesOfType, request);
            }
        }
        return messagesOfType;
    }

    private void addRequestMessageOfType(final WilmaHttpEntity entity, final Collection<Message> messagesOfType) {
        Message message;
        if (httpEntityUtils.isSoapMessage(entity)) {
            message = messageFactory.createSoapMessage(entity, getXmlConverter());
            messagesOfType.add(message);
        } else if (httpEntityUtils.isXmlMessage(entity)) {
            message = messageFactory.createRestRequest(entity, getXmlConverter());
            messagesOfType.add(message);
        } else if (httpEntityUtils.isJsonMessage(entity)) {
            message = messageFactory.createRestRequest(entity, getJsonConverter());
            messagesOfType.add(message);
        }
    }

    private void addResponseMessageOfType(final WilmaHttpEntity entity, final Collection<Message> messagesOfType, final WilmaHttpRequest request) {
        Message message;
        if (httpEntityUtils.isSoapMessage(entity)) {
            message = messageFactory.createSoapMessage(entity, getXmlConverter());
            messagesOfType.add(message);
        } else if (httpEntityUtils.isXmlMessage(entity)) {
            message = messageFactory.createRestResponse(entity, getXmlConverter(), request);
            messagesOfType.add(message);
        } else if (httpEntityUtils.isJsonMessage(entity)) {
            message = messageFactory.createRestResponse(entity, getJsonConverter(), request);
            messagesOfType.add(message);
        }
    }

    /**
     * Return the converter used for converting XML data.
     * @return the xml converter
     */
    protected abstract Converter getXmlConverter();

    /**
     * Return the converter used for converting JSON data.
     * @return the json converter
     */
    protected abstract Converter getJsonConverter();
}
