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

import com.epam.wilma.compression.fis.FastInfosetCompressionService;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Provides unit tests for the class {@link FastInfosetDecompressorProcessor}.
 * @author Tunde_Kovacs
 *
 */
public class FastInfosetDecompressorProcessorTest {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FAST_INFO_SET = "fastinfoset";

    @Mock
    private WilmaHttpEntity entity;
    @Mock
    private FastInfosetCompressionService fastinfosetDecompressor;
    @Mock
    private InputStream inputStream;
    @Mock
    private ByteArrayOutputStream body;

    @InjectMocks
    private FastInfosetDecompressorProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessShouldCallDecompressWhenFIDecompressionNeeded() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(FAST_INFO_SET);
        given(entity.getInputStream()).willReturn(inputStream);
        given(fastinfosetDecompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(fastinfosetDecompressor).decompress(inputStream);
    }

    @Test
    public void testProcessShouldSetBodyWhenFIDecompressionNeeded() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(FAST_INFO_SET);
        given(entity.getInputStream()).willReturn(inputStream);
        given(fastinfosetDecompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity).setBody(body.toString());
    }

    @Test
    public void testProcessShouldSetNotBodyWhenFIDecompressionNotNeeded() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn("xml");
        given(entity.getInputStream()).willReturn(inputStream);
        given(fastinfosetDecompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity, never()).setBody(body.toString());
    }

    @Test
    public void testProcessShouldSetNotBodyWhenHeaderIsNull() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(null);
        given(entity.getInputStream()).willReturn(inputStream);
        given(fastinfosetDecompressor.decompress(inputStream)).willReturn(body);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity, never()).setBody(body.toString());
    }
}
