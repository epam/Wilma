package com.epam.wilma.sequence.formatters.helper.message;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;

/**
 * Factory class for the various {@link Message} classes.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class MessageFactory {

    @Autowired
    private ApplicationContext context;

    /**
     * Creates a {@link SoapMessage} with autowired dependencies.
     * @param entity the {@link WilmaHttpEntity} to wrap
     * @param converter the converter of the message object
     * @return a new instance of {@link SoapMessage}
     */
    public Message createSoapMessage(final WilmaHttpEntity entity, final Converter converter) {
        Message message = new SoapMessage(entity, converter);
        context.getAutowireCapableBeanFactory().autowireBean(message);
        return message;
    }

    /**
     * Creates a {@link RestRequest} with autowired dependencies.
     * @param request the {@link WilmaHttpEntity} to wrap
     * @param converter the converter of the message object
     * @return a new instance of {@link RestRequest}
     */
    public Message createRestRequest(final WilmaHttpEntity request, final Converter converter) {
        Message message = new RestRequest(request, converter);
        context.getAutowireCapableBeanFactory().autowireBean(message);
        return message;
    }

    /**
     * Creates a {@link RestResponse} with autowired dependencies.
     * @param response the {@link WilmaHttpEntity} to wrap
     * @param converter the converter of the message object
     * @param request the {@link WilmaHttpRequest} pair of the response
     * @return a new instance of {@link RestResponse}
     */
    public Message createRestResponse(final WilmaHttpEntity response, final Converter converter, final WilmaHttpRequest request) {
        Message message = new RestResponse(response, converter, request);
        context.getAutowireCapableBeanFactory().autowireBean(message);
        return message;
    }
}
