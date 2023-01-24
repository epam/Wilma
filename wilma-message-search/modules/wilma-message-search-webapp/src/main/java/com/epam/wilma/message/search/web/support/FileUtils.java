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

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.springframework.stereotype.Component;

/**
 * Wrapper for {@link org.apache.commons.io.FileUtils}.
 *
 * @author Adam_Csaba_Kiraly
 */
@Component
public class FileUtils {

    /**
     * Returns the files contained in the folder (non-recursive) with the provided extension.
     * @param folder the folder to check for files
     * @param extension the provided extension
     * @return the files in the folder of the specified extension
     */
    @SuppressWarnings("unchecked")
    public Collection<File> getFilesWithExtension(final String folder, final String extension) {
        return org.apache.commons.io.FileUtils.listFiles(new File(folder), new String[]{extension}, false);
    }

    /**
     * Reads the contents of a file into a String using the default encoding for the VM. The file is always closed.
     * @param file the file to read, must not be null
     * @return the file contents, never null
     * @throws IOException  in case of an I/O error
     */
    public String readFileToString(final File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(file);
    }
}
