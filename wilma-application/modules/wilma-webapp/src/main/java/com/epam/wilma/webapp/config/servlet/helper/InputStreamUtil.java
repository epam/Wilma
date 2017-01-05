package com.epam.wilma.webapp.config.servlet.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

/**
 * Provides {@link InputStream} conversions.
 * @author Tunde_Kovacs
 *
 */
@Component
public class InputStreamUtil {

    /**
     * Converts an {@link InputStream} to a string with an "UTF-8" character encoding.
     * @param inputStream the input stream to be converted
     * @return the result of the conversion
     * @throws IOException if an I/O error occured during the conversion
     */
    public String toString(final InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, "utf-8");
    }

    /**
     * Reads an inputStream into a byte array.
     * @param inputStream to read from
     * @return contents of the inputStream as a byte array
     * @throws IOException if an IO error occurs
     */
    public byte[] transformToByteArray(InputStream inputStream) throws IOException {
        return IOUtils.toByteArray(inputStream);
    }
}
