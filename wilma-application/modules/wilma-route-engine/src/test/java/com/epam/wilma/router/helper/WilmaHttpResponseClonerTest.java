package com.epam.wilma.router.helper;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import static org.testng.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Provides unit tests for the class {@link WilmaHttpResponseCloner}.
 * @author Tibor_Kovacs
 *
 */
public class WilmaHttpResponseClonerTest {

    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String GZIP_CONTENT = "gzip";
    private static final String XML_CONTENT = "application/xml";

    private WilmaHttpResponse response;

    private WilmaHttpResponseCloner underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new WilmaHttpResponseCloner();
        response = new WilmaHttpResponse();
    }

    @Test
    public void testCloneResponseShouldReturnResponseBody() {
        //GIVEN
        response.setBody("body");
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(actual.getBody(), response.getBody());
    }

    @Test
    public void testCloneResponseShouldReturnResponseStatusCode() {
        //GIVEN
        response.setStatusCode(HttpServletResponse.SC_OK);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(actual.getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testCloneResponseShouldReturnResponseContentType() {
        //GIVEN
        response.setContentType("text/html");
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(actual.getContentType(), response.getContentType());
    }

    @Test
    public void testCloneResponseShouldReturnResponseHeaders() {
        //GIVEN
        response.addHeader(CONTENT_TYPE_HEADER, XML_CONTENT);
        response.addHeader(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(actual.getHeader(CONTENT_TYPE_HEADER), XML_CONTENT);
        assertEquals(actual.getHeader(CONTENT_ENCODING_HEADER), GZIP_CONTENT);
    }

    @Test
    public void testCloneResponseShouldReturnResponseExtraHeaders() {
        //GIVEN
        response.addExtraHeader(CONTENT_TYPE_HEADER, XML_CONTENT);
        response.addExtraHeader(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(actual.getExtraHeader(CONTENT_TYPE_HEADER), XML_CONTENT);
        assertEquals(actual.getExtraHeader(CONTENT_ENCODING_HEADER), GZIP_CONTENT);
    }

    @Test
    public void testCloneResponseShouldReturnRequestHeaders() {
        //GIVEN
        response.addRequestHeader(CONTENT_TYPE_HEADER, XML_CONTENT);
        response.addRequestHeader(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpResponse actual = underTest.cloneResponse(response);
        //THEN
        assertEquals(actual.getRequestHeader(CONTENT_TYPE_HEADER), XML_CONTENT);
        assertEquals(actual.getRequestHeader(CONTENT_ENCODING_HEADER), GZIP_CONTENT);
    }
}
