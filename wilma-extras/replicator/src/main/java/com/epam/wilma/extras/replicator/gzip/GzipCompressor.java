package com.epam.wilma.extras.replicator.gzip;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Responsible for compressing an input stream into gzip.
 *
 * @author Tamas_Bihari
 */
public class GzipCompressor {

    private final Logger logger = LoggerFactory.getLogger(GzipCompressor.class);

    /**
     * Compresses an {@link InputStream} object into gzip.
     *
     * @param source the input stream that will be compressed.
     * @return a {@link ByteArrayOutputStream} containing gzipped byte array.
     */
    public InputStream compress(final InputStream source) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gout = new GZIPOutputStream(baos);
            //... Code to read from your original uncompressed data and write to gout.
            IOUtils.copy(source, gout);
            gout.finish();
            gout.close();
        } catch (IOException e) {
            logger.error("gzip - Error when compress inputStream, exception message:" + e.getMessage());
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
