package com.epam.wilma.gepard.testclient.compression.gzip;
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

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This class compresses/decompresses the input stream with GZIP.
 */
public class GzipCompressor {

    /**
     * Compress the input stream with GZIP.
     *
     * @param inputStream is the input
     * @return with compressed format
     * @throws IOException if problem occurs during the compression
     */
    public InputStream compress(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(baos);
        //... Code to read from your original uncompressed data and write to gout.
        IOUtils.copy(inputStream, gout);
        gout.finish();
        //Convert to InputStream.
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * De-Compress the input stream from GZIP.
     *
     * @param inputStream is the input
     * @return with de-compressed format
     * @throws IOException if problem occurs during the de-compression
     */
    public ByteArrayInputStream decompress(final InputStream inputStream) throws IOException {
        OutputStream writer = new ByteArrayOutputStream();
        GZIPInputStream gzipStream = new GZIPInputStream(inputStream);
        IOUtils.copy(gzipStream, writer);
        return new ByteArrayInputStream(((ByteArrayOutputStream) writer).toByteArray());
    }
}
