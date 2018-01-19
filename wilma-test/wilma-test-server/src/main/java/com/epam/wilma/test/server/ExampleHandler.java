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
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.epam.wilma.test.server.compress.Decompressor;
import com.epam.wilma.test.server.compress.fis.FastInfosetDecompressor;
import com.epam.wilma.test.server.compress.gzip.GzipCompressor;
import com.epam.wilma.test.server.compress.gzip.GzipDecompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jetty handler that is able to handle requests coming to /example with one of the given XMLs in the request body.
 * @author Marton_Sereg
 *
 */
public class ExampleHandler extends AbstractHandler {
    private static final String FIS_RESPONSE = "example.xml.fis";
    private static final String EXAMPLE_XML = "example.xml";
    private static final String EXAMPLE_JSON = "example.json";
    private static final String WILMA_LOGGER_ID = "Wilma-Logger-ID";

    private static final String PATH_OK = "/ok";
    private static final String PATH_NOT_IMPLEMENTED = "/sendnotimplemented";
    private static final String PATH_BAD_GATWAY = "/sendbadgateway";
    private static final String PATH_SERVICE_UNAVAILABLE = "/sendserviceunavailable";
    private static final String PATH_INTERNAL_SERVER_ERROR = "/sendinternalservererror";
    private static final String PATH_SEND_BAD_FIS = "/sendbadfis";
    private static final String PATH_REPLICATOR = "/replicator";
    private static final int WAIT_IN_MILLIS = 61000;
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String GZIP_TYPE = "gzip";
    private static final String FASTINFOSET_TYPE = "application/fastinfoset";
    private static final String XML_TYPE = "application/xml";
    private static final String ANY_TYPE = "*/*";

    private static final String PATH_TO_HANDLE = "/example";
    private static final String PATH_TO_TIMEOUT = "/sendtimeout";

    private final Logger logger = LoggerFactory.getLogger(ExampleHandler.class);
    private final InputStreamConverter inputStreamConverter;

    private final Decompressor fisDecompressor = new FastInfosetDecompressor();

    private final GzipDecompressor gzipDecompresser = new GzipDecompressor();

    private final GzipCompressor gzipCompressor = new GzipCompressor();

    /**
     * Constructor with InputStreamConverter.
     * @param inputStreamConverter to inject
     */
    public ExampleHandler(final InputStreamConverter inputStreamConverter) {
        this.inputStreamConverter = inputStreamConverter;
    }

    @Override
    public void handle(final String path, final Request baseRequest, final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse) throws IOException, ServletException {
        if (PATH_TO_HANDLE.equals(path) || PATH_OK.equals(path)) {
            byte[] byteArray = null;
            String requestBody = "";
            String contentEncodingHeader = httpServletRequest.getHeader(CONTENT_ENCODING);
            if (contentEncodingHeader != null && contentEncodingHeader.contains(GZIP_TYPE)) {
                byteArray = gzipDecompresser.decompress(httpServletRequest.getInputStream()).toByteArray();
                requestBody = IOUtils.toString(byteArray, "utf-8");
            }
            String headerField = httpServletRequest.getHeader(CONTENT_TYPE);
            if (headerField != null && headerField.contains(FASTINFOSET_TYPE)) {
                if (byteArray != null) {
                    requestBody = fisDecompressor.decompress(new ByteArrayInputStream(byteArray));
                } else {
                    requestBody = fisDecompressor.decompress(httpServletRequest.getInputStream());
                }
            } else if (headerField != null && headerField.contains(XML_TYPE)) {
                if (byteArray == null) {
                    requestBody = inputStreamConverter.getStringFromStream(httpServletRequest.getInputStream());
                }
            }
            if (PATH_OK.equals(path)) {
                //ok, send back the test server version
                requestBody = getClass().getPackage().getImplementationTitle();
            }
            if ("".equals(requestBody)) {
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                IOUtils.copy(httpServletRequest.getInputStream(), writer);
                requestBody = IOUtils.toString(writer.toByteArray(), "utf-8"); //default request body content
            }
            setAnswer(baseRequest, httpServletRequest, httpServletResponse, requestBody);
        } else if (baseRequest.getRequestURI().startsWith(PATH_REPLICATOR)) {
            //send a replicator answer back
            InputStream json = getXmlFromFile(EXAMPLE_JSON); //getXml? who cares
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            byte[] responseBodyAsBytes = IOUtils.toByteArray(json, json.available());

            //Encodes response body with gzip if client accepts gzip encoding
            if (httpServletRequest.getHeader(ACCEPT_ENCODING) != null && httpServletRequest.getHeader(ACCEPT_ENCODING).contains(GZIP_TYPE)) {
                ByteArrayOutputStream gzipped = gzipCompressor.compress(new ByteArrayInputStream(responseBodyAsBytes));
                responseBodyAsBytes = gzipped.toByteArray();
                httpServletResponse.addHeader(CONTENT_ENCODING, GZIP_TYPE);
            }

            httpServletResponse.getOutputStream().write(responseBodyAsBytes);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        } else {
            generateBadResponses(path, httpServletRequest, httpServletResponse, baseRequest);
            generateErrorCode(path, httpServletResponse);
        }
    }

    private void generateBadResponses(String path, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Request baseRequest) throws IOException {
        if (PATH_SEND_BAD_FIS.equals(path)) {
            byte[] responseBodyAsBytes;
            if (httpServletRequest.getHeader(ACCEPT_HEADER).contains(FASTINFOSET_TYPE)) {
                InputStream xml = getXmlFromFile(EXAMPLE_XML);
                httpServletResponse.setContentType("application/fastinfoset");
                httpServletResponse.setCharacterEncoding("UTF-8");
                responseBodyAsBytes = IOUtils.toByteArray(xml, xml.available());

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
        }
    }

    private void generateErrorCode(final String path, final HttpServletResponse httpServletResponse)
        throws ServletException, IOException {
        if (PATH_TO_TIMEOUT.equals(path)) {
            try {
                Thread.sleep(WAIT_IN_MILLIS);
            } catch (Exception e) {
                throw new ServletException("Thread's wait failed....", e);
            }
        } else if (PATH_INTERNAL_SERVER_ERROR.equals(path)) {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (PATH_SERVICE_UNAVAILABLE.equals(path)) {
            httpServletResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } else if (PATH_BAD_GATWAY.equals(path)) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_GATEWAY);
        } else if (PATH_NOT_IMPLEMENTED.equals(path)) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

    }

    private void setAnswer(final Request baseRequest, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
            final String requestBody) throws IOException {
        boolean needVersionAnswer = requestBody.contains("Wilma Test Server");
        if (answerShouldBeSent(requestBody) || needVersionAnswer) {
            byte[] responseBodyAsBytes = null;
            if (((httpServletRequest.getHeader(CONTENT_TYPE) != null) && (httpServletRequest.getHeader(CONTENT_TYPE).contains("text/plain")))
                    || (needVersionAnswer)) {
                //just send the request back + Wilma-Logger-ID in front, if exists
                String answer = requestBody;
                if (httpServletRequest.getHeader(WILMA_LOGGER_ID) != null) {
                    answer = httpServletRequest.getHeader(WILMA_LOGGER_ID) + " " + requestBody;
                }
                httpServletResponse.setContentType("text/plain");
                httpServletResponse.setCharacterEncoding("UTF-8");
                responseBodyAsBytes = answer.getBytes();
                logger.info("Received message:\n-----------\n" + requestBody + "\n-----------");
            } else if (httpServletRequest.getHeader(ACCEPT_HEADER) == null) {
                httpServletResponse.getWriter().println("Missing accept header!");
            } else if (httpServletRequest.getHeader(ACCEPT_HEADER).contains(XML_TYPE) || httpServletRequest.getHeader(ACCEPT_HEADER).contains(ANY_TYPE)) {
                InputStream xml = getXmlFromFile(EXAMPLE_XML);
                httpServletResponse.setContentType("application/xml;charset=UTF-8");
                responseBodyAsBytes = (inputStreamConverter.getStringFromStream(xml)).getBytes();
            } else if (httpServletRequest.getHeader(ACCEPT_HEADER).contains(FASTINFOSET_TYPE)) {
                InputStream xml = getXmlFromFile(FIS_RESPONSE);
                httpServletResponse.setContentType("application/fastinfoset");
                httpServletResponse.setCharacterEncoding("UTF-8");
                responseBodyAsBytes = IOUtils.toByteArray(xml, xml.available());
            }
            if (responseBodyAsBytes != null) {
                //first copy wilma message id to the response, if necessary
                if (httpServletRequest.getHeader(WILMA_LOGGER_ID) != null) {
                    httpServletResponse.addHeader(WILMA_LOGGER_ID, httpServletRequest.getHeader(WILMA_LOGGER_ID));
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
        }
    }

    private boolean answerShouldBeSent(final String requestBody) {
        return requestBody.contains("exampleID=\"123\"") || requestBody.contains("exampleID=\"456\"")
                || requestBody.contains("<exampleID2>101</exampleID2>") || requestBody.contains("NEED_ANSWER");
    }

    InputStream getXmlFromFile(final String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }
}
