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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class for processing the generated response with the list of processors.
 * @author Tamas_Bihari
 *
 */
public class StubResponseProcessor {

    private List<ResponseProcessor> responseProcessors;

    /**
     * Process response body with the list of processors.
     * @param req is the received {@link HttpServletRequest}
     * @param resp is the {@link HttpServletResponse}, which will be sent as response
     * @param responseBody is the generated response body by the stub
     * @return with the fully processed response body
     */
    public byte[] processResponse(final HttpServletRequest req, final HttpServletResponse resp, final byte[] responseBody) {
        byte[] result = responseBody;
        for (ResponseProcessor processor : responseProcessors) {
            result = processor.process(req, resp, result);
        }
        return result;
    }

    public void setResponseProcessors(final List<ResponseProcessor> responseProcessors) {
        this.responseProcessors = responseProcessors;
    }

}
