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

import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Serves as the interface for the requests and responses for the sequence based template formatters.
 * @author Adam_Csaba_Kiraly
 *
 */
public interface Message {

    /**
     * Resolves the name of the request/response, which serves as the identifier among the sequence.
     * @param parameters the parameters of the template formatter
     * @return the name of the request/response
     */
    String resolveName(final ParameterList parameters);

    /**
     * Converts the request/response to the desired format.
     * @param name optional argument that represents the resolved name of the request
     * @return the converted data
     */
    String convert(String name);
}
