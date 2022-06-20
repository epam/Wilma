package com.epam.wilma.message.search.lucene.index.helper;
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

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Factory for {@link FileWrapper}.
 * @author Tamas_Bihari
 *
 */
@Component
public class FileWrapperFactory {

    /**
     * Create a new {@link FileWrapper} instance.
     * @param path is the file's path
     * @return with a new {@link FileWrapper} instance
     */
    public FileWrapper createFile(final String path) {
        return new FileWrapper(path);
    }

    /**
     * Create a new {@link File} instance.
     *
     * @param file is the parent file/folder
     * @param child is the child file
     * @return with a new {@link File} instance
     */
    public FileWrapper createFile(FileWrapper file, String child) {
        return new FileWrapper(file.getFile(), child);
    }
}
