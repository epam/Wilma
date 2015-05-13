package com.epam.wilma.messagemarker;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.TooManyRequestsException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.messagemarker.idgenerator.IdGenerator;

/**
 * Marks a HTTP message by adding a new request header to it.
 * The value of the header contains a generated identifier, that's based on the timestamp when the request is intercepted.
 * @author Marton_Sereg
 *
 */
@Component
public class TimestampBasedMessageMarker implements MessageMarker {

    @Autowired
    @Qualifier("timestampSimpleNumber")
    private IdGenerator idGenerator;

    @Override
    public void markMessageHeader(final WilmaHttpRequest request) throws TooManyRequestsException {
        String identifier = idGenerator.nextIdentifier();
        request.addWilmaLoggerId(identifier);
    }

}
