package com.epam.wilma.webapp.stub.response.processor.gzip;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.wilma.compression.gzip.GzipCompressionService;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

/**
 * Tests for {@link GzipCompressorProcessor}.
 * @author Tamas_Bihari
 *
 */
public class GzipCompressorProcessorTest {
    private static final String HEADER_VALUE_GZIP = "gzip";
    private static final String HEADER_KEY_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_KEY_CONTENT_ENCODING = "Content-Encoding";

    private byte[] responseBody;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private GzipCompressionService gzipCompressor;
    @Mock
    private ByteArrayInputStreamFactory inputStreamFactory;
    @Mock
    private ByteArrayOutputStream outputStream;
    @Mock
    private ByteArrayInputStream inputStream;

    @InjectMocks
    private GzipCompressorProcessor underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        responseBody = "SIMPLE_RESPONSE".getBytes();
    }

    @Test
    public void testProcessShouldCheckTheAcceptEncodingHeaderInTheRequest() {
        //GIVEN in setUp
        //WHEN
        underTest.process(req, resp, responseBody);
        //THEN
        verify(req).getHeader(HEADER_KEY_ACCEPT_ENCODING);
    }

    @Test
    public void testProcessShouldDoNothingWhenGzipCompressionIsNotNeeded() {
        //GIVEN
        given(req.getHeader(HEADER_KEY_ACCEPT_ENCODING)).willReturn("nothing");
        //WHEN
        underTest.process(req, resp, responseBody);
        //THEN
        verify(resp, times(0)).addHeader(HEADER_KEY_CONTENT_ENCODING, HEADER_VALUE_GZIP);
        verify(gzipCompressor, times(0)).compress(any(ByteArrayInputStream.class));
    }

    @Test
    public void testProcessShouldAddContentEncodingHeaderWithValueAndCompressResponseBodyWhenGzipCompressionIsNeeded() {
        //GIVEN
        given(req.getHeader(HEADER_KEY_ACCEPT_ENCODING)).willReturn(HEADER_VALUE_GZIP);
        given(gzipCompressor.compress(any(ByteArrayInputStream.class))).willReturn(outputStream);
        given(outputStream.toByteArray()).willReturn(responseBody);
        given(inputStreamFactory.createByteArrayInputStream(any())).willReturn(inputStream);
        //WHEN
        byte[] actual = underTest.process(req, resp, responseBody);
        //THEN
        verify(resp).addHeader(HEADER_KEY_CONTENT_ENCODING, HEADER_VALUE_GZIP);
        assertEquals(actual, responseBody);
    }
}
