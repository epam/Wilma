package com.epam.wilma.test.server.compress.gzip;
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
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

import com.epam.wilma.test.server.SystemException;

/**
 * Responsible for compressing an input stream into gzip.
 * @author Tamas_Bihari
 *
 */
public class GzipCompressor {

    /**
     * Compresses an {@link InputStream} object into gzip.
     * @param source the input stream that will be compressed.
     * @return a {@link ByteArrayOutputStream} containing gzipped byte array.
     */
    public ByteArrayOutputStream compress(final InputStream source) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gout = new GZIPOutputStream(baos);
            //... Code to read from your original uncompressed data and write to gout.
            IOUtils.copy(source, gout);
            gout.finish();
            gout.close();
        } catch (IOException e) {
            throw new SystemException("error", e);
        }
        return baos;
    }
}
