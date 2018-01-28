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

import java.io.ByteArrayInputStream;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.compression.base64.Base64Decoder;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Provides unit tests for the class {@link Base64DecoderProcessor}.
 * @author Tunde_Kovacs
 *
 */
public class Base64DecoderProcessorTest {

    private static final String BODY = "body";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FAST_INFOSET = "fastinfoset";
    private static final String XML_CONTENT = "xml";
    private final byte[] result = new byte[1];

    @Mock
    private WilmaHttpEntity entity;
    @Mock
    private Base64Decoder decoder;

    @InjectMocks
    private Base64DecoderProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(entity.getBody()).willReturn(BODY);
    }

    @Test
    public void testProcessShouldCallDecoderWhenEntityHasBinaryContent() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(FAST_INFOSET);
        given(decoder.decode(BODY)).willReturn(result);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(decoder).decode(BODY);
    }

    @Test
    public void testProcessShouldSetInputstreamWhenEntityHasBinaryContent() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(FAST_INFOSET);
        given(decoder.decode(BODY)).willReturn(result);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity).setInputStream(Mockito.any(ByteArrayInputStream.class));
    }

    @Test
    public void testProcessShouldNotDecompressBase64WhenEntityHasPlainContent() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(XML_CONTENT);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity, never()).setInputStream(Mockito.any(ByteArrayInputStream.class));
    }

    @Test
    public void testProcessShouldNotDecompressBase64WhenContentTypeIsNull() throws ApplicationException {
        //GIVEN
        given(entity.getHeader(CONTENT_TYPE)).willReturn(null);
        //WHEN
        underTest.process(entity);
        //THEN
        verify(entity, never()).setInputStream(Mockito.any(ByteArrayInputStream.class));
    }
}
