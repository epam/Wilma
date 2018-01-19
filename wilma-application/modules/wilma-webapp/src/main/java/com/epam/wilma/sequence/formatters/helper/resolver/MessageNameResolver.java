package com.epam.wilma.sequence.formatters.helper.resolver;
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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Resolves the name of the given Wilma message.
 * @author Balazs_Berkes
 */
public interface MessageNameResolver {

    /**
     * Resolves the name of the message.
     * @param request request which contain the message.
     * @param parameters parameters passed to resolver
     * @return the name of the message, if it cannot be resolved empty string
     */
    String resolve(WilmaHttpEntity request, ParameterList parameters);
}
