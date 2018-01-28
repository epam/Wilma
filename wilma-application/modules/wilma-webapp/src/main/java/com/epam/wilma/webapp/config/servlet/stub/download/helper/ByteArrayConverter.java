package com.epam.wilma.webapp.config.servlet.stub.download.helper;
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

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

/**
 * Provides byte array conversions.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ByteArrayConverter {

    /**
     * Converts a byte array into string using UTF-8 character encoding.
     * @param xml the byte array to be converted
     * @return the result of the conversion
     * @throws IOException if an I/O error occurs during conversion
     */
    public String toString(final byte[] xml) throws IOException {
        return IOUtils.toString(xml, "UTF-8");
    }

}
