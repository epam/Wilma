package com.epam.wilma.webapp.stub.servlet.helper;
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

import java.io.ByteArrayInputStream;

import org.springframework.stereotype.Component;

/**
 * Factory for creating instances of {@link ByteArrayInputStream}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ByteArrayInputStreamFactory {

    /**
     * Creates a new {@link ByteArrayInputStream} from a byte array.
     * @param byteArray the source from which the inputstream is built of
     * @return the new instance
     */
    public ByteArrayInputStream createByteArrayInputStream(final byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }
}
