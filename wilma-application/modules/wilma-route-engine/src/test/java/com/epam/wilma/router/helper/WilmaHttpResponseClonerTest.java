package com.epam.wilma.router.helper;
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


import com.epam.wilma.domain.http.WilmaHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides unit tests for the class {@link WilmaHttpResponseCloner}.
 *
 * @author Tibor_Kovacs
 */
public class WilmaHttpResponseClonerTest {

    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String GZIP_CONTENT = "gzip";
    private static final String XML_CONTENT = "application/xml";

    private WilmaHttpResponse response;

    private WilmaHttpResponseCloner underTest;

    @BeforeEach
    public void setUp() {
        underTest = new WilmaHttpResponseCloner();
        response = new WilmaHttpResponse(false);
    }

    @Test
    public void testCloneResponseShouldReturnResponseBody() {
        //GIVEN
        response.setBody("body");
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(response.getBody(), actual.getBody());
    }

    @Test
    public void testCloneResponseShouldReturnResponseStatusCode() {
        //GIVEN
        response.setStatusCode(HttpServletResponse.SC_OK);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(response.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testCloneResponseShouldReturnResponseContentType() {
        //GIVEN
        response.setContentType("text/html");
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(response.getContentType(), actual.getContentType());
    }

    @Test
    public void testCloneResponseShouldReturnResponseHeaders() {
        //GIVEN
        response.addHeader(CONTENT_TYPE_HEADER, XML_CONTENT);
        response.addHeader(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(XML_CONTENT, actual.getHeader(CONTENT_TYPE_HEADER));
        assertEquals(GZIP_CONTENT, actual.getHeader(CONTENT_ENCODING_HEADER));
    }

    @Test
    public void testCloneResponseShouldReturnResponseHeaderUpdates() {
        //GIVEN
        response.addHeaderUpdate(CONTENT_TYPE_HEADER, XML_CONTENT);
        response.addHeaderUpdate(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(XML_CONTENT, actual.getHeaderUpdateValue(CONTENT_TYPE_HEADER));
        assertEquals(GZIP_CONTENT, actual.getHeaderUpdateValue(CONTENT_ENCODING_HEADER));
    }

    @Test
    public void testCloneResponseShouldReturnResponseHeaderRemovals() {
        //GIVEN
        response.addHeaderRemove(CONTENT_TYPE_HEADER);
        response.addHeaderRemove(CONTENT_ENCODING_HEADER);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(2, actual.getHeaderChanges().entrySet().size());
    }

    @Test
    public void testCloneResponseShouldReturnRequestHeaders() {
        //GIVEN
        response.addRequestHeader(CONTENT_TYPE_HEADER, XML_CONTENT);
        response.addRequestHeader(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(XML_CONTENT, actual.getRequestHeader(CONTENT_TYPE_HEADER));
        assertEquals(GZIP_CONTENT, actual.getRequestHeader(CONTENT_ENCODING_HEADER));
    }
}
