package com.epam.wilma.message.search.web.support;
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

import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

/**
 * Class creates a ZipOutputStream from an OutputStream.
 *
 * @author Tibor_Kovacs
 */
@Component
public class ZipOutputStreamFactory {
    /**
     * This method creates a new ZipOutputStream from the given OutputStream.
     * @param outputStream is the source of the new stream.
     * @return with a ZipOutputStream
     */
    public ZipOutputStream createZipOutputStream(final OutputStream outputStream) {
        return new ZipOutputStream(outputStream);
    }
}
