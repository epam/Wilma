package com.epam.wilma.compression.gzip;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;
import com.epam.wilma.compression.gzip.helper.GzipInputStreamFactory;
import com.epam.wilma.compression.gzip.helper.GzipOutputStreamFactory;
import com.epam.wilma.domain.exception.SystemException;

/**
 * Provides unit tests for the {@link GzipCompressionService} class.
 * @author Tunde_Kovacs
 *
 */
public class GzipCompressionServiceTest {

    @Mock
    private GZIPOutputStream gzipOutputStream;
    @Mock
    private ByteArrayOutputStream baos;
    @Mock
    private InputStream source;
    @Mock
    private ByteArrayOutputStreamFactory outputStreamFactory;
    @Mock
    private GzipOutputStreamFactory gzipOutpuStreamFactory;
    @Mock
    private GzipInputStreamFactory gzipInputStreamFactory;
    @Mock
    private GZIPInputStream gzipInputStream;

    @InjectMocks
    private GzipCompressionService underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(gzipOutpuStreamFactory.createOutputStream(baos)).willReturn(gzipOutputStream);
        given(outputStreamFactory.createByteArrayOutputStream()).willReturn(baos);
    }

    @Test
    public void testCompressShouldReturnABtyeArrayOutputStream() throws IOException {
        //GIVEN
        given(source.read((byte[]) Mockito.any())).willReturn(-1);
        //WHEN
        ByteArrayOutputStream actual = underTest.compress(source);
        //THEN
        assertEquals(actual, baos);
    }

    @Test
    public void testCompressShouldCallFinish() throws IOException {
        //GIVEN
        given(source.read((byte[]) Mockito.any())).willReturn(-1);
        //WHEN
        underTest.compress(source);
        //THEN
        verify(gzipOutputStream).finish();
    }

    @Test
    public void testCompressShouldCallClose() throws IOException {
        //GIVEN
        given(source.read((byte[]) Mockito.any())).willReturn(-1);
        //WHEN
        underTest.compress(source);
        //THEN
        verify(gzipOutputStream).close();
    }

    @Test(expectedExceptions = SystemException.class)
    public void testCompressShouldThrowSystemExceptionWhenIOExceptionIsCatched() throws IOException {
        //GIVEN
        given(source.read((byte[]) Mockito.any())).willThrow(new IOException());
        //WHEN
        underTest.compress(source);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = SystemException.class)
    public void testDecompressShouldThrowExceptionWhenStreamCopyThrowsIOException() throws IOException {
        //GIVEN
        given(gzipInputStreamFactory.createInputStream(source)).willReturn(gzipInputStream);
        given(gzipInputStream.read((byte[]) Mockito.any())).willThrow(new IOException());
        //WHEN
        underTest.decompress(source);
        //THEN it should throw exception
    }

    @Test
    public void testDecompressShouldDoDecompression() throws IOException {
        //GIVEN
        given(gzipInputStreamFactory.createInputStream(source)).willReturn(gzipInputStream);
        given(gzipInputStream.read((byte[]) Mockito.any())).willReturn(1, -1);
        //WHEN
        underTest.decompress(source);
        //THEN
        verify(gzipInputStream, times(2)).read(Mockito.any(byte[].class));
    }

    @Test
    public void testDecompressShouldReturnWriter() throws IOException {
        //GIVEN
        given(outputStreamFactory.createByteArrayOutputStream()).willReturn(baos);
        given(gzipInputStreamFactory.createInputStream(source)).willReturn(gzipInputStream);
        given(gzipInputStream.read((byte[]) Mockito.any())).willReturn(1, -1);
        //WHEN
        ByteArrayOutputStream actual = underTest.decompress(source);
        //THEN
        assertEquals(actual, baos);
    }
}
