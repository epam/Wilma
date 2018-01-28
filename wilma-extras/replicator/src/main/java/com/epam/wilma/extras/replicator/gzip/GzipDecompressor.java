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
import java.util.zip.GZIPInputStream;

/**
 * Decompresses requests that are encoded with gzip.
 *
 * @author Tamas_Bihari
 */
public class GzipDecompressor {
    private final Logger logger = LoggerFactory.getLogger(GzipDecompressor.class);

    /**
     * Decompresses an input stream from gzip.
     *
     * @param body the inputstream that is decompressed.
     * @return the decompressed {@link ByteArrayOutputStream}
     */
    public InputStream decompress(final InputStream body) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        try {
            GZIPInputStream gzipStream = new GZIPInputStream(body);
            IOUtils.copy(gzipStream, writer);
        } catch (IOException e) {
            logger.error("gzip - Error when decompress inputStream, exception message:" + e.getMessage());
        }
        return new ByteArrayInputStream(writer.toByteArray());
    }

}
