package com.epam.wilma.domain.stubconfig.interceptor;
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
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Custom request processor.
 * @author Tunde_Kovacs
 *
 */
public interface RequestInterceptor {

    /**
     * Intercepts and processes an incoming {@link WilmaHttpRequest}.
     * @param request the request to be processed
     * @param parameters the list of parameters configured from the
     * stub configuration
     */
    void onRequestReceive(WilmaHttpRequest request, ParameterList parameters);
}
