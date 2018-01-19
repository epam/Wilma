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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Component;

/**
 * Factory for creating instances of {@link BufferedWriter}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class BufferedWriterFactory {

    /**
     * Creates a new instance of {@link BufferedWriter}.
     * @param file the system-dependent filename
     * @param outputBufferSize output-buffer size, a positive integer
     * @return the new instance
     * @throws IOException if the named file exists but is a directory
     * rather than a regular file, does not exist but cannot be created,
     * or cannot be opened for any other reason
     */
    public BufferedWriter createBufferedWriter(final String file, final int outputBufferSize) throws IOException {
        return new BufferedWriter(new FileWriter(file), outputBufferSize);
    }
}
