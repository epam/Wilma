package com.epam.wilma.test.server;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.test.server.compress.gzip.GzipCompressor;

/**
 * PUT CLASS DESCRIPTION HERE.
 */
public class SequenceTestHandler extends AbstractHandler {
    private static final String INTERRUPTED_EXCEPTION_MESSAGE = "Got interrupted before setting proper answer!";
    private static final String INTERNAL_ERROR_MESSAGE = "Wilma Test Server - Sequence test\nCannot handle Request - Internal Error.";
    private static final String PATH_TO_HANDLE_SEQUENCE = "/sequencetest";
    private static final String PATH_TO_HANDLE_RESETSEQUENCE = "/resetsequences";
    private static final String WILMA_LOGGER_ID = "Wilma-Logger-ID";

    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String GZIP_TYPE = "gzip";
    private static final String TEXT_TYPE = "text/html";

    private final Logger logger = LoggerFactory.getLogger(SequenceTestHandler.class);

    private final GzipCompressor gzipCompressor = new GzipCompressor();
    private final Map<String, Integer> sequenceMap = new HashMap<>();
    private final Semaphore s = new Semaphore(1);

    /**
     * Constructor with InputStreamConverter.
     * @param inputStreamConverter to inject
     */
    public SequenceTestHandler(final InputStreamConverter inputStreamConverter) {
    }

    @Override
    public void handle(final String path, final Request baseRequest, final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse) throws IOException, ServletException {
        if (PATH_TO_HANDLE_SEQUENCE.equals(path)) {
            String parameter = baseRequest.getQueryString();
            setAnswer(baseRequest, httpServletRequest, httpServletResponse, parameter);
        } else if (PATH_TO_HANDLE_RESETSEQUENCE.equals(path)) {
            String parameter = baseRequest.getQueryString();
            setAnswerReset(baseRequest, httpServletRequest, httpServletResponse);
        }
    }

    private void setAnswer(final Request baseRequest, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
            final String parameter) throws IOException {
        byte[] responseBodyAsBytes = null;
        if (httpServletRequest.getHeader(ACCEPT_HEADER) == null) {
            httpServletResponse.getWriter().println("Missing accept header!");
        } else if (httpServletRequest.getHeader(ACCEPT_HEADER).contains(TEXT_TYPE)) {
            //now reset internal sequence table
            String response = null;
            try {
                response = handleSequenceTable(parameter);
            } catch (InterruptedException e) {
                logger.error(INTERRUPTED_EXCEPTION_MESSAGE, e);
                response = INTERNAL_ERROR_MESSAGE;
            }
            httpServletResponse.setContentType("text/plain;charset=UTF-8");
            responseBodyAsBytes = response.getBytes();
        }
        //Encodes response body with gzip if client accepts gzip encoding
        if (httpServletRequest.getHeader(ACCEPT_ENCODING) != null && httpServletRequest.getHeader(ACCEPT_ENCODING).contains(GZIP_TYPE)) {
            ByteArrayOutputStream gzipped = gzipCompressor.compress(new ByteArrayInputStream(responseBodyAsBytes));
            responseBodyAsBytes = gzipped.toByteArray();
            httpServletResponse.addHeader(CONTENT_ENCODING, GZIP_TYPE);
        }
        httpServletResponse.getOutputStream().write(responseBodyAsBytes);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
    }

    private void setAnswerReset(final Request baseRequest, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse)
        throws IOException {
        byte[] responseBodyAsBytes = null;
        if (httpServletRequest.getHeader(ACCEPT_HEADER) == null) {
            httpServletResponse.getWriter().println("Missing accept header!");
        } else if (httpServletRequest.getHeader(ACCEPT_HEADER).contains(TEXT_TYPE)) {
            //now reset internal sequence table
            String response = "Wilma Test Server - Sequence test\nOK, Reset done.";
            String interceptorReqTest = baseRequest.getHeader(WILMA_LOGGER_ID);
            if (interceptorReqTest != null) {
                response += "\n" + WILMA_LOGGER_ID + " was found:" + interceptorReqTest;
            }
            try {
                resetSequenceTable();
            } catch (InterruptedException e) {
                logger.error(INTERRUPTED_EXCEPTION_MESSAGE, e);
                response = INTERNAL_ERROR_MESSAGE;
            }
            httpServletResponse.setContentType("text/plain;charset=UTF-8");
            responseBodyAsBytes = response.getBytes();
        }
        //Encodes response body with gzip if client accepts gzip encoding
        if (httpServletRequest.getHeader(ACCEPT_ENCODING) != null && httpServletRequest.getHeader(ACCEPT_ENCODING).contains(GZIP_TYPE)) {
            ByteArrayOutputStream gzipped = gzipCompressor.compress(new ByteArrayInputStream(responseBodyAsBytes));
            responseBodyAsBytes = gzipped.toByteArray();
            httpServletResponse.addHeader(CONTENT_ENCODING, GZIP_TYPE);
        }
        httpServletResponse.getOutputStream().write(responseBodyAsBytes);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
    }

    private String handleSequenceTable(final String parameter) throws InterruptedException {
        String response = "Wilma Test Server - Sequence test\n";
        if (parameter == null) {
            response += "BAD SEQUENCE REQUEST! - (UNRECOGNIZED REQUEST)";
        } else if (!parameter.startsWith("seq=")) {
            response += "BAD SEQUENCE REQUEST! - (Keyword not found)";
        } else {
            s.acquire();
            if (sequenceMap.containsKey(parameter)) {
                Integer i = sequenceMap.get(parameter);
                i++;
                sequenceMap.put(parameter, i); //increase the number of sequences
            } else {
                sequenceMap.put(parameter, 1); //initiate the sequence
            }
            response += printSequenceTable();
            s.release();
        }
        return response;
    }

    private void resetSequenceTable() throws InterruptedException {
        s.acquire();
        sequenceMap.clear();
        s.release();
    }

    private String printSequenceTable() {
        StringBuffer s = new StringBuffer();
        s.append("\nActual Sequence Table:\n");
        for (Map.Entry<String, Integer> entry : sequenceMap.entrySet()) {
            s.append(entry.getKey() + "->" + entry.getValue() + "\n");
        }
        return s.toString();
    }

}
