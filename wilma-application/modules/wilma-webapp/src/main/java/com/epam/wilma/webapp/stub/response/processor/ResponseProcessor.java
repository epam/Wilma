package com.epam.wilma.webapp.stub.response.processor;
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

/**
 * Interface for processing stub responses.
 * @author Tamas_Bihari
 *
 */
public interface ResponseProcessor {

    /**
     * Processes stub responses with the given parameters.
     * @param req is a {@link HttpServletRequest}
     * @param resp is a {@link HttpServletResponse}
     * @param responseBody is a byte array which will be processed
     * @return with the processed response body as byte array
     */
    byte[] process(final HttpServletRequest req, final HttpServletResponse resp, final byte[] responseBody);
}
