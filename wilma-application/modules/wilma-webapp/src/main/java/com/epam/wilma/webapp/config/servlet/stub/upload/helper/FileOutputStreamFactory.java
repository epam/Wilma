package com.epam.wilma.webapp.config.servlet.stub.upload.helper;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.stereotype.Component;

/**
 * Factory for creating instances of {@link FileOutputStream}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class FileOutputStreamFactory {

    /**
     * Creates a new instance of {@link FileOutputStream}.
     * @param file the file the outputstream will write to
     * @return the new instance
     * @throws FileNotFoundException if the file exists but is a directory rather
     * than a regular file, does not exist but cannot be created,
     * or cannot be opened for any other reason
     */
    public FileOutputStream createFileOutputStream(final File file) throws FileNotFoundException {
        return new FileOutputStream(file.getAbsoluteFile());
    }

}
