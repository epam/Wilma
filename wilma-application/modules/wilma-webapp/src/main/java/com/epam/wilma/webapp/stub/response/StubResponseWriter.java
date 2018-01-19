package com.epam.wilma.webapp.stub.response;
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

import com.epam.wilma.webapp.stub.response.processor.StubResponseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Writes stub generated response to the {@link HttpServletResponse}'s {@link ServletOutputStream}.
 * @author Tamas_Bihari
 *
 */
@Component
public class StubResponseWriter {

    private static final String ERROR_MESSAGE_FOR_UNWANTED_REQUESTS = "Wilma has declined this request.";
    private final Logger logger = LoggerFactory.getLogger(StubResponseWriter.class);
    @Autowired
    private StubResponseGenerator stubResponseGenerator;
    @Autowired
    private StubResponseProcessor stubResponseProcessor;

    /**
     * Writes response to the {@link HttpServletResponse}'s {@link ServletOutputStream},
     * which is generated using the {@link HttpServletRequest} by the {@link StubResponseGenerator}
     * and processed/compressed by the {@link StubResponseProcessor}.
     * @param req is the {@link HttpServletRequest}
     * @param resp is the {@link HttpServletResponse}
     * @throws IOException when response can not be written to {@link ServletOutputStream}.
     */
    public void writeResponse(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        //response generator to create response
        byte[] responseBody = stubResponseGenerator.generateResponse(req, resp);
        if (responseBody != null) {
            //call StubResponseProcessor to compress response body to the appropriate format
            responseBody = stubResponseProcessor.processResponse(req, resp, responseBody);
            //write response to servlet response's output stream
            writeResponseToOutputStream(resp, responseBody);
        } else {
            writeErrorResponse(resp);
            logger.warn("Wilma has got an unwanted request from this address: " + req.getRemoteAddr());
        }
    }

    private void writeResponseToOutputStream(final HttpServletResponse resp, final byte[] responseBody) throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        out.write(responseBody);
        out.close();
    }

    private void writeErrorResponse(final HttpServletResponse resp) throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        out.write(ERROR_MESSAGE_FOR_UNWANTED_REQUESTS.getBytes());
        out.close();
    }
}
