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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Provides unit tests for the class {@link WilmaHttpRequestCloner}.
 * @author Tunde_Kovacs
 *
 */
public class WilmaHttpRequestClonerTest {

    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final String GZIP_CONTENT = "gzip";

    private static final String XML_CONTENT = "application/xml";

    private WilmaHttpRequest request;

    private WilmaHttpRequestCloner underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new WilmaHttpRequestCloner();
        request = new WilmaHttpRequest();
    }

    @Test
    public void testCloneRequestShouldReturnRequestBody() {
        //GIVEN
        request.setBody("body");
        //WHEN
        WilmaHttpRequest actual = underTest.cloneRequest(request);
        //THEN
        assertEquals(actual.getBody(), request.getBody());
    }

    @Test
    public void testCloneRequestShouldCloneRequestLine() {
        //GIVEN
        request.setRequestLine("request-line");
        //WHEN
        WilmaHttpRequest actual = underTest.cloneRequest(request);
        //THEN
        assertEquals(actual.getRequestLine(), request.getRequestLine());
    }

    @Test
    public void testCloneRequestShouldCloneRequestURI() throws URISyntaxException {
        //GIVEN
        request.setUri(new URI("blabla"));
        //WHEN
        WilmaHttpRequest actual = underTest.cloneRequest(request);
        //THEN
        assertEquals(actual.getUri(), request.getUri());
    }

    @Test
    public void testCloneRequestShouldReturnRequestHeaders() {
        //GIVEN
        request.addHeader(CONTENT_TYPE_HEADER, XML_CONTENT);
        request.addHeader(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpRequest actual = underTest.cloneRequest(request);
        //THEN
        assertEquals(actual.getHeader(CONTENT_TYPE_HEADER), XML_CONTENT);
        assertEquals(actual.getHeader(CONTENT_ENCODING_HEADER), GZIP_CONTENT);
    }

    @Test
    public void testCloneRequestShouldReturnRequestHeaderUpdates() {
        //GIVEN
        request.addHeaderUpdate(CONTENT_TYPE_HEADER, XML_CONTENT);
        request.addHeaderUpdate(CONTENT_ENCODING_HEADER, GZIP_CONTENT);
        //WHEN
        WilmaHttpRequest actual = underTest.cloneRequest(request);
        //THEN
        assertEquals(actual.getHeaderUpdateValue(CONTENT_TYPE_HEADER), XML_CONTENT);
        assertEquals(actual.getHeaderUpdateValue(CONTENT_ENCODING_HEADER), GZIP_CONTENT);
    }

    @Test
    public void testCloneRequestShouldReturnRequestHeaderRemovals() {
        //GIVEN
        request.addHeaderRemove(CONTENT_TYPE_HEADER);
        request.addHeaderRemove(CONTENT_ENCODING_HEADER);
        //WHEN
        WilmaHttpRequest actual = underTest.cloneRequest(request);
        //THEN
        assertTrue(actual.getHeaderChanges().entrySet().size() == 2);
    }
}
