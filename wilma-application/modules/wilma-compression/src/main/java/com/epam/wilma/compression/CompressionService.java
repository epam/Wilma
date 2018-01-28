package com.epam.wilma.compression;
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
import java.io.InputStream;

/**
 * Provides compression/decompression of an {@link InputStream}.
 * @author Tunde_Kovacs
 *
 */
public interface CompressionService {

    /**
     * Does the compression of an InputStream.
     * @param inputStream the {@link InputStream} that will be compressed.
     * @return a ByteArrayOutputStream  containing the compressed information
     */
    ByteArrayOutputStream compress(InputStream inputStream);

    /**
     * Does the decompression of an InputStream.
     * @param inputStream the {@link InputStream} that will be decompressed.
     * @return a ByteArrayOutputStream  containing the decompressed information
     */
    ByteArrayOutputStream decompress(InputStream inputStream);
}
