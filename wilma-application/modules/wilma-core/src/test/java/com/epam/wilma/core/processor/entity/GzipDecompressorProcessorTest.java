package com.epam.wilma.core.processor.entity;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.compression.gzip.GzipCompressionService;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Provides unit tests for the class {@link GzipDecompressorProcessor}.
 * @author Tunde_Kovacs
 *
 */
public class GzipDecompressorProcessorTest {

    private static final String GZIP_ENCODING = "Content-Encoding";
    private static final String GZIP = "gzip";

    @Mock
    private GzipCompressionService decompressor;
    @Mock
    private WilmaHttpEntity entity;
    @Mock
    private InputStream inputStream;
    @Mock
    private ByteArrayOutputStream body;

    @InjectMocks
    private GzipDecompressorProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        byte[] byteArray = new byte[0];
        given(body.toByteArray()).willReturn(byteArray);
    }

    @Test
    public void testProcessShouldCallDecompressor() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(GZIP_ENCODING)).willReturn(GZIP);
        given(entity.getInputStream()).willReturn(inputStream);
        given(decompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(decompressor).decompress(inputStream);
    }

    @Test
    public void testProcessShouldSetBodyWhenDecompressionNeeded() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(GZIP_ENCODING)).willReturn(GZIP);
        given(entity.getInputStream()).willReturn(inputStream);
        given(decompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity).setBody(body.toString());
    }

    @Test
    public void testProcessShouldNotSetBodyWhenDecompressionNotNeeded() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(GZIP_ENCODING)).willReturn("xml");
        given(entity.getInputStream()).willReturn(inputStream);
        given(decompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(decompressor, never()).decompress(inputStream);
    }

    @Test
    public void testProcessShouldNotSetBodyWhenHeaderIsNull() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(GZIP_ENCODING)).willReturn(null);
        given(entity.getInputStream()).willReturn(inputStream);
        given(decompressor.compress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(decompressor, never()).decompress(inputStream);
    }

}
