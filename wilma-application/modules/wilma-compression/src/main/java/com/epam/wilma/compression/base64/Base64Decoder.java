package com.epam.wilma.compression.base64;

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

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * Decodes a string from base64.
 * @author Tunde_Kovacs
 *
 */
@Component
public class Base64Decoder {

    /**
     * Decodes a string from base64.
     * @param base64Content the string to be decoded
     * @return the result of the decoding
     */
    public byte[] decode(final String base64Content) {
        return Base64.decodeBase64(base64Content);
    }
}
