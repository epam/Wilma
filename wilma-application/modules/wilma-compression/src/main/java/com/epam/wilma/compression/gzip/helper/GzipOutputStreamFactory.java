package com.epam.wilma.compression.gzip.helper;
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
import java.util.zip.GZIPOutputStream;

import org.springframework.stereotype.Component;

/**
 * Factory for creating new instances of {@link GZIPOutputStream}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class GzipOutputStreamFactory {

    /**
     * Creates a new instance of {@link GZIPOutputStream}.
     * @param baos the byteArrayOutpuStream that is given as argument
     * to the GZIPOutputStream's constructor
     * @return the new instance
     * @throws IOException if an I/O error has occurred
     */
    public GZIPOutputStream createOutputStream(final ByteArrayOutputStream baos) throws IOException {
        return new GZIPOutputStream(baos);
    }
}
