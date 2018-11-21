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

import java.util.zip.ZipEntry;
import org.springframework.stereotype.Component;

/**
 * Class creates a ZipEntry from a file name.
 * @author Tibor_Kovacs
 *
 */
@Component
public class ZipEntryFactory {
    /**
     * This method creates a new ZipEntry from the given file name.
     * @param fileName is a name of a file
     * @return with a ZipEntry
     */
    public ZipEntry createZipEntry(final String fileName) {
        return new ZipEntry(fileName);
    }
}
