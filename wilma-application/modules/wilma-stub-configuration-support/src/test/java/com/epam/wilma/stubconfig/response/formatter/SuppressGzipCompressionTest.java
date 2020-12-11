package com.epam.wilma.stubconfig.response.formatter;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link SuppressGzipCompression}.
 *
 * @author Tamas_Kohegyi
 */
public class SuppressGzipCompressionTest {

    private static final String ANY_HEADER_VALUE = "value";

    @Mock
    private HttpServletResponse response;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList parameterMap;

    private SuppressGzipCompression underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new SuppressGzipCompression();
    }

    @Test
    public void testFormatTemplateWithoutExistingHeader() {
        //GIVEN
        //SetUp
        given(request.getHeader(SuppressGzipCompression.HEADER_KEY_ACCEPT_ENCODING)).willReturn(SuppressGzipCompression.HEADER_VALUE_GZIP);
        given(response.getHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING)).willReturn(null);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING, SuppressGzipCompression.HEADER_VALUE_GZIP);
    }

    @Test
    public void testFormatTemplateWithExistingHeader() {
        //GIVEN
        //SetUp
        given(request.getHeader(SuppressGzipCompression.HEADER_KEY_ACCEPT_ENCODING)).willReturn(SuppressGzipCompression.HEADER_VALUE_GZIP);
        given(response.getHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING)).willReturn(ANY_HEADER_VALUE);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING, ANY_HEADER_VALUE + "," + SuppressGzipCompression.HEADER_VALUE_GZIP);
    }

}
