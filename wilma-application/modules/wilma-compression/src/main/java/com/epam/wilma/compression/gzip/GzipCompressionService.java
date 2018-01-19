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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;
import com.epam.wilma.compression.CompressionService;
import com.epam.wilma.compression.gzip.helper.GzipInputStreamFactory;
import com.epam.wilma.compression.gzip.helper.GzipOutputStreamFactory;
import com.epam.wilma.domain.exception.SystemException;

/**
 * Responsible for compressing and decompressing an input stream into/from gzip.
 * @author Tamas_Bihari
 *
 */
@Component
public class GzipCompressionService implements CompressionService {

    @Autowired
    private GzipOutputStreamFactory gzipOutputStreamFactory;
    @Autowired
    private GzipInputStreamFactory gzipInputStreamFactory;
    @Autowired
    private ByteArrayOutputStreamFactory outputStreamFactory;

    /**
     * Compresses an {@link InputStream} object into gzip.
     * @param inputStream the inputstream that will be compressed
     * @return a {@link ByteArrayOutputStream} containing gzipped byte array
     */
    @Override
    public ByteArrayOutputStream compress(final InputStream inputStream) {
        InputStream source = inputStream;
        ByteArrayOutputStream baos = outputStreamFactory.createByteArrayOutputStream();
        try {
            GZIPOutputStream gout = gzipOutputStreamFactory.createOutputStream(baos);
            //... Code to read from your original uncompressed data and write to gout.
            IOUtils.copy(source, gout);
            gout.finish();
            gout.close();
        } catch (IOException e) {
            throw new SystemException("Could not gzip message body!", e);
        }
        return baos;
    }

    @Override
    public ByteArrayOutputStream decompress(final InputStream inputStream) {
        OutputStream writer = outputStreamFactory.createByteArrayOutputStream();
        try {
            GZIPInputStream gzipStream = gzipInputStreamFactory.createInputStream(inputStream);
            IOUtils.copy(gzipStream, writer);
        } catch (IOException e) {
            throw new SystemException("Could not ungzip message body!", e);
        }
        return (ByteArrayOutputStream) writer;
    }
}
