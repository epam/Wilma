package com.epam.wilma.webapp.stub.response.processor.fis;
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

import com.epam.wilma.compression.fis.FastInfosetCompressionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link FastInfosetCompressorProcessor}.
 *
 * @author Tamas_Bihari
 */
public class FastInfosetCompressorProcessorTest {
    private static final String ACCEPT_VALUE_XML = "application/xml";
    private static final String RESPONSE_BODY = "RESPONSE_BODY";
    private static final String ACCEPT_HEADER_KEY = "Accept";
    private static final String ACCEPT_VALUE_FAST_INFOSET = "application/fastinfoset";

    @Mock
    private FastInfosetCompressionService fastInfosetCompressor;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private ByteArrayOutputStream outputStream;

    @InjectMocks
    private FastInfosetCompressorProcessor underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessShouldCheckAcceptHeaderOfTheRequest() {
        //GIVEN
        given(resp.getContentType()).willReturn(ACCEPT_VALUE_XML);
        //WHEN
        underTest.process(req, resp, RESPONSE_BODY.getBytes());
        //THEN
        verify(req).getHeader(ACCEPT_HEADER_KEY);
    }

    @Test
    public void testProcessShouldDoNothingWhenResponseFISCompressionIsNotNeeded() {
        //GIVEN
        given(resp.getContentType()).willReturn("html");
        given(req.getHeader(ACCEPT_HEADER_KEY)).willReturn(ACCEPT_VALUE_XML);
        //WHEN
        underTest.process(req, resp, RESPONSE_BODY.getBytes());
        //THEN
        verify(fastInfosetCompressor, times(0)).compress(Mockito.any(ByteArrayInputStream.class));
        verify(resp, times(0)).setHeader(ACCEPT_HEADER_KEY, ACCEPT_VALUE_FAST_INFOSET);
    }

    @Test
    public void testProcessShouldDoNothingWhenResponseFISCompressionIsNotNeededBecauseOfRequestHeader() {
        //GIVEN
        given(resp.getContentType()).willReturn(ACCEPT_VALUE_XML);
        given(req.getHeader(ACCEPT_HEADER_KEY)).willReturn(ACCEPT_VALUE_XML);
        //WHEN
        underTest.process(req, resp, RESPONSE_BODY.getBytes());
        //THEN
        verify(fastInfosetCompressor, times(0)).compress(Mockito.any(ByteArrayInputStream.class));
        verify(resp, times(0)).setHeader(ACCEPT_HEADER_KEY, ACCEPT_VALUE_FAST_INFOSET);
    }

    @Test
    public void testProcessShouldDoNothingWhenResponseFISCompressionIsNotNeededBecauseOfRespContentType() {
        //GIVEN
        given(resp.getContentType()).willReturn("html");
        given(req.getHeader(ACCEPT_HEADER_KEY)).willReturn(ACCEPT_VALUE_FAST_INFOSET);
        //WHEN
        underTest.process(req, resp, RESPONSE_BODY.getBytes());
        //THEN
        verify(fastInfosetCompressor, times(0)).compress(Mockito.any(ByteArrayInputStream.class));
        verify(resp, times(0)).setHeader(ACCEPT_HEADER_KEY, ACCEPT_VALUE_FAST_INFOSET);
    }

    @Test
    public void testProcessShouldCompressResponseBodyAndSetContentTypeWhenResponseFISCompressionIsNeeded() {
        //GIVEN
        given(resp.getContentType()).willReturn(ACCEPT_VALUE_XML);
        given(req.getHeader(ACCEPT_HEADER_KEY)).willReturn(ACCEPT_VALUE_FAST_INFOSET);
        given(fastInfosetCompressor.compress(Mockito.any(ByteArrayInputStream.class))).willReturn(outputStream);
        given(outputStream.toByteArray()).willReturn(RESPONSE_BODY.getBytes());
        //WHEN
        byte[] actual = underTest.process(req, resp, RESPONSE_BODY.getBytes());
        //THEN
        verify(resp).setContentType(ACCEPT_VALUE_FAST_INFOSET);
        String actualString = new String(actual);
        assertEquals(RESPONSE_BODY, actualString);
    }

    @Test
    public void testProcessShouldCompressResponseBodyAndSetContentTypeWhenContentTypeIsFastInfoset() {
        //GIVEN
        given(resp.getContentType()).willReturn(ACCEPT_VALUE_FAST_INFOSET);
        given(req.getHeader(ACCEPT_HEADER_KEY)).willReturn(ACCEPT_VALUE_FAST_INFOSET);
        given(fastInfosetCompressor.compress(Mockito.any(ByteArrayInputStream.class))).willReturn(outputStream);
        given(outputStream.toByteArray()).willReturn(RESPONSE_BODY.getBytes());
        //WHEN
        byte[] actual = underTest.process(req, resp, RESPONSE_BODY.getBytes());
        //THEN
        verify(resp).setContentType(ACCEPT_VALUE_FAST_INFOSET);
        String actualString = new String(actual);
        assertEquals(RESPONSE_BODY, actualString);
    }

}
