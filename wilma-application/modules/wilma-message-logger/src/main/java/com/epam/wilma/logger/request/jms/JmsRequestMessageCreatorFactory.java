package com.epam.wilma.logger.request.jms;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Creates a new {@link JmsRequestMessageCreator} based on a {@link WilmaHttpRequest}.
 * @author Tamas_Bihari
 *
 */
@Component
public class JmsRequestMessageCreatorFactory {

    /**
     * Creates a new {@link JmsRequestMessageCreator} based on a {@link WilmaHttpRequest}.
     * @param request the base of the {@link JmsRequestMessageCreator}
     * @return the created object
     */
    public JmsRequestMessageCreator createJmsRequestMessageCreator(final WilmaHttpRequest request) {
        return new JmsRequestMessageCreator(request);
    }

}
