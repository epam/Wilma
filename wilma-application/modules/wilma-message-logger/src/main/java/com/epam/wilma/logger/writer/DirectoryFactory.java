package com.epam.wilma.logger.writer;
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

import org.springframework.stereotype.Component;

/**
 * Factory for creating instances of {@link File}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class DirectoryFactory {

    /**
     * Creates a new directory with the specified target foler path.
     * @param targetFolderPath the path where the directory will be created at.
     * @return the new instance
     */
    public File createNewDirectory(final String targetFolderPath) {
        return new File(targetFolderPath);
    }

}
