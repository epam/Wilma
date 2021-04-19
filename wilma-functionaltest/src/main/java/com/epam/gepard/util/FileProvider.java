package com.epam.gepard.util;
/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.File;

/**
 * Class used for handling File objects.
 *
 * @author tkohegyi
 */
public final class FileProvider {
    /**
     * Returns with File object, based on the specified fileName.
     *
     * @param fileName is the name of the file
     * @return with File object
     */
    public File getFile(final String fileName) {
        return new File(fileName);
    }

    /**
     * Checks if a File exists or not.
     *
     * @param fileName is the name of the file.
     * @return true if file exists, otherwise false
     */
    public boolean isFileAvailable(final String fileName) {
        return isFileAvailable(getFile(fileName));
    }

    /**
     * Checks if a File exists or not.
     *
     * @param f is the File object.
     * @return true if file exists, otherwise false
     */
    public boolean isFileAvailable(final File f) {
        return f.exists();
    }
}
