package com.epam.wilma.core.processor.response.jms;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Factory for creating instances of {@link JmsResponseMessageCreator}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmsResponseMessageCreatorFactory {

    /**
     * Creates a new instance of {@link JmsResponseMessageCreator}.
     * @param response the instance of {@link WilmaHttpResponse} that will
     * be used to build the message
     * @param bodyDecompressed true if body of HTTP response is decompressed
     * @return the new instance
     */
    public JmsResponseMessageCreator create(final WilmaHttpResponse response, final boolean bodyDecompressed) {
        return new JmsResponseMessageCreator(response, bodyDecompressed);
    }
}
