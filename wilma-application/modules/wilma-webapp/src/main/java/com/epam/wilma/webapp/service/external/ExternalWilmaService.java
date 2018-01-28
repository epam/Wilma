package com.epam.wilma.webapp.service.external;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Interface that should be implemented by the Wilma external classes. Can be used to extend the REST services of Wilma.
 *
 * @author Tamas_Kohegyi
 */
public interface ExternalWilmaService {
    /**
     * Method that will be invoked, when the response is asked to be created.
     *
     * @param req              is the original request servlet
     * @param requestedService is the requested REST service part
     * @param resp             is the response servlet
     * @return with the body of the response, or null, if no response is provided.
     */
    String handleRequest(final HttpServletRequest req, final String requestedService, HttpServletResponse resp);

    /**
     * Get the list of REST services (Strings) when Wilma will call the handleRequest method.
     *
     * @return with the list of services provided by the class.
     */
    Set<String> getHandlers();
}
